package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskDataType;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.BusGraphRepository;
import com.naver.pubtrans.itn.api.repository.BusRouteRepository;
import com.naver.pubtrans.itn.api.repository.BusStopRepository;
import com.naver.pubtrans.itn.api.repository.TaskRepository;
import com.naver.pubtrans.itn.api.vo.bus.graph.BusStopGraphVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.output.GeoJsonOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BisBusRouteChangeGraphVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BisBusRouteChangeVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteTaskVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteBypassOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteCompanyOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteDetailOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteTaskDetailOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopMappingVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopVo;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 버스 노선관리 서비스
 * @author adtec10
 *
 */
@Service
public class BusRouteService {

	private final OutputFmtUtil outputFmtUtil;

	private final CommonService commonService;

	private final BusGraphService busGraphService;

	private final TaskService taskService;

	private final BusRouteRepository busRouteRepository;

	private final BusGraphRepository busGraphRepository;

	private final TaskRepository taskRepository;

	private final BusStopRepository busStopRepository;


	@Autowired
	BusRouteService(OutputFmtUtil outputFmtUtil, CommonService commonService, BusGraphService busGraphService, TaskService taskService,
		BusRouteRepository busRouteRepository, BusGraphRepository busGraphRepository, TaskRepository taskRepository, BusStopRepository busStopRepository){

		this.outputFmtUtil = outputFmtUtil;
		this.commonService = commonService;
		this.busGraphService = busGraphService;
		this.taskService = taskService;
		this.busRouteRepository = busRouteRepository;
		this.busGraphRepository = busGraphRepository;
		this.taskRepository = taskRepository;
		this.busStopRepository = busStopRepository;
	}

	/**
	 * 버스 노선 목록을 가져온다
	 * @param busRouteSearchVo - 버스노선 검색조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusRouteList(BusRouteSearchVo busRouteSearchVo) throws Exception {

		/**
		 * 1. 페이지 목록 조회
		 */

