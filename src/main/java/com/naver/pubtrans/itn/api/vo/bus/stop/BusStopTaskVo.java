package com.naver.pubtrans.itn.api.vo.bus.stop;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 정류장 작업정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopTaskVo {

	/**
	 * 작업 ID
	 */
	private long taskId;

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
	private int cityCode;

	/**
	 * 도시코드 명칭
	 */
	private String cityName;


	/**
	 * 정류장 유형
	 */
	private Integer transportId;

	/**
	 * 정류장 위치 구분
	 */
	private Integer level;

	/**
	 * 미정차 정류장 여부
	 */
	private String nonstopYn;



	/**
	 * 가상정류장 여부
	 */
	private String virtualStopYn;

	/**
	 * 중앙차로 정류장 여부
	 */
	private String centerStopYn;

	/**
	 * 광역시, 도명
	 */
	private String sido;

	/**
	 * 구
	 */
	private String gu;

	/**
	 * 동
	 */
	private String dong;

	/**
	 * 도로명 주소
	 */
	private String roadAddress;

	/**
	 * 도로명
	 */
	private String roadName;

	/**
	 * 본번
	 */
	private String bonbun;

	/**
	 * 부번
	 */
	private String boobun;



	/**
	 * 지자체 정류장 ID
	 */
	private String localStopId;

	/**
	 * 지자체 BIS ID
	 */
	private Integer providerId;

	/**
	 * 정류장에 표기된 ID
	 */
	private String displayId;


}
