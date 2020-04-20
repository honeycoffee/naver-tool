package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import lombok.extern.slf4j.Slf4j;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.BusDirection;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransId;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskAssignType;
import com.naver.pubtrans.itn.api.consts.TaskDataType;
import com.naver.pubtrans.itn.api.consts.TaskStatus;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.BusGraphRepository;
import com.naver.pubtrans.itn.api.repository.BusRouteRepository;
import com.naver.pubtrans.itn.api.repository.BusStopRepository;
import com.naver.pubtrans.itn.api.repository.TaskRepository;
import com.naver.pubtrans.itn.api.vo.bus.graph.BusStopGraphVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureGeometryVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphInputVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.GeoJsonFeatureInputVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.GeoJsonInputVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.output.GeoJsonOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BisBusRouteChangeGraphVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BisBusRouteChangeVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteStopVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteTaskVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteCompanyTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteStopTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteBypassOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteCompanyOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteDetailOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteTaskDetailOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopMappingVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusRouteOutputVo;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.task.input.TaskInputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 버스 노선관리 서비스
 * @author adtec10
 *
 */
@Slf4j
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
		ignoreColumnNameList.add("transport_id");

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
				busRouteTaskDetailOutputVo.setBypassChildList(busRouteBypassOutputVoList);
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

		// 마지막 구간 정보는 출발 정류장과 도착정류장이 동일한 버스노선 종점 정류장 데이터 이므로 제외한다
		busStopGraphVoList.remove(busStopGraphVoList.size()-1);
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

		// 마지막 구간 정보는 출발 정류장과 도착정류장이 동일한 버스노선 종점 정류장 데이터 이므로 제외한다
		busStopGraphVoList.remove(busStopGraphVoList.size()-1);

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

	/**
	 * 버스노선 생성/수정/삭제를 위한 Task를 등록한다
	 * @param taskType - 작업 등록구분(컨텐츠 추가, 수정, 삭제)
	 * @param busRouteTaskInputVo - 노선 작업정보
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public CommonResult registerBusRouteTask(String taskType, BusRouteTaskInputVo busRouteTaskInputVo) throws Exception {

		// 작업등록 유형이 '신규등록' 이나 "수정'인 경우 노선별 경유정류장 그래프 유효성 검사를 수행한다
		// 작업등록 유형이 '노선삭제' 인 경우에는 노선별 경유장 그래프 유효성 검사를 수행하지 않는다.
		if(taskType.equals(TaskType.REGISTER.getCode()) || taskType.equals(TaskType.MODIFY.getCode())) {
			boolean result = this.verifyBusRouteGraphInfo(busRouteTaskInputVo.getBusStopGraphInfo());
			 if (result == false) {
		        throw new ApiException(ResultCode.BUS_ROUTE_STOPS_NOT_VALID.getApiErrorCode(), ResultCode.BUS_ROUTE_STOPS_NOT_VALID.getDisplayMessage());
		    }
		}


		// 노선 신규등록인 경우
		if(taskType.equals(TaskType.REGISTER.getCode())) {
			/**
			 * 노선 임시 ID 설정
			 * Task 검수 완료 후 배포시 노선 테이블의 auto increment값을 사용하여 ID를 부여한다.
			 */
			busRouteTaskInputVo.setRouteId(0);
		}

		// 스케줄 ID 정보 가져오기
		Integer calendarServiceId = busRouteRepository.getCalendarServiceId(busRouteTaskInputVo);
		if(calendarServiceId == null) {
			throw new ApiException(ResultCode.BUS_SCHEDULE_NOT_MATCH.getApiErrorCode(), ResultCode.BUS_SCHEDULE_NOT_MATCH.getDisplayMessage());
		}

		busRouteTaskInputVo.setServiceId(calendarServiceId);


		// Task 기본정보
		TaskInputVo taskInputVo = this.createTaskInputInfo(taskType, TaskStatus.PROGRESS.getCode(), busRouteTaskInputVo);


		/**
		 * Task를 구성하는 전체 정보를 저장한다
		 *
		 * 1. Task 기본정보 저장
		 * 2. Task 할당정보 저장
		 * 3. Task 상태변경 히스토리 정보 저장
		 */
		long taskId = taskService.registerTaskInfoAll(taskInputVo);
		busRouteTaskInputVo.setTaskId(taskId);

		// 노선 기본정보 저장
		busRouteRepository.insertBusRouteTask(busRouteTaskInputVo);

		// 노선 부가정보 저장
		busRouteRepository.insertBusRouteSubTask(busRouteTaskInputVo);

		// 운수회사 정보 저장
		List<BusRouteCompanyTaskInputVo> busRouteCompanyTaskInputVoList = busRouteTaskInputVo.getCompanyList();
		if(busRouteCompanyTaskInputVoList != null && busRouteCompanyTaskInputVoList.size() > 0) {
			for(BusRouteCompanyTaskInputVo busRouteCompanyTaskInputVo : busRouteCompanyTaskInputVoList) {
				busRouteCompanyTaskInputVo.setTaskId(taskId);
				busRouteRepository.insertBusRouteCompanyTask(busRouteCompanyTaskInputVo);
			};
		}


		// 노선 매핑정보 저장 (매핑정보가 있는 경우)
		if(StringUtils.isNotEmpty(busRouteTaskInputVo.getLocalRouteId())) {
			busRouteRepository.insertBusRouteMappingTask(busRouteTaskInputVo);
		}


		// 우회노선 정보 저장 (우회노선인 경우)
		if(CommonConstant.Y.equals(busRouteTaskInputVo.getBypassYn())) {

			// 우회노선 유효성 검사
			if(!this.verifyBusRouteBypass(busRouteTaskInputVo)) {
				throw new ApiException(ResultCode.BUS_ROUTE_BYPASS_PARAMETER_ERROR.getApiErrorCode(), ResultCode.BUS_ROUTE_BYPASS_PARAMETER_ERROR.getDisplayMessage());
			}

			busRouteRepository.insertBusRouteBypassTask(busRouteTaskInputVo);
		}


		if(taskType.equals(TaskType.REGISTER.getCode()) || taskType.equals(TaskType.MODIFY.getCode())) {

			/**
			 * 요금정보
			 * 신규 노선 등록이거나, 도시코드 또는 노선코드가 변경되었을경우에는 기본 요금ID를 검색하여 해당 ID를 저장한다
			 */

			// 요금ID 변경여부
			boolean isBusRouteFareChange = false;
			if(taskType.equals(TaskType.MODIFY.getCode())) {
			    // 노선정보
			    BusRouteVo busRouteVo = busRouteRepository.getBusRoute(busRouteTaskInputVo.getRouteId());

			    // 도시코드나 버스 클래스가 변경되면 요금ID도 변경되어야 한다
			    if(busRouteVo.getCityCode() != busRouteTaskInputVo.getCityCode() || busRouteVo.getBusClass() != busRouteTaskInputVo.getBusClass()) {
			        isBusRouteFareChange = true;
			    }
			}

			if(taskType.equals(TaskType.REGISTER.getCode()) || isBusRouteFareChange) {
				Integer fareId = busRouteRepository.getBaseFareId(busRouteTaskInputVo);
				if(fareId == null) {
					throw new ApiException(ResultCode.BUS_FARE_NOT_MATCH.getApiErrorCode(), ResultCode.BUS_FARE_NOT_MATCH.getDisplayMessage());
				}

				// 요금정보 매핑테이블 Task 저장
				busRouteTaskInputVo.setFareId(fareId);

				busRouteRepository.insertBusRouteFareTask(busRouteTaskInputVo);
			}

			// 노선 경유정류장과 그래프 정보를 저장한다
			this.registerBusRouteStopAndBusStopGraphTask(taskType, busRouteTaskInputVo);


		}

		if(taskType.equals(TaskType.REMOVE.getCode())) {

			// 현재 서비스중인 경유정류장 목록을 작업정보로 저장한다
			List<BusStopGraphVo> busStopGraphVoList = busGraphRepository.selectBusRouteGraphList(busRouteTaskInputVo.getRouteId());
			for(BusStopGraphVo busStopGraphVo : busStopGraphVoList) {
				BusRouteStopTaskInputVo busRouteStopTaskInputVo = new BusRouteStopTaskInputVo();
				busRouteStopTaskInputVo.setTaskId(taskId);
				busRouteStopTaskInputVo.setRouteId(busRouteTaskInputVo.getRouteId());
				busRouteStopTaskInputVo.setStopSequence(busStopGraphVo.getStopSequence());
				busRouteStopTaskInputVo.setStopId(busStopGraphVo.getStartStopId());
				busRouteStopTaskInputVo.setNextStopId(busStopGraphVo.getEndStopId());
				busRouteStopTaskInputVo.setUpDown(busStopGraphVo.getUpDown());
				busRouteStopTaskInputVo.setCumulativeDistance(busStopGraphVo.getCumulativeDistance());
				busRouteStopTaskInputVo.setGraphId(busStopGraphVo.getGraphId());

				busRouteRepository.insertBusRouteStopTask(busRouteStopTaskInputVo);
			};

			// 현재 서비스중인 요금정보를 작업정보를 저장한다
			List<Integer> busRouteFareIdList = busRouteRepository.selectBusRouteFareIdList(busRouteTaskInputVo.getRouteId());
			for(Integer fareId : busRouteFareIdList) {
				BusRouteTaskInputVo busRouteFareTaskInputVo = new BusRouteTaskInputVo();
				busRouteFareTaskInputVo.setTaskId(taskId);
				busRouteFareTaskInputVo.setRouteId(busRouteTaskInputVo.getRouteId());
				busRouteFareTaskInputVo.setFareId(fareId);

				busRouteRepository.insertBusRouteFareTask(busRouteFareTaskInputVo);
			}

		}

		// 성공시 작업ID 리턴
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, taskId);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);
		return commonResult;
	}


	/**
	 * 노선 경유 정류장의 입력 정보에 대해 유효성 검사를 진행한다
	 * @param geoJsonInputVo - 경유정류장 입력정보
	 * @return
	 * @throws Exception
	 */
	public boolean verifyBusRouteGraphInfo(GeoJsonInputVo geoJsonInputVo) throws Exception {

		List<GeoJsonFeatureInputVo> geoJsonFeatureInputVoList = geoJsonInputVo.getFeatures();

		// 경유 정류장 정보가 없는경우
		if(geoJsonFeatureInputVoList == null || geoJsonFeatureInputVoList.size() == 0) {
			return false;
		}


		// 경유 정류장 및 그래프 목록 검증
		for(int i=0; i < geoJsonFeatureInputVoList.size(); i++) {
			GeoJsonFeatureInputVo geoJsonFeatureInputVo = geoJsonFeatureInputVoList.get(i);
			if(geoJsonFeatureInputVo == null) {
				   return false;
			}

			GeoJsonFeatureGeometryVo geoJsonFeatureGeometryVo = geoJsonFeatureInputVo.getGeometry();
			BusStopGraphInputVo busStopGraphInputVo = geoJsonFeatureInputVo.getProperties();
			if(busStopGraphInputVo == null) {
			  return false;
			}

			// 출/도착 정류장
			int startStopId = busStopGraphInputVo.getStartStopId();
			int endStopId = busStopGraphInputVo.getEndStopId();

			// 출/도착 구간 좌표
			List<List<Double>> coordinates = geoJsonFeatureGeometryVo.getCoordinates();
			List<Double> startPoint = coordinates.get(0);

			// 출발 정류장 또는 도착정류장 ID가 없는 경우. 또는 출/도착 정류장ID가 동일한 경우
			if(startStopId <= 0 || endStopId <= 0 || startStopId == endStopId) {
				return false;
			}


			if(i > 0) {
				// 이전 정류장
				int prevEndStopId = geoJsonFeatureInputVoList.get(i-1).getProperties().getEndStopId();

				// 이전 구간 그래프 좌표 정보
				List<List<Double>> prevCoordinates = geoJsonFeatureInputVoList.get(i-1).getGeometry().getCoordinates();

				// 이전구간 마지막 좌표 지점
				List<Double> prevEndPoint = prevCoordinates.get(prevCoordinates.size()-1);

				// 이전 구간 도착 정류장과 현재 구간 출발 정류장이 다른 경우
				if(prevEndStopId != startStopId) {
					return false;
				}

				// 이전 구간 마지막 좌표와 현재 구간 출발 시작 좌표가 다른경우
				if(!prevEndPoint.containsAll(startPoint)) {
					return false;
				}

			}

		}

		return true;
	}


	/**
	 * 사용자 입력 그래프정보와 DB에 저장된 그래프 정보를 비교하여 추가/수정되는 그래프 정보를 반환한다
	 * @param busStopGraphVoList - 버스노선 작업 경유정륲장 및 그래프 정보
	 * @return
	 * @throws Exception
	 */
	public List<BusStopGraphVo> extractChangedBusStopGraphVoList(List<BusStopGraphVo> busStopGraphVoList) throws Exception {

		/**
		 * 사용자 입력 정류장 정보를 가지고 DB에 저장된 그래프 정보를 가져온다
		 */
		List<BusStopGraphSearchVo> busStopGraphSearchVoList = busStopGraphVoList.stream().map(o -> {
			int startBusStopId = o.getStartStopId();
			int endBusStopId = o.getEndStopId();
			return new BusStopGraphSearchVo(startBusStopId, endBusStopId);
		}).collect(Collectors.toList());


		// 정류장 검색 목록으로 그래프 정보 목록을 가져온다
		List<BusStopGraphVo> busStopGraphVoListOfDb = busGraphRepository.selectBusStopGraphList(busStopGraphSearchVoList);

		/**
		 * 사용자 입력 그래프 정보와 DB에 저장된 그래프 정보를 비교한다.
		 * 그래프가 수정되거나 없는 그래프 정보인 경우 해당 목록을 List에 담는다
		 */
		List<BusStopGraphVo> newBusStopGraphVoList = new ArrayList<>();

		busStopGraphVoList.stream().forEach(t -> {
			BusStopGraphVo newBusStopGraphVo = new BusStopGraphVo();

			boolean isMatchedGraph = busStopGraphVoListOfDb.stream().anyMatch(d -> t.getStartStopId() == d.getStartStopId() && t.getEndStopId() == d.getEndStopId());

			// 기존 그래프 수정
			busStopGraphVoListOfDb.stream().filter(d -> t.getStartStopId() == d.getStartStopId() && t.getEndStopId() == d.getEndStopId() && !StringUtils.equals(t.getGraphInfo(), d.getGraphInfo())).forEach(o -> {
				newBusStopGraphVo.setGraphId(o.getGraphId());
			});


			/**
			 * 그래프가 일치하는 경우가 없는경우 그래프를 추가/수정 하기 위한 정보를 설정한다.
			 */
			if(!isMatchedGraph || !Objects.isNull(newBusStopGraphVo.getGraphId())) {

				// 신규 그래프 생성
				if(Objects.isNull(newBusStopGraphVo.getGraphId())) {
					newBusStopGraphVo.setGraphId(0);
				}

				newBusStopGraphVo.setStartStopId(t.getStartStopId());
				newBusStopGraphVo.setEndStopId(t.getEndStopId());
				newBusStopGraphVo.setGraphInfo(t.getGraphInfo());
				newBusStopGraphVo.setDistance(t.getDistance());

				newBusStopGraphVoList.add(newBusStopGraphVo);
			}

		});

		return newBusStopGraphVoList;
	}

	/**
	 * 정류장 전체 목록을 생성한다
	 * @param routeId - 노선ID
	 * @param busStopGraphVoList - 버스노선 작업 경유정류장 및 그래프 정보
	 * @return
	 * @throws Exception
	 */
	public List<BusRouteStopVo> buildBusRouteStopVoList(int routeId, List<BusStopGraphVo> busStopGraphVoList) throws Exception {
		// 사용자 입력 경유정류장 목록
		List<BusRouteStopVo> busRouteStopVoListOfTask = busStopGraphVoList.stream().map(o -> {
			BusRouteStopVo busRouteStopVo = this.makeBusRouteStopVo(o);
			busRouteStopVo.setRouteId(routeId);
			return busRouteStopVo;
		}).collect(Collectors.toList());

		return busRouteStopVoListOfTask;
	}



	/**
	 * 사용자 입력 노선 정류장 목록 정보와 DB에 저장되어 있는 노선 정류장 목록이 동일한지 체크한다
	 * @param taskType - 작업 등록구분(컨텐츠 추가, 수정)
	 * @param routeId - 노선ID
	 * @param busStopGraphVoList - 버스노선 작업 경유정류장 및 그래프 정보
	 * @return
	 * @throws Exception
	 */
	public boolean isTheSameAsBusRouteStopVoListOfDb(String taskType, int routeId, List<BusStopGraphVo> busStopGraphVoList) throws Exception {

		// 사용자 입력 경유정류장 목록
		List<BusRouteStopVo> busRouteStopVoListOfTask = this.buildBusRouteStopVoList(routeId, busStopGraphVoList);

		// 노선 수정
		if(taskType.equals(TaskType.MODIFY.getCode())) {

			// 경유정류장 및 그래프 원본
			List<BusStopGraphVo> busStopGraphVoListOfDb = busGraphRepository.selectBusRouteGraphList(routeId);

			// 원본 경유정류장 목록
			List<BusRouteStopVo> busRouteStopVoListOfDb = busStopGraphVoListOfDb.stream().map(o -> {
				BusRouteStopVo busRouteStopVo = this.makeBusRouteStopVo(o);
				busRouteStopVo.setRouteId(routeId);
				return busRouteStopVo;
			}).collect(Collectors.toList());



			/**
			 * 사용자 입력 정류장 목록 정보와 DB에 저장되어 있는 정류장 목록 정보를 비교한다.
			 *
			 */
			List<Integer> matchedRouteStopList = new ArrayList<>();
			int matchedRouteStopCnt = 0;

			if(busRouteStopVoListOfTask.size() == busRouteStopVoListOfDb.size()) {

				AtomicInteger indexOfTask = new AtomicInteger();
				AtomicInteger indexOfDb = new AtomicInteger();
				busRouteStopVoListOfTask.stream().forEach(o -> {
					busRouteStopVoListOfDb.stream().filter(t -> indexOfTask.getAndIncrement() == indexOfDb.getAndIncrement()
						&& t.getGraphId() == o.getGraphId() && t.getRouteId() == o.getRouteId()
						&& t.getStopId() == o.getStopId() && t.getNextStopId() == o.getNextStopId()
						&& t.getStopSequence() == o.getStopSequence() && StringUtils.equals(t.getUpDown(), o.getUpDown())).forEach(t->{
							matchedRouteStopList.add(1);
						});

				});

				matchedRouteStopCnt = matchedRouteStopList.stream().mapToInt(Integer::intValue).sum();

			}

			// 경유 정류장 변경사항이 없으면 true
			if(busRouteStopVoListOfTask.size() == matchedRouteStopCnt) {
				return true;
			}


		}

		return false;

	}

	/**
	 * 버스 정류장간의 그래프 정보에서 경유 정류장 정보만 가져온다
	 * @param busStopGraphVo - 버스 정류장 그래프 정보
	 * @return
	 * @throws Exception
	 */
	public BusRouteStopVo makeBusRouteStopVo(BusStopGraphVo busStopGraphVo) {

		if(busStopGraphVo.getGraphId() == null) {
			busStopGraphVo.setGraphId(0);
		}

		BusRouteStopVo busRouteStopVo = new BusRouteStopVo();
		busRouteStopVo.setStopSequence(busStopGraphVo.getStopSequence());
		busRouteStopVo.setStopId(busStopGraphVo.getStartStopId());
		busRouteStopVo.setNextStopId(busStopGraphVo.getEndStopId());
		busRouteStopVo.setUpDown(busStopGraphVo.getUpDown());
		busRouteStopVo.setCumulativeDistance(busStopGraphVo.getCumulativeDistance());
		busRouteStopVo.setGraphId(busStopGraphVo.getGraphId());

		return busRouteStopVo;
	}

	/**
	 * 사용자 버스노선 작업 정보에서 DB에서 사용될 경유정류장 및 그래프 정보를 생성한다
	 * @param busRouteTaskInputVo - 버스노선 작업 정보
	 * @return
	 * @throws Exception
	 */
	public List<BusStopGraphVo> makeBusStopGraphVoList(BusRouteTaskInputVo busRouteTaskInputVo) throws Exception {

		// 작업정보에서 경유정류장 정보를 테이블 포맷에 맞게 가져온다
		List<BusStopGraphVo> busStopGraphVoList = this.selectBusStopGraphListByTaskInput(busRouteTaskInputVo);

		// 노선 종점데이터를 삽입한다
		busStopGraphVoList = this.insertLastBusStopGraphInfo(busStopGraphVoList);

		return busStopGraphVoList;
	}


	/**
	 * 작업 등록정보를 생성한다
	 * @param taskType - 작업 종류
	 * @param taskStatus - 작업 상태
	 * @param busRouteTaskInputVo - 버스노선 작업정보
	 * @return
	 * @throws Exception
	 */
	private TaskInputVo createTaskInputInfo(String taskType, String taskStatus, BusRouteTaskInputVo busRouteTaskInputVo) throws Exception {
		// Task 기본정보
		TaskInputVo taskInputVo = new TaskInputVo();
		taskInputVo.setTaskType(taskType);
		taskInputVo.setPubTransId(busRouteTaskInputVo.getRouteId());

		taskInputVo.setProviderId(busRouteTaskInputVo.getProviderId());
		taskInputVo.setTaskStatus(taskStatus);
		taskInputVo.setTaskDataType(TaskDataType.ROUTE.getCode());
		taskInputVo.setTaskDataName(busRouteTaskInputVo.getRouteName());
		taskInputVo.setTaskComment(busRouteTaskInputVo.getTaskComment());
		taskInputVo.setTaskRegisterType(CommonConstant.MANUAL);
		taskInputVo.setCheckUserId(busRouteTaskInputVo.getCheckUserId());

		/*
		 * 등록자, 작업자 정보
		 * 작업 등록시  등록자,작업자는 본인 자신이다.
		 */
		taskService.addTaskMemberInfo(TaskAssignType.REGISTER.getCode(), taskInputVo);
		taskService.addTaskMemberInfo(TaskAssignType.WORK.getCode(), taskInputVo);

		// 검수자 정보
		taskService.addTaskMemberInfo(TaskAssignType.CHECK.getCode(), taskInputVo);


		return taskInputVo;
	}


	/**
	 * 노선 작업정보에서 경유정류장 데이터를 DB 테이블 포맷에 맞게 생성하여 가져온다
	 * @param busRouteTaskInputVo
	 * @return
	 * @throws Exception
	 */
	private List<BusStopGraphVo> selectBusStopGraphListByTaskInput(BusRouteTaskInputVo busRouteTaskInputVo) throws Exception {

		// 입력받은 경유정류장 데이터를 테이블 포맷에 맞게 생성한다
		GeoJsonInputVo geoJsonInputVo = busRouteTaskInputVo.getBusStopGraphInfo();
		if(geoJsonInputVo == null) {
		    throw new ApiException(ResultCode.BUS_ROUTE_STOPS_NOT_VALID.getApiErrorCode(), ResultCode.BUS_ROUTE_STOPS_NOT_VALID.getDisplayMessage());
		}

		List<GeoJsonFeatureInputVo> geojsonFeatureInputVoList = geoJsonInputVo.getFeatures();
		if(geojsonFeatureInputVoList == null) {
		    throw new ApiException(ResultCode.BUS_ROUTE_STOPS_NOT_VALID.getApiErrorCode(), ResultCode.BUS_ROUTE_STOPS_NOT_VALID.getDisplayMessage());
		}



		// 누적거리
		List<Integer> cumulativeDistanceList = new ArrayList<>();

		//
		AtomicInteger stopSequence = new AtomicInteger();

		List<BusStopGraphVo> busStopGraphVoList = geojsonFeatureInputVoList.stream().map(o -> {

			BusStopGraphInputVo busStopGraphInputVo = o.getProperties();
			BusStopGraphVo busStopGraphVo = new BusStopGraphVo();

			try {
				busStopGraphVo = busGraphService.getDistanceAndLineStringFromGeoJsonFeature(o);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			busStopGraphVo.setGraphId(busStopGraphInputVo.getGraphId());
			busStopGraphVo.setStartStopId(busStopGraphInputVo.getStartStopId());
			busStopGraphVo.setEndStopId(busStopGraphInputVo.getEndStopId());
			busStopGraphVo.setStopSequence(stopSequence.getAndIncrement()+1);
			busStopGraphVo.setCumulativeDistance(cumulativeDistanceList.stream().mapToInt(Integer::intValue).sum());


			// 회차 정류장 정보가 있을경우 상/하행 표기를 한다. 없을경우에는 하행으로 표기
			if(busRouteTaskInputVo.getTurningPointSequence() > 0) {
				// 회차점보다 작을경우 하행(D)으로 표기
				if(busStopGraphVo.getStopSequence() < busRouteTaskInputVo.getTurningPointSequence()) {
					busStopGraphVo.setUpDown(BusDirection.DOWN.getCode());
				}else {
					busStopGraphVo.setUpDown(BusDirection.UP.getCode());
				}
			}else {
				busStopGraphVo.setUpDown(BusDirection.DOWN.getCode());
			}

			// 누적거리
			cumulativeDistanceList.add(busStopGraphVo.getDistance());
			return busStopGraphVo;
		}).collect(Collectors.toList());


		return busStopGraphVoList;
	}


	/**
	 * 버스 정류장 종점 데이터를 삽입한다
	 * @param busStopGraphVoList - 버스 경유정류장 그래프 정보
	 * @return
	 * @throws Exception
	 */
	private List<BusStopGraphVo> insertLastBusStopGraphInfo(List<BusStopGraphVo> busStopGraphVoList) throws Exception {

		int cumulativeDistance = busStopGraphVoList.stream().mapToInt(i -> i.getDistance()).sum();

		List<Double> lastStopCoordinate = busGraphService.selectLastCoordinates(busStopGraphVoList.get(busStopGraphVoList.size()-1).getGraphInfo());

		double lastStopLongitude = lastStopCoordinate.get(0);		// 마지막 정류장 경도
		double lastStopLatitude = lastStopCoordinate.get(1);		// 마지막 정류장 위도

		/**
		 * 버스정류장 종점 데이터를 삽입한다 - 출/도착 정류장 ID가 동일
		 * 출/도착 정류장이 동일한 그래프가 존재하는지 확인하고 없을경우 출/도착 좌표가 동일한 신규 그래프 정보를 생성한다
		 */
		BusStopGraphVo lastBusStopGraphVo = busStopGraphVoList.get(busStopGraphVoList.size()-1);
		BusStopGraphVo newBusStopGraphVo = new BusStopGraphVo();
		newBusStopGraphVo.setStartStopId(lastBusStopGraphVo.getEndStopId());
		newBusStopGraphVo.setEndStopId(lastBusStopGraphVo.getEndStopId());
		newBusStopGraphVo.setStopSequence(lastBusStopGraphVo.getStopSequence()+1);
		newBusStopGraphVo.setCumulativeDistance(cumulativeDistance);
		newBusStopGraphVo.setUpDown(lastBusStopGraphVo.getUpDown());


		// 버스 종점 데이터 구간 그래프 정보가 DB에 존재하는 지  확인한다
		List<BusStopGraphSearchVo> busStopGraphSearchVoList = new ArrayList<>();
		busStopGraphSearchVoList.add(new BusStopGraphSearchVo(newBusStopGraphVo.getStartStopId(), newBusStopGraphVo.getEndStopId()));
		List<BusStopGraphVo> resultBusStopGraphVoList = busGraphRepository.selectBusStopGraphList(busStopGraphSearchVoList);

		if(resultBusStopGraphVoList == null) {
			throw new ApiException(ResultCode.INNER_FAIL.getApiErrorCode(), ResultCode.INNER_FAIL.getDisplayMessage());
		}

		if(resultBusStopGraphVoList.size() > 0) {
			newBusStopGraphVo.setGraphId(resultBusStopGraphVoList.get(0).getGraphId());
			newBusStopGraphVo.setGraphInfo(resultBusStopGraphVoList.get(0).getGraphInfo());
		}else {
			// 좌표 정보. 두 지점의 좌표는 종점 정류장 좌표로 지정한다
			List<Double> startCoordinate = Arrays.asList(lastStopLongitude, lastStopLatitude);
			List<Double> endCoordinate = Arrays.asList(lastStopLongitude, lastStopLatitude);
			List<List<Double>> coordinates = Arrays.asList(startCoordinate, endCoordinate);

			newBusStopGraphVo.setGraphInfo(busGraphService.convertLineString(coordinates));
		}

		busStopGraphVoList.add(newBusStopGraphVo);

		return busStopGraphVoList;
	}

	/**
	 * 버스노선을 삭제하는 작업정보를 등록한다
	 * @param busRouteRemoveTaskInputVo - 노선 삭제 입력정보
	 * @throws Exception
	 */
	public CommonResult registerBusRouteRemoveTask(BusRouteRemoveTaskInputVo busRouteRemoveTaskInputVo) throws Exception {

		int routeId = busRouteRemoveTaskInputVo.getRouteId();

		// 노선ID 체크
		if(routeId < PubTransId.ROUTE_MIN.getId() || routeId > PubTransId.ROUTE_MAX.getId()) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}


		// 기본정보 가져오기
		BusRouteVo busRouteVo = busRouteRepository.getBusRoute(routeId);

		if(Objects.isNull(busRouteVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

		BusRouteTaskInputVo busRouteTaskInputVo = new BusRouteTaskInputVo();
		BeanUtils.copyProperties(busRouteVo, busRouteTaskInputVo);

		busRouteTaskInputVo.setTaskComment(busRouteRemoveTaskInputVo.getTaskComment());
		busRouteTaskInputVo.setCheckUserId(busRouteRemoveTaskInputVo.getCheckUserId());

		// 운수회사 정보 가져오기
		List<BusRouteCompanyOutputVo> busRouteCompanyOutputVo = busRouteRepository.selectBusRouteCompanyList(routeId);
		List<BusRouteCompanyTaskInputVo> busRouteCompanyTaskInputVoList = busRouteCompanyOutputVo.stream().map(o -> {
			BusRouteCompanyTaskInputVo busRouteCompanyTaskInputVo = new BusRouteCompanyTaskInputVo();
			busRouteCompanyTaskInputVo.setRouteId(routeId);
			busRouteCompanyTaskInputVo.setCompanyId(o.getCompanyId());
			return busRouteCompanyTaskInputVo;
		}).collect(Collectors.toList());

		busRouteTaskInputVo.setCompanyList(busRouteCompanyTaskInputVoList);;

		// 노선 삭제요청 Task 등록
		CommonResult commonResult = this.registerBusRouteTask(TaskType.REMOVE.getCode(), busRouteTaskInputVo);
		return commonResult;
	}


	/**
	 * 버스노선 작업정보를 수정한다
	 * @param busRouteTaskInputVo - 노선 작업정보
	 * @throws Exception
	 */
	@Transactional
	public void modifyBusRouteTask(BusRouteTaskInputVo busRouteTaskInputVo) throws Exception {

		long taskId = busRouteTaskInputVo.getTaskId();

		// Task 정보
		TaskOutputVo taskOutputVo = taskService.getTaskInfo(taskId);

		TaskInputVo taskInputVo = this.createTaskInputInfo(taskOutputVo.getTaskType(), taskOutputVo.getTaskStatus(), busRouteTaskInputVo);
		taskInputVo.setTaskId(taskId);

		/**
		 * Task를 구성하는 전체 정보를 수정한다
		 *
		 * 1. Task 기본정보 수정
		 * 2. Task 할당정보 저장
		 */
		int updateTaskInfoCnt = taskService.modifyTaskInfoAll(taskInputVo);


		// 스케줄 ID 정보 가져오기
		Integer calendarServiceId = busRouteRepository.getCalendarServiceId(busRouteTaskInputVo);
		if(calendarServiceId == null) {
			throw new ApiException(ResultCode.BUS_SCHEDULE_NOT_MATCH.getApiErrorCode(), ResultCode.BUS_SCHEDULE_NOT_MATCH.getDisplayMessage());
		}

		busRouteTaskInputVo.setServiceId(calendarServiceId);

		// 정류장 연관 Task 데이터 업데이트
		int updateBusRouteTaskCnt = busRouteRepository.updateBusRouteTask(busRouteTaskInputVo);
		int updateBusRouteSubTaskCnt = busRouteRepository.updateBusRouteSubTask(busRouteTaskInputVo);


		/**
		 * 운수회사 정보 업데이트
		 * 노선별 운수회사 Task 전체 삭제후 추가
		 */
		busRouteRepository.deleteBusRouteCompanyTask(taskId);

		List<BusRouteCompanyTaskInputVo> busRouteCompanyTaskInputVoList = busRouteTaskInputVo.getCompanyList();
		if(busRouteCompanyTaskInputVoList != null && busRouteCompanyTaskInputVoList.size() > 0) {
			for(BusRouteCompanyTaskInputVo busRouteCompanyTaskInputVo : busRouteCompanyTaskInputVoList) {
				busRouteCompanyTaskInputVo.setTaskId(taskId);
				busRouteRepository.insertBusRouteCompanyTask(busRouteCompanyTaskInputVo);
			}
		}

		/**
		 * BIS 매핑정보 업데이트
		 *  - BIS 매핑정보 Task 삭제후 추가
		 */
		busRouteRepository.deleteBusRouteMappingTask(taskId);
		if(StringUtils.isNotEmpty(busRouteTaskInputVo.getLocalRouteId())) {
			busRouteRepository.insertBusRouteMappingTask(busRouteTaskInputVo);
		}

		/**
		 * 우회노선 정보 업데이트
		 *  - 우회노선 정보 Task 삭제후 추가
		 */
		busRouteRepository.deleteBusRouteBypassTask(taskId);
		if(CommonConstant.Y.equals(busRouteTaskInputVo.getBypassYn())) {

			// 우회노선 유효성 검사
			if(!this.verifyBusRouteBypass(busRouteTaskInputVo)) {
				throw new ApiException(ResultCode.BUS_ROUTE_BYPASS_PARAMETER_ERROR.getApiErrorCode(), ResultCode.BUS_ROUTE_BYPASS_PARAMETER_ERROR.getDisplayMessage());
			}

			busRouteRepository.insertBusRouteBypassTask(busRouteTaskInputVo);
		}

		/**
		 * 버스노선 기본요금 업데이트
		 *  - 도시코드 또는 버스타입이 변경되었을경우 기존 Task 정보 삭제후 신규 정보를 추가한다
		 */
		// 버스노선 기본 작업정보
		BusRouteTaskVo busRouteTaskVo = busRouteRepository.getBusRouteTask(taskId);
		if(busRouteTaskVo.getCityCode() != busRouteTaskInputVo.getCityCode() || busRouteTaskVo.getBusClass() != busRouteTaskInputVo.getBusClass()) {
			Integer fareId = busRouteRepository.getBaseFareId(busRouteTaskInputVo);
			if(fareId == null) {
				throw new ApiException(ResultCode.BUS_FARE_NOT_MATCH.getApiErrorCode(), ResultCode.BUS_FARE_NOT_MATCH.getDisplayMessage());
			}

			// 요금정보 매핑테이블 Task 저장
			busRouteTaskInputVo.setFareId(fareId);

			// 삭제
			busRouteRepository.deleteBusRouteFareTask(taskId);


			// TaskId, fareId 체크
			if(busRouteTaskInputVo.getTaskId() <= 0 || (busRouteTaskInputVo.getFareId() == null || busRouteTaskInputVo.getFareId() <= 0)) {
				throw new ApiException(ResultCode.SAVE_FAIL.getApiErrorCode(), ResultCode.SAVE_FAIL.getDisplayMessage());
			}

			// 노선 수정인경우 routeId 체크
			if(TaskType.MODIFY.getCode().equals(taskOutputVo.getTaskType())) {
				Integer tempRouteId = busRouteTaskInputVo.getRouteId();

				if (tempRouteId == null || tempRouteId < PubTransId.ROUTE_MIN.getId() || tempRouteId > PubTransId.ROUTE_MAX.getId()) {
					throw new ApiException(ResultCode.SAVE_FAIL.getApiErrorCode(), ResultCode.SAVE_FAIL.getDisplayMessage());
				}
			}


			// 추가
			busRouteRepository.insertBusRouteFareTask(busRouteTaskInputVo);
		}

		/**
		 * 경유정류장 정보 업데이트
		 *  - 경유정류장 작업정보가 있는경우 Task 전체 삭제 후 추가한다
		 *
		 */
		busRouteRepository.deleteBusRouteStopTask(taskId);
		busGraphRepository.deleteBusStopGraphTask(taskId);

		// 노선 경유정류장과 그래프 정보를 저장한다
		this.registerBusRouteStopAndBusStopGraphTask(taskOutputVo.getTaskType(), busRouteTaskInputVo);


		// 저장 오류 처리
		if(!(updateTaskInfoCnt == 1 && updateBusRouteTaskCnt == 1 && updateBusRouteSubTaskCnt == 1)) {
			throw new ApiException(ResultCode.SAVE_FAIL.getApiErrorCode(), ResultCode.SAVE_FAIL.getDisplayMessage());
		}

	}

	/**
	 * 노선 정류장과 정류장 그래프 작업정보를 저장한다
	 * @param taskType - 작업 등록구분
	 * @param busRouteTaskInputVo - 노선 작업정보
	 * @throws Exception
	 */
	public void registerBusRouteStopAndBusStopGraphTask(String taskType, BusRouteTaskInputVo busRouteTaskInputVo) throws Exception {

		// 사용자 입력 경유정류장 및 그래프 정보 가져오기
		List<BusStopGraphVo> busStopGraphVoList = this.makeBusStopGraphVoList(busRouteTaskInputVo);


		/**
		 * 노선 경유정류장 정보가 변경되었을경우 전체 경유정류장 목록을 작업정보로 저장한다
		 */
		boolean isSame = false;
		List<BusRouteStopVo> busRouteStopVoList = null;

		// 원본 노선별 경유정류장 목록과 사용자 입력 경유 정류장이 동일한지 확인한다
		if (taskType.equals(TaskType.MODIFY.getCode())) {
		   isSame = isTheSameAsBusRouteStopVoListOfDb(taskType, busRouteTaskInputVo.getRouteId(), busStopGraphVoList);
		} else {
		  isSame = false;
		}

		if (isSame == false) {
		    busRouteStopVoList = buildBusRouteStopVoList(busRouteTaskInputVo.getRouteId(),  busStopGraphVoList);
		}

		if(!Objects.isNull(busRouteStopVoList) && busRouteStopVoList.size() > 0) {
			for(BusRouteStopVo busRouteStopVo : busRouteStopVoList) {
				BusRouteStopTaskInputVo busRouteStopTaskInputVo = new BusRouteStopTaskInputVo();
				BeanUtils.copyProperties(busRouteStopVo, busRouteStopTaskInputVo);
				busRouteStopTaskInputVo.setTaskId(busRouteTaskInputVo.getTaskId());

				busRouteRepository.insertBusRouteStopTask(busRouteStopTaskInputVo);
			};
		}


		/**
		 * 그래프 정보가 변경되었을 경우에만 변경된 구간에 대하여 그래프 작업정보를 저장한다
		 */
		List<BusStopGraphVo> busStopGraphVoChangeList = this.extractChangedBusStopGraphVoList(busStopGraphVoList);
		if(!Objects.isNull(busStopGraphVoChangeList) && busStopGraphVoChangeList.size() > 0) {
			for(BusStopGraphVo busStopGraphVo : busStopGraphVoChangeList) {
				BusStopGraphTaskInputVo busStopGraphTaskInputVo = new BusStopGraphTaskInputVo();
				BeanUtils.copyProperties(busStopGraphVo, busStopGraphTaskInputVo);
				busStopGraphTaskInputVo.setTaskId(busRouteTaskInputVo.getTaskId());

				busGraphRepository.insertBusStopGraphTask(busStopGraphTaskInputVo);
			};

		}
	}


	/**
	 * 우회노선 입력값의 유효성 검사를 진행한다
	 * @param busRouteTaskInputVo - 노선 작업정보
	 * @return
	 * @throws Exception
	 */
	public boolean verifyBusRouteBypass(BusRouteTaskInputVo busRouteTaskInputVo) throws Exception {

		// 본노선 유효성 확인
		Integer parentRouteId = busRouteTaskInputVo.getParentRouteId();
		if (parentRouteId == null || parentRouteId < PubTransId.ROUTE_MIN.getId() || parentRouteId > PubTransId.ROUTE_MAX.getId()) {
			return false;
		}

		// 본 노선과 우회노선의 노선ID가 동일하면 오류
		if(parentRouteId == busRouteTaskInputVo.getRouteId()) {
			return false;
		}


		// 우회 시작일 종료일 유효성 검사
		if(StringUtils.isEmpty(busRouteTaskInputVo.getBypassStartDateTime()) || StringUtils.isEmpty(busRouteTaskInputVo.getBypassEndDateTime())) {
			return false;
		}

		// 사용자 입력은 '분' 단위까지만 입력받으므로 '초'단위를 임의로 삽입한다
		// 시작일에는 00초를 붙인다
		busRouteTaskInputVo.setBypassStartDateTime(busRouteTaskInputVo.getBypassStartDateTime() + CommonConstant.FIRST_SECONDS);

		// 종료일에는 59초를 붙인다
		busRouteTaskInputVo.setBypassEndDateTime(busRouteTaskInputVo.getBypassEndDateTime() + CommonConstant.LAST_SECONDS);


		// 입력받은 날짜형식이 포맷과 다른경우 확인
		if(!Util.isDatePattern(CommonConstant.INPUT_DATE_TIME_PATTERN, busRouteTaskInputVo.getBypassStartDateTime())
				|| !Util.isDatePattern(CommonConstant.INPUT_DATE_TIME_PATTERN, busRouteTaskInputVo.getBypassEndDateTime())) {

			return false;
		}


		return true;
	}
}
