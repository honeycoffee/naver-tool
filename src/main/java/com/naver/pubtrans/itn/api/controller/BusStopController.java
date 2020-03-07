package com.naver.pubtrans.itn.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naver.pubtrans.itn.api.service.BusStopService;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;


/**
 * 버스 정류장 관리 컨트롤러
 *
 * @author adtec10
 *
 */

@RestController
public class BusStopController {

	@Autowired
	BusStopService busStopService ;


	/**
	 * 버스정류장 목록 조회
	 * @param busStopSearchVo - 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/busStop")
	public CommonOutput listBusStop(BusStopSearchVo busStopSearchVo) throws Exception {
		CommonResult commonResult = busStopService.getBusStopList(busStopSearchVo) ;
		return new CommonOutput(commonResult) ;
	}



}
