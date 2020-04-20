package com.naver.pubtrans.itn.api.vo.fare;

import lombok.Getter;
import lombok.Setter;

/**
 * 요금 룰 상세 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareInfoVo {

	// 요금 ID
	private Integer fareId;

	// 기본요금(일반_카드기준)
	private Integer baseFare;

	// 기본거리
	private Integer baseDist;

	// 단위요금
	private Integer unitFare;

	// 단위거리
	private Integer unitDist;

	// 최대 요금
	private Integer maxFare;

	// 요금 룰의 기본 정보 여부
	private String baseYn;

	// 연령별 타입
	private Integer ageId;

	// 지불 방식 식별
	private Integer paymentId;

}
