package com.naver.pubtrans.itn.api.vo.bus.graph.output;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusStopGraphOutputVo {

	// 그래프 ID
	private Integer graphId;

	// 출발정류장 ID
	private int startStopId;

	// 도착정류장 ID
	private int endStopId;

	// 출발정류장 명
	private String startStopName;

	// 도착정류장 명
	private String endStopName;

	// 일치하는 그래프 존재 여부 - 출/도착지 일치하는 그래프가 있는경우 Y
	private String matchGraphInfoYn;
}
