package com.naver.pubtrans.itn.api.vo.fare.output;

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
	 * 예외 요금 이름에 쓰일 노선 목록 (최대 5개)
	 */
	private String routes;

	/**
	 * 예외 요금 이름
	 */
	private String ignoredFareName;

	/**
	 * 예외 요금 이름 생성 
	 */
	public void setIgnoredFareName() {
		
		StringBuilder sb = new StringBuilder( CommonConstant.EXCEPTION_TEXT);
		sb.append(" ");
		sb.append(this.order);
		sb.append(CommonConstant.BRACKET_START);
		sb.append(this.routes);
		sb.append(CommonConstant.BRACKET_END);
		
		ignoredFareName = sb.toString();
	}
}
