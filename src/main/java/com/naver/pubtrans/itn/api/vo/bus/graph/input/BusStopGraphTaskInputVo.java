package com.naver.pubtrans.itn.api.vo.bus.graph.input;

import lombok.Getter;
import lombok.Setter;

/**
 * 정류장 그래프 작업정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopGraphTaskInputVo {

	// 작업ID
	private long taskId;

	// 그래프 ID
	private Integer graphId;

	// 출발정류장 ID
	private int startStopId;

	// 도착정류장 ID
	private int endStopId;

	// 그래프 정보
	private String graphInfo;

	// 구간 거리
	private int distance;
}
