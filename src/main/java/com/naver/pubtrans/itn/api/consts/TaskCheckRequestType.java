package com.naver.pubtrans.itn.api.consts;

import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

import com.naver.pubtrans.itn.api.config.mybatis.StringCodeEnum;
import com.naver.pubtrans.itn.api.exception.ApiException;

/**
 * 작업 검수요청 구분
 * @author adtec10
 *
 */
@Getter
public enum TaskCheckRequestType implements StringCodeEnum {

	CHECK_REQUEST("check", "검수요청"),
	EXCEPTION_REQUEST("exception", "예외요청");

	// 코드
	private String code;

	// 설명
	private String description;

	TaskCheckRequestType(String code, String description){
		this.code = code;
		this.description = description;
	}

	/**
	 * 코드에 해당하는 Enum Value를 반환한다
	 * @param code - 코드
	 * @return
	 * @throws ApiException
	 */
	@JsonCreator
	public static TaskCheckRequestType getTaskCheckRequestType(String code) throws ApiException {
		return EnumSet.allOf(TaskCheckRequestType.class).stream()
			.filter(t -> t.getCode().equals(code))
			.findFirst()
			.orElseThrow(() -> new ApiException(ResultCode.PARAMETER_RULE_ERROR.getApiErrorCode(), ResultCode.PARAMETER_RULE_ERROR.getDisplayMessage()));

	}

	@JsonValue
    public String getCode() {
        return code;
    }
}
