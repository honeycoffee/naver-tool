package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskDataType;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.BusGraphRepository;
import com.naver.pubtrans.itn.api.repository.BusRouteRepository;
import com.naver.pubtrans.itn.api.vo.bus.graph.BusStopGraphVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.output.GeoJsonOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;

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


	@Autowired
	BusRouteService(OutputFmtUtil outputFmtUtil, CommonService commonService, BusGraphService busGraphService, TaskService taskService,
		BusRouteRepository busRouteRepository, BusGraphRepository busGraphRepository){

		this.outputFmtUtil = outputFmtUtil;
		this.commonService = commonService;
		this.busGraphService = busGraphService;
		this.taskService = taskService;
		this.busRouteRepository = busRouteRepository;
		this.busGraphRepository = busGraphRepository;
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
		commonSchemaList.addAll(tbRoutesSchemaList) ;
		commonSchemaList.addAll(tbBusRoutesInfoSchemaList) ;

		// 동일 컬럼에 대해 중복을 제거
		List<CommonSchema> distinctCommonSchemaVoList = commonSchemaList.stream()
																		.filter(Util.distinctByKey(o -> o.getFieldName()))
																		.collect(Collectors.toList());


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


}
