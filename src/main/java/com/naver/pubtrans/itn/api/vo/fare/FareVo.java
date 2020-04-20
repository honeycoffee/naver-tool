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
	
	// 총 노선 수
	private Integer totalRouteCount;

	// 출처 명
	private String sourceName;

	// 출처 URL
	private String sourceUrl;

	// 설명
	private String comment;

	// 생성일
	private String createDate;
	
	// 일반 - 카드 요금 상세정보
	private FareInfoVo generalCardFare;

}
