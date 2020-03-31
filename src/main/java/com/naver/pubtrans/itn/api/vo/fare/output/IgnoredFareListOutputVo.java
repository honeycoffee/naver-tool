package com.naver.pubtrans.itn.api.vo.fare.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 예외 노선 요금 리스트 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class IgnoredFareListOutputVo {

	/**
	 * 요금 룰 ID
	 */
	private int fareId;

	/**
	 * 목록 순서
	 */
	private int order;

	/**
	 * 예외 요금 이름
	 */
	private int ignoredFareName;
}
