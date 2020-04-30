package com.naver.pubtrans.itn.api.consts;

import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

import com.naver.pubtrans.itn.api.config.mybatis.StringCodeEnum;
import com.naver.pubtrans.itn.api.exception.ApiException;

/**
 * Task 할당 구분
 * @author adtec10
 *
 */
@Getter
public enum TaskAssignType implements StringCodeEnum {

	REGISTER("reg", "등록"),
	WORK("work", "작업"),
	CHECK("check", "검수");

	// 코드
	private String code;

	// 설명
	private String description;

	TaskAssignType(String code, String description){
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
	public static TaskAssignType getTaskAssignType(String code) throws ApiException {
		return EnumSet.allOf(TaskAssignType.class).stream()
			.filter(t -> t.getCode().equals(code))
			.findFirst()
			.orElseThrow(() -> new ApiException(ResultCode.PARAMETER_RULE_ERROR.getApiErrorCode(), ResultCode.PARAMETER_RULE_ERROR.getDisplayMessage()));

	}

	@JsonValue
    public String getCode() {
        return code;
    }

}
