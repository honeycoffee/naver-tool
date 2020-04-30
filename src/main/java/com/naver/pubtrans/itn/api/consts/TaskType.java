package com.naver.pubtrans.itn.api.consts;

import java.util.EnumSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.config.mybatis.StringCodeEnum;
import com.naver.pubtrans.itn.api.exception.ApiException;


/**
 * Task 작업 구분
 * @author adtec10
 *
 */
@Getter
public enum TaskType implements StringCodeEnum {

	REGISTER("register", "신규등록"),
	MODIFY("modify", "수정"),
	REMOVE("remove", "삭제");

	// 코드
	private String code;

	// 설명
	private String description;

	TaskType(String code, String description){
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
	public static TaskType getTaskType(String code) throws ApiException {
		return EnumSet.allOf(TaskType.class).stream()
			.filter(t -> t.getCode().equals(code))
			.findFirst()
			.orElseThrow(() -> new ApiException(ResultCode.PARAMETER_RULE_ERROR.getApiErrorCode(), ResultCode.PARAMETER_RULE_ERROR.getDisplayMessage()));
	}

	/**
	 * Spring Docs 설명에 쓰이는 Enum 코드 및 설명을 콜론과 함께 문자열로 반환한다
	 * @return
	 * @throws Exception
	 */
	public static String getCodeAndDescriptionWithColon() throws Exception{
		StringBuilder stringBuilder = new StringBuilder(CommonConstant.BRACKET_START);
		stringBuilder.append(EnumSet.allOf(TaskType.class).stream()
			.map(t -> Util.getCodeDescriptionWithColon(t.code, t.description)).collect(Collectors.joining(", ")));
		stringBuilder.append(CommonConstant.BRACKET_END);
		return stringBuilder.toString();
	}

	@JsonValue
    public String getCode() {
        return code;
    }
}
