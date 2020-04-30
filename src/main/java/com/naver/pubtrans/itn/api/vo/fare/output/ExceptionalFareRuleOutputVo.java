package com.naver.pubtrans.itn.api.vo.fare.output;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.CommonConstant;

/**
 * 예외 노선 요금 리스트 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class ExceptionalFareRuleOutputVo {

	/**
	 * 요금 룰 ID
	 */
	private int fareId;

	/**
	 * 목록 순서
	 */
	@JsonIgnore
	private int order;

	/**
	 * 예외 요금 이름에 쓰일 노선 목록 (최대 5개)
	 */
	@JsonIgnore
	private String routeNames;

	/**
	 * 예외 요금 이름
	 */
	private String exceptionalFareRuleName;

	/**
	 * 예외 요금 이름 생성 
	 */
	public void setExceptionalFareRuleName() {
		StringBuilder stringBuilder = new StringBuilder(CommonConstant.EXCEPTION_TEXT);
		stringBuilder.append(CommonConstant.BLANK);
		stringBuilder.append(this.order);
		stringBuilder.append(CommonConstant.BRACKET_START);
		stringBuilder.append(this.routeNames);
		stringBuilder.append(CommonConstant.BRACKET_END);
		
		exceptionalFareRuleName = stringBuilder.toString();
	}
}
