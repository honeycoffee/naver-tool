package com.naver.pubtrans.itn.api.vo.bus.route;

import lombok.Getter;
import lombok.Setter;

/**
 * 노선 경유정류장 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteStopVo {

	// 노선ID
	private int routeId;

	// 정류장 순서
	private int stopSequence;

	// 정류장 iD
	private int stopId;

	// 다음정류장 ID
	private int nextStopId;

	// 상,하행 구분
	private String upDown;

	// 누적거리
	private int cumulativeDistance;

	// 그래프 ID
	private int graphId;
}
