package com.naver.pubtrans.itn.api.vo.bus.stop.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 정류장 목록 결과 Vo
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopListOutputVo {

	/**
	 * 정류장 ID
	 */
	private int stopId;

	/**
	 * 정류장명
	 */
	private String stopName;

	/**
	 * 경도
	 */
	private Double longitude;

	/**
	 * 위도
	 */
	private Double latitude;

	/**
	 * 도시코드
	 */
	private String cityCode;

	/**
	 * 도시코드 명칭
	 */
	private String cityName;
}
