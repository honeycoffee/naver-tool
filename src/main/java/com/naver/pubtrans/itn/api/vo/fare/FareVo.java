package com.naver.pubtrans.itn.api.vo.fare;

import lombok.Getter;
import lombok.Setter;

/**
 * 요금 룰 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareVo {

	// 요금 ID
	private Integer fareId;

	// 기본요금(일반_카드기준)
	private Integer baseFare;

	// 기본요금(청소년_카드기준)
	private Integer youthFare;

	// 기본요금(어린이_카드기준)
	private Integer childFare;

	// 기본요금(일반_현금기준)
	private Integer baseCashFare;

	// 기본요금(청소년_현금기준)
	private Integer youthCashFare;

	// 기본요금(어린이_현금기준)
	private Integer childCashFare;

	// 기본거리
	private Integer baseDist;

	// 단위요금
	private Integer unitFare;

	// 단위거리
	private Integer unitDist;

	// 최대 요금
	private Integer maxFare;

	// 시작 정류장 
	private Integer startStopId;

	// 도착 정류장
	private Integer endStopId;

	// 평일 최대 배차간격(분)
	private Integer serviceId;

	// 지역코드
	private Integer cityCode;

	// 버스 노선 종류
	private Integer busClass;

	// 요금 룰의 기본 정보 여부
	private String baseYn;

	// 설명
	private String command;

}
