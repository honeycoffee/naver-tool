package com.naver.pubtrans.itn.api.vo.bus.stop.output;

import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 경유노선을 포함하는 버스정류장 상세 출력정보
 *
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopOutputVoWithRoute extends BusStopVo {


	/**
	 * 정류장 경유노선 정보
	 */
	private BusRouteOutputVo busRouteInfo;

}
