package com.naver.pubtrans.itn.api.vo.fare;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * 요금 룰 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareRuleVo {

	// 요금 ID
	private Integer fareId;

	// 시작 정류장 ID
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Integer startStopId;

	// 도착 정류장 ID
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Integer endStopId;

	// 시작 정류장 명
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String startStopName;

	// 도착 정류장 명
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String endStopName;

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

	// 예외 요금 룰 이름
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String exceptionalFareRuleName;

	// 예외 노선 이름들 (최대 5개)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String routeNames;

	// 생성일
	private String createDate;

	// 수정일
	private String updateDate;
	
	// 일반 - 카드 요금 상세정보
	private FareRuleInfoVo generalCardFareRuleInfo;

}
