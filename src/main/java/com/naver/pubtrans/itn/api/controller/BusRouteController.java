package com.naver.pubtrans.itn.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.service.BusRouteService;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteSearchVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;

/**
 * 버스 노선관리 컨트롤러
 * @author adtec10
 *
 */
@RestController
public class BusRouteController {

	private final BusRouteService busRouteService;
	private final OutputFmtUtil outputFmtUtil;

	@Autowired
	BusRouteController(BusRouteService busRouteService, OutputFmtUtil outputFmtUtil){
		this.busRouteService = busRouteService;
		this.outputFmtUtil = outputFmtUtil;
	}

	/**
	 * 버스노선 목록 조회
	 * @param busRouteSearchVo - 버스노선 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/busRoute")
	public CommonOutput listBusRoute(BusRouteSearchVo busRouteSearchVo) throws Exception {
		CommonResult commonResult = busRouteService.getBusRouteList(busRouteSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 특정 버스 노선의 작업 히스토리 요약정보를 가져온다
	 * @param busRouteId - 정류장 ID
	 * @param searchVo - 페이징 정보
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/busRouteTask/{busRouteId}")
	public CommonOutput listBusRouteTaskSummary(@PathVariable int busRouteId, SearchVo searchVo) throws Exception {
		CommonResult commonResult = busRouteService.getBusRouteTaskSummaryList(busRouteId, searchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 버스정류장 사이의 그래프 정보를 가져온다
	 * @param busStopIds - 정류장 정보
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/busStopsGraph")
	public CommonOutput listGraphInfoBetweenBusStops(@RequestParam(required = true) List<String> busStopIds) throws Exception {
		CommonResult commonResult = busRouteService.getGraphInfoBetweenBusStops(busStopIds);
		return new CommonOutput(commonResult);
	}

	/**
	 * 버스노선 상세 스키마 정보를 가져온다
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/schema/busRoute")
	public CommonOutput getBusStopSchema() throws Exception {
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(busRouteService.selectBusRouteSchemaAll());
		return new CommonOutput(commonResult);
	}

	/**
	 * 버스노선 상세정보를 조회한다
	 * @param routeId - 노선ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/busRoute/{routeId}")
	public CommonOutput infoBusRoute(@PathVariable int routeId) throws Exception {
		CommonResult commonResult = busRouteService.getBusRouteInfo(routeId);
		return new CommonOutput(commonResult);
	}

	/**
	 * 버스노선 상세 작업정보를 조회한다
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/busRouteTask/{taskId}")
	public CommonOutput infoBusRouteTask(@PathVariable long taskId) throws Exception {
		CommonResult commonResult = busRouteService.getBusRouteTaskInfo(taskId);
		return new CommonOutput(commonResult);
	}
}
