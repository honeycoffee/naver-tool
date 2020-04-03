package com.naver.pubtrans.itn.api.vo.bus.route.input;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteStopVo;

/**
 * 노선 경유정류장 작업 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteStopTaskInputVo extends BusRouteStopVo {

	// 작업 ID
	private long taskId;


}
