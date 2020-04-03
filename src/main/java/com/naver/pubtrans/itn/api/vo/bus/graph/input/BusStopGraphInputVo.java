package com.naver.pubtrans.itn.api.vo.bus.graph.input;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 정류장 구간 그래프 속성 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopGraphInputVo {

	// 그래프 ID
	private int graphId;

	// 출발정류장 ID
	private int startStopId;

	// 도착정류장 ID
	private int endStopId;

}
