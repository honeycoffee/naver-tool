package com.naver.pubtrans.itn.api.vo.bus.graph.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 버스정류장간의 그래프 검색 정보
 * @author adtec10
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class BusStopGraphSearchVo {

	// 출발정류장 ID
	private int startStopId;

	// 도착정류장 ID
	private int endStopId;
}
