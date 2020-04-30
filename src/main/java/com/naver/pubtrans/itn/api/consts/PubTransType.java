package com.naver.pubtrans.itn.api.consts;

import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.config.mybatis.StringCodeEnum;
import com.naver.pubtrans.itn.api.exception.ApiException;

/**
 * 대중교통 구분
 * @author adtec10
 *
 */
@Getter
public enum PubTransType implements StringCodeEnum {

	ROUTE("route", "노선정보"),
	STOP("stop", "정류장 정보"),
	COMPANY("company", "운수사"),
	FARE_RULE("fare_rule", "요금 룰");

	// 코드
	private String code;

	// 설명
	private String description;

	PubTransType(String code, String description){
		this.code = code;
		this.description = description;
	}

	public static Stream<PubTransType> stream() {
		return Stream.of(PubTransType.values());
	}

	/**
	 * 코드에 해당하는 Enum Value를 반환한다
	 * @param code - 코드
	 * @return
	 * @throws ApiException
	 */
	@JsonCreator
	public static PubTransType getPubTransType(String code) throws ApiException {
		return EnumSet.allOf(PubTransType.class).stream()
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
		stringBuilder.append(EnumSet.allOf(PubTransType.class).stream()
			.map(t -> Util.getCodeDescriptionWithColon(t.code, t.description)).collect(Collectors.joining(", ")));
		stringBuilder.append(CommonConstant.BRACKET_END);
		return stringBuilder.toString();
	}

	@JsonValue
    public String getCode() {
        return code;
    }

}
