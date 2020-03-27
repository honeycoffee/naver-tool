package com.naver.pubtrans.itn.api.vo.fare.input;

import lombok.Getter;
import lombok.Setter;

/**
 * 요금 룰 검색 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class FareSearchVo {

	// 요금 ID
	private Integer fareId;

	// 지역코드
	private Integer cityCode;

	// 버스 노선 종류
	private Integer busClass;

}