		// 전체 목록 수 가져오기
		int totalListCnt = busRouteRepository.getBusRouteListTotalCnt(busRouteSearchVo);

		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalListCnt, busRouteSearchVo.getPageNo(), busRouteSearchVo.getListSize());

		// 목록 조회 페이징 정보 set
		busRouteSearchVo.setStartPageLimit(pagingVo.getStartPageLimit());
		busRouteSearchVo.setEndPageLimit(pagingVo.getEndPageLimit());

		// 목록 조회
		List<BusRouteListOutputVo> busRouteListOutputVoList = busRouteRepository.selectBusRouteList(busRouteSearchVo);


		/**
		 * 2. 검색 폼 데이터 구조 생성
		 */
		// 사용하고자 하는 컬럼 목록
		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("route_id");
		usableColumnNameList.add("route_name");
		usableColumnNameList.add("city_code");
		usableColumnNameList.add("bus_class");


		// 노선 기본정보 테이블
		List<CommonSchema> tbRoutesSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_ROUTES.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);

		// 노선 부가정보 테이블
		List<CommonSchema> tbBusRoutesInfoSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_BUS_ROUTES_INFO.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);

		List<CommonSchema> commonSchemaList = new ArrayList<>();
		commonSchemaList.addAll(tbRoutesSchemaList);
		commonSchemaList.addAll(tbBusRoutesInfoSchemaList);

		// 동일 컬럼에 대해 중복을 제거
		List<CommonSchema> distinctCommonSchemaVoList = outputFmtUtil.distinctCommonSchemaList(commonSchemaList);


		/**
		 * 3. 공통 출력포맷 생성
		 */
		CommonResult commonResult = outputFmtUtil.setCommonListFmt(distinctCommonSchemaVoList, pagingVo, busRouteListOutputVoList);
		return commonResult;
	}

	/**
	 * 노선 작업 히스토리 요약목록을 가져온다
	 * @param busRouteId - 버스 노선ID
	 * @param searchVo - 페이징 정보
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusRouteTaskSummaryList(int busRouteId, SearchVo searchVo) throws Exception {
		return taskService.getTaskSummaryList(busRouteId, TaskDataType.ROUTE.getCode(), searchVo);
	}

	/**
	 * 버스 정류장 사이의 그래프 정보를 가져온다
	 * <pre>
	 * 단순 두 정류장 사이의 그래프 정보 호출 : busStopIds=55000837,55000520
	 *   - 55000837 ~ 55000520 그래프 정보
	 * 연속되는 정류장 사이의 그래프 정보 호출 : busStopIds=55000837,55000520,55000529
	 *   - 55000837 ~ 55000520 그래프 정보
	 *   - 55000520 ~ 55000529 그래프 정보
	 * </pre>
	 * @param busStopIds - 정류장ID 정보
	 * @return
	 * @throws Exception
	 */
	public CommonResult getGraphInfoBetweenBusStops(List<String> busStopIds) throws Exception {

		// 정류장 최소 개수 확인(2개. 출발-도착 정류장)
		if(busStopIds.size() < 2) {
			throw new ApiException(ResultCode.PARAMETER_RULE_ERROR.getApiErrorCode(), ResultCode.PARAMETER_RULE_ERROR.getDisplayMessage());
		}


		/**
		 * 버스 정류장간의 구간 그래프 정보를 가져오기 위한 검색 조건 목록을 생성한다.
		 * 각 구간 검색 정보는 '출발 정류장 ID', '도착 정류장 ID'로 구성된다.
		 */
		List<BusStopGraphSearchVo> busStopGraphSearchVoList = new ArrayList<>();
		for(int i=0; i < busStopIds.size()-1; i++) {
			int startBusStopId = Integer.parseInt(busStopIds.get(i));
			int endBusStopId = Integer.parseInt(busStopIds.get(i+1));

			busStopGraphSearchVoList.add(new BusStopGraphSearchVo(startBusStopId, endBusStopId));
		}

		// 구간별 그래프 정보 목록을 가져온다
		List<BusStopGraphVo> busStopGraphVoList = busGraphRepository.selectBusStopGraphList(busStopGraphSearchVoList);

		// 구간 그래프 정보를 GeoJson Feature 목록 형태로 만든다.
		List<GeoJsonFeatureVo> geoJsonFeatureVoList = busGraphService.makeGeoJsonFeatureList(busStopGraphSearchVoList, busStopGraphVoList);

		GeoJsonOutputVo geoJsonOutputVo = new GeoJsonOutputVo();
		geoJsonOutputVo.setFeatures(geoJsonFeatureVoList);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(geoJsonOutputVo);
		return commonResult;

	}


	/**
	 * 버스 노선 상세정보의 전체 스키마 정보를 가져온다
	 * @return
	 * @throws Exception
	 */
	public List<CommonSchema> selectBusRouteSchemaAll() throws Exception {
		List<CommonSchema> commonSchemaList = new ArrayList<>();
		commonSchemaList.addAll(this.selectBusRouteSchema());
		commonSchemaList.addAll(this.selectBusRouteInfoSchema());
		commonSchemaList.addAll(this.selectBusRouteMappingSchema());
		commonSchemaList.addAll(this.selectBusRouteBypassMappingSchema());
		commonSchemaList.addAll(this.selectBusRouteCalendarSchema());
		commonSchemaList.addAll(taskService.selectTaskSchemaMinimal());

		// 동일 컬럼에 대해 중복을 제거
		List<CommonSchema> distinctCommonSchemaVoList = outputFmtUtil.distinctCommonSchemaList(commonSchemaList);
		return distinctCommonSchemaVoList;
	}

	/**
	 * 버스노선 기본정보 테이블 스키마
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectBusRouteSchema() throws Exception {
		ArrayList<String> ignoreColumnNameList = new ArrayList<>();
		ignoreColumnNameList.add("tranport_id");

		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_ROUTES.getName(), CommonConstant.IGNORE_COLUMN, ignoreColumnNameList);
		return commonSchemaList;
	}

	/**
	 * 버스노선 부가정보 테이블 스키마
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectBusRouteInfoSchema() throws Exception {
		ArrayList<String> ignoreColumnNameList = new ArrayList<>();
		ignoreColumnNameList.add("alias_kor");
		ignoreColumnNameList.add("express_way");
		ignoreColumnNameList.add("run_bus");
		ignoreColumnNameList.add("main_bus_stop");
		ignoreColumnNameList.add("run_time");
		ignoreColumnNameList.add("bus_stop_cnt");
		ignoreColumnNameList.add("service_id");

		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_BUS_ROUTES_INFO.getName(), CommonConstant.IGNORE_COLUMN, ignoreColumnNameList);
		return commonSchemaList;
	}

	/**
	 * 버스노선 BIS 매핑정보 테이블 스키마
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectBusRouteMappingSchema() throws Exception {
		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("local_route_id");

		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_BUS_ROUTE_MAPPING.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);
		return commonSchemaList;
	}

	/**
	 * 우회노선 정보 테이블 스키마
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectBusRouteBypassMappingSchema() throws Exception {
		ArrayList<String> ignoreColumnNameList = new ArrayList<>();
		ignoreColumnNameList.add("bypass_route_id");

		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_BUS_ROUTE_BYPASS_MAPPING.getName(), CommonConstant.IGNORE_COLUMN, ignoreColumnNameList);
		return commonSchemaList;
	}

	/**
	 * 서비스 요일정보 테이블 스키마
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectBusRouteCalendarSchema() throws Exception {
		ArrayList<String> ignoreColumnNameList = new ArrayList<>();
		ignoreColumnNameList.add("service_id");
		ignoreColumnNameList.add("start_date");
		ignoreColumnNameList.add("end_date");
		ignoreColumnNameList.add("comment");

		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_CALENDAR.getName(), CommonConstant.IGNORE_COLUMN, ignoreColumnNameList);
		return commonSchemaList;
	}


	/**
	 * 버스 노선 상세정보를 가져온다
	 * @param routeId - 노선ID
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusRouteInfo(int routeId) throws Exception {
		// 노선정보
		BusRouteVo busRouteVo = busRouteRepository.getBusRoute(routeId);

		if(Objects.isNull(busRouteVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

		BusRouteDetailOutputVo busRouteDetailoutputVo = new BusRouteDetailOutputVo();
		BeanUtils.copyProperties(busRouteVo, busRouteDetailoutputVo);

		// 그래프 정보
		GeoJsonOutputVo geoJsonOutputVo = this.getBusRouteGraphInfo(routeId);
		busRouteDetailoutputVo.setBusStopGraphInfo(geoJsonOutputVo);

		// 운수회사 정보
		List<BusRouteCompanyOutputVo> busRouteCompanyOutputVoList = busRouteRepository.selectBusRouteCompanyList(routeId);
		busRouteDetailoutputVo.setCompanyList(busRouteCompanyOutputVoList);

		// 우회노선 목록
		if(CommonConstant.N.equals(busRouteVo.getBypassYn())) {
			List<BusRouteBypassOutputVo> busRouteBypassOutputVoList = busRouteRepository.selectBusRouteBypassList(routeId);
			if(busRouteBypassOutputVoList.size() > 0) {
				busRouteDetailoutputVo.setBypassChildList(busRouteBypassOutputVoList);
			}
		}

		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectBusRouteSchemaAll();
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, busRouteDetailoutputVo);

		return commonResult;
	}

	/**
	 * 버스노선 작업정보를 가져온다
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusRouteTaskInfo(long taskId) throws Exception {

		// 버스노선 기본 작업정보
		BusRouteTaskVo busRouteTaskVo = busRouteRepository.getBusRouteTask(taskId);

		// 작업 기본정보
		TaskOutputVo taskOutputVo = taskRepository.getTaskInfo(taskId);

		if(Objects.isNull(busRouteTaskVo) || Objects.isNull(taskOutputVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

		BusRouteTaskDetailOutputVo busRouteTaskDetailOutputVo = new BusRouteTaskDetailOutputVo();
		BeanUtils.copyProperties(busRouteTaskVo, busRouteTaskDetailOutputVo);

		// BIS 자동 변경사항
		if(StringUtils.isNotEmpty(taskOutputVo.getBisAutoChangeData())) {
			BisBusRouteChangeVo BisBusRouteChangeVo = this.getBisBusRouteChangeInfo(taskOutputVo);
			taskOutputVo.setBisChangeDataInfo(BisBusRouteChangeVo);
		}

		// 그래프 정보 조회
		GeoJsonOutputVo geoJsonOutputVo = this.getBusRouteGraphTaskInfo(taskOutputVo);

		// 운수회사 정보 조회
		List<BusRouteCompanyOutputVo> busRouteCompanyOutputVoList = new ArrayList<>();
		int busRouteCompanyTaskTotalCnt = busRouteRepository.getBusRouteCompanyTaskListCnt(taskId);
		if(busRouteCompanyTaskTotalCnt > 0) {
			busRouteCompanyOutputVoList = busRouteRepository.selectBusRouteCompanyTaskList(taskId);
		}else {
			busRouteCompanyOutputVoList = busRouteRepository.selectBusRouteCompanyList(taskOutputVo.getPubTransId());
		}

		// 우회노선 목록
		if(CommonConstant.N.equals(busRouteTaskVo.getBypassYn())) {
			List<BusRouteBypassOutputVo> busRouteBypassOutputVoList = busRouteRepository.selectBusRouteBypassList(taskOutputVo.getPubTransId());
			if(busRouteBypassOutputVoList.size() > 0) {
				busRouteTaskDetailOutputVo.setBypassChildrenList(busRouteBypassOutputVoList);
			}
		}

		busRouteTaskDetailOutputVo.setTaskId(taskId);
		busRouteTaskDetailOutputVo.setBusStopGraphInfo(geoJsonOutputVo);
		busRouteTaskDetailOutputVo.setCompanyList(busRouteCompanyOutputVoList);
		busRouteTaskDetailOutputVo.setTaskInfo(taskOutputVo);

		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectBusRouteSchemaAll();
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, busRouteTaskDetailOutputVo);

		return commonResult;
	}


	/**
	 * 버스 노선의 그래프 정보를 가져온다
	 * @param routeId - 노선 ID
	 * @return
	 * @throws Exception
	 */
	public GeoJsonOutputVo getBusRouteGraphInfo(int routeId) throws Exception {

		List<BusStopGraphVo> busStopGraphVoList = busGraphRepository.selectBusRouteGraphList(routeId);
		List<GeoJsonFeatureVo> geoJsonFeatureVoList = busGraphService.makeGeoJsonFeatureList(busStopGraphVoList);

		// geojson 형태의 feature 목록 생성
		GeoJsonOutputVo geoJsonOutputVo = new GeoJsonOutputVo();
		geoJsonOutputVo.setFeatures(geoJsonFeatureVoList);

		return geoJsonOutputVo;
	}

	/**
	 * 버스노선 작업정보의 그래프 정보를 가져온다
	 * @param taskOutputVo - 작업 정보
	 * @return
	 * @throws Exception
	 */
	public GeoJsonOutputVo getBusRouteGraphTaskInfo(TaskOutputVo taskOutputVo) throws Exception {

		List<BusStopGraphVo> busStopGraphVoList = new ArrayList<>();
		int totalListCnt = busGraphRepository.geBusRouteGraphTaskListCnt(taskOutputVo.getTaskId());

		/**
		 * 그래프 작업 목록이 존재하면 작업목록에서 데이터를 가져온다.
		 * 작업목록이 없을경우 서비스 테이블의 그래프 정보를 가져온다.
		 */
		if(totalListCnt > 0) {
			busStopGraphVoList = busGraphRepository.selectBusRouteGraphTaskList(taskOutputVo.getTaskId());
		}else {
			busStopGraphVoList = busGraphRepository.selectBusRouteGraphList(taskOutputVo.getPubTransId());
		}

		// geojson 형태의 feature 목록 생성
		List<GeoJsonFeatureVo> geoJsonFeatureVoList = busGraphService.makeGeoJsonFeatureList(busStopGraphVoList);

		GeoJsonOutputVo geoJsonOutputVo = new GeoJsonOutputVo();
		geoJsonOutputVo.setFeatures(geoJsonFeatureVoList);

		return geoJsonOutputVo;
	}

	/**
	 * BIS 경유정류장 목록을 가지고 그래프 정보를 가져온다
	 * @param providerId - BIS 지역 ID
	 * @param bisBusStopVoList - BIS에서 변경된 노선 경유정류장 전체목록
	 * @return
	 * @throws Exception
	 */
	public BisBusRouteChangeGraphVo getBusRouteGraphInfoByBisAutoChangeVo(int providerId, List<BusStopVo> bisBusStopVoList) throws Exception {

		BisBusRouteChangeGraphVo bisBusRouteChangeVo = new BisBusRouteChangeGraphVo();

		List<String> busStopIds = new ArrayList<>();
		List<String> nonMatchLocalStopIds = new ArrayList<>();

		/**
		 * BIS에서 넘어온 경유정류장 목록을 가지고 그래프 정보를 생성하기 위한 정류장ID 목록을 만든다
		 * 만약 버스 정류장 매핑 정보에서 BIS 정류장 ID에 매핑되는 정류장 ID가 1개라도 없을경우 그래프를 생성하지 않는다.
		 */
		for(BusStopVo busStopVo : bisBusStopVoList) {
			Integer stopId = busStopVo.getStopId();

			if(stopId == 0) {
				BusStopMappingVo busStopMappingVo = new BusStopMappingVo();
				busStopMappingVo.setLocalStopId(busStopVo.getLocalStopId());
				busStopMappingVo.setProviderId(providerId);

				// BIS 버스 정류장에 매핑된 버스정류장 ID 조회
				stopId = busStopRepository.getBusStopIdMappedToBisStopId(busStopMappingVo);
				if(stopId == null) {
					nonMatchLocalStopIds.add(busStopVo.getLocalStopId());
				} else {
					busStopIds.add(String.valueOf(stopId));
				}
			} else {
				busStopIds.add(String.valueOf(stopId));
			}
		}

		if(nonMatchLocalStopIds.size() == 0) {
			CommonResult commonResult = this.getGraphInfoBetweenBusStops(busStopIds);
			bisBusRouteChangeVo.setGeoJsonOutputVo((GeoJsonOutputVo)commonResult.getData());
		}


		bisBusRouteChangeVo.setBusStopIds(busStopIds);
		bisBusRouteChangeVo.setNonMatchLocalStopIds(nonMatchLocalStopIds);

		return bisBusRouteChangeVo;
	}

	/**
	 * BIS 자동 변경사항 정보를 가져온다
	 * @param taskOutputVo - 작업 정보
	 * @return
	 * @throws Exception
	 */
	public BisBusRouteChangeVo getBisBusRouteChangeInfo(TaskOutputVo taskOutputVo) throws Exception {

		BisBusRouteChangeVo BisBusRouteChangeVo = new ObjectMapper().readValue(taskOutputVo.getBisAutoChangeData(), BisBusRouteChangeVo.class);

		// 버스 경유정류장 변경사항 확인 및 그래프 조회후 변경사항 목록에 담기
		if(!Objects.isNull(BisBusRouteChangeVo.getBisBusStopList()) && BisBusRouteChangeVo.getBisBusStopList().size() > 0) {
			BisBusRouteChangeGraphVo BisBusRouteChangeGraphVo = this.getBusRouteGraphInfoByBisAutoChangeVo(taskOutputVo.getProviderId(), BisBusRouteChangeVo.getBisBusStopList());

			if(Objects.isNull(BisBusRouteChangeGraphVo.getGeoJsonOutputVo())) {

				/**
				 *  정류장 미존재  메세지
				 *  2020-03-20 12:20:18_BIS 경유 정류장이 변경되었으나, 일부 버스 정류장이 버스 정류장 매핑 정보에 존재하지 않습니다. 정류장 매핑 정보에 존재하지 않는 BIS 정류장 ID-228487,28789
				 */
				String errorComment = Util.getToday(CommonConstant.DATE_TIME_FORMAT) + CommonConstant.UNDERSCORE
					+ CommonConstant.NONEXISTENT_BIS_BUS_STOP_IN_BUS_STOP_MAPPING + BisBusRouteChangeGraphVo.getNonMatchLocalStopIds();

				String taskComment = taskOutputVo.getTaskComment();
				taskComment += CommonConstant.NEWLINE + errorComment;
				taskOutputVo.setTaskComment(taskComment);
			}else {
				BisBusRouteChangeVo.setBusStopGraphInfo(BisBusRouteChangeGraphVo.getGeoJsonOutputVo());
			}

			// 해당 데이터는 경유 정류장 그래프를 생성하기 위한 정류장 정보이므로 사용자에게 출력되지 않도록 Null 처리 한다.
			BisBusRouteChangeVo.setBisBusStopList(null);
		}

		return BisBusRouteChangeVo;
	}

}
