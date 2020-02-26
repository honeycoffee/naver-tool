package com.naver.pubtrans.itn.api.vo.common.output;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

	/**
	 * API 호출 결과 메타 정보 - 페이지 정보
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private CommonMeta meta ;
	
	/**
	 * 입/출력 스키마
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<CommonStructure> structure ;
	
	/**
	 * API 호출 결과 데이터
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object data ;
	
	
	public CommonResult() {} ;
	
	public CommonResult(CommonMeta m) {
		this.meta = m ;
	}
	
	public CommonResult(Object d) {
		this.data = d ;
	}
	
	public CommonResult(CommonMeta m, Object d) {
		this.meta = m ;
		this.data = d ;
	}
	
}
