package com.naver.pubtrans.itn.api.vo.fare.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * 요금 정책 등록/수정을 위한 작업 입력정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareTaskInputVo {

	/**
	 * 기본요금(일반_카드기준)
	 */
	@Size(max=11)
	private Integer baseFare;

	/**
	 * 기본요금(청소년_카드기준)
	 */
	@Size(max=11)
	private Integer youthFare;

	/**
	 * 기본요금(어린이_카드기준)
	 */
	@Size(max=11)
	private Integer childFare;

	/**
	 * 기본요금(일반_현금기준)
	 */
	@Size(max=11)
	private Integer baseCashFare;

	/**
	 * 기본요금(청소년_현금기준)
	 */
	@Size(max=11)
	private Integer youthCashFare;

	/**
	 * 기본요금(어린이_현금기준)
	 */
	@Size(max=11)
	private Integer childCashFare;

	/**
	 * 기본거리
	 */
	@Size(max=11)
	private Integer baseDist;

	/**
	 * 단위요금
	 */
	@Size(max=11)
	private Integer unitFare;

	/**
	 * 단위거리
	 */
	@Size(max=11)
	private Integer unitDist;

	/**
	 * 최대 요금
	 */
	@Size(max=11)
	private Integer maxFare;

	/**
	 * 시작 정류장
	 */
	private Integer[] startStopId;

	/**
	 * 도착 정류장
	 */
	private Integer[] endStopId;

	/**
	 * 운행 요일 ID
	 */
	private Integer serviceId;

	/**
	 * 지역코드
	 */
	@NotNull
	private Integer cityCode;

	/**
	 * 요금 룰의 기본 정보 여부
	 */
	@NotNull
	private String baseYn;

	/**
	 * 버스 노선 종류
	 */
	@NotNull
	private Integer busClass;

	/**
	 * 설명
	 */
	private String command;

	/**
	 * 버스노선 ID
	 */
	private Integer routeId;

}
