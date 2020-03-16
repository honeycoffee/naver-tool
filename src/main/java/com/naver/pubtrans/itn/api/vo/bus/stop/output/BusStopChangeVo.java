package com.naver.pubtrans.itn.api.vo.bus.stop.output;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * BIS 버스정류장 변경정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopChangeVo {

	/**
	 * 정류장명
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String stopName;

	/**
	 * 경도
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Double longitude;

	/**
	 * 위도
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Double latitude;

	/**
	 * 도시코드
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String cityCode;


	/**
	 * 정류장 유형
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer transportId;

	/**
	 * 정류장 위치 구분
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer level;

	/**
	 * 미정차 정류장 여부
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String nonstopYn;



	/**
	 * 가상정류장 여부
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String virtualStopYn;

	/**
	 * 중앙차로 정류장 여부
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String centerStopYn;



	/**
	 * 지자체 정류장 ID
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String localStopId;

	/**
	 * 지자체 BIS ID
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer providerId;

	/**
	 * 정류장에 표기된 ID
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String displayId;


}
