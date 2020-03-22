package com.naver.pubtrans.itn.api.vo.bus.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 정류장간의 그래프 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopGraphVo {

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

	// 그래프 정보 - DB 저장 형태
	@JsonIgnore
	private String graphInfo;

	// 그래프 정보 - GeoJson 형태
	@JsonIgnore
	private String graphInfoToGeoJson;

}
