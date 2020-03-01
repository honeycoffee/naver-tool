package com.naver.pubtrans.itn.api.vo.common.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.naver.pubtrans.itn.api.consts.ResultCode;

import lombok.Getter;
import lombok.Setter;

/**
 * 공통 전체 출력구조를 정의
 *
 * @author adtec10
 *
 */
@Getter
@Setter
public class CommonOutput {

	/**
	 * API 호출 응답코드
	 * 데이터 여부와 상관없이 예외가 발생하지 않을경우 200으로 고정
	 */
	private int code = ResultCode.OK.getApiErrorCode() ;

	/**
	 * API 호출 응답 메세지
	 * 필요시 메세지 세팅
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message = ResultCode.OK.getDisplayMessage();

	/**
	 * API 호출 결과 데이터 정보
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private CommonResult result ;


	public CommonOutput() {}

	public CommonOutput(CommonResult result) {
		this.result = result ;
	}

	public CommonOutput(int code, String msg) {
		this.code = code ;
		this.message = msg ;
	}

	public CommonOutput(int code, CommonResult result) {
		this.code = code ;
		this.result = result ;
	}

}
