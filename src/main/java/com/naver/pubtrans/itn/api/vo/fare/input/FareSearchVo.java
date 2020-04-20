package com.naver.pubtrans.itn.api.vo.fare.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * 요금 룰 검색 조건
 * @author westwind
 *
 */
@Getter
@Setter
public class FareSearchVo {

	// 요금 ID
	private Integer fareId;

	// 지역코드
	@NotNull
	private Integer cityCode;

	// 버스 노선 종류
	@NotNull
	private Integer busClass;

	// 작업 ID
	private long taskId;

	// 연령별 타입
	private Integer ageId;

	// 지불 방식 식별
	private Integer paymentId;

	/**
	 * 연령별 타입 가져오기
	 * @return
	 */
	public Integer getAgeId() {
		if(ageId == null) {
			this.ageId = 1;
		}

		return ageId;
	}

	/**
	 * 지불 방식 식별 가져오기
	 * @return
	 */
	public Integer getPaymentId() {
		if(paymentId == null) {
			this.paymentId = 1;
		}

		return paymentId;
	}


}
