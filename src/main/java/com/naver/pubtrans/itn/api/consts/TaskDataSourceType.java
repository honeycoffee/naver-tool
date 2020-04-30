package com.naver.pubtrans.itn.api.consts;

import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

import com.naver.pubtrans.itn.api.config.mybatis.IntegerCodeEnum;
import com.naver.pubtrans.itn.api.exception.ApiException;

/**
 * 작업 데이터 출처 구분
 * @author adtec10
 *
 */
@Getter
public enum TaskDataSourceType implements IntegerCodeEnum {

	BIS(1, "BIS", "지자체 버스 정보 시스템"),
	MEF(2, "MEF", "지도 수정요청 어드민"),
	SFO(3, "SFO", "서비스광장"),
	VCA(4, "VCA", "고객문의 분석시스템"),
	OSS(5, "OSS", "네이버 oss"),
	UM(6, "모니터링", "사용자 모니터링"),
	ETC(7, "기타",  "기타");

	// 코드
	private int code;

	// 코드명
	private String codeName;

	// 코드설명
	private String description;

	TaskDataSourceType(int code, String codeName, String description){
		this.code = code;
		this.codeName = codeName;
		this.description = description;
	}

	/**
	 * 코드에 해당하는 Enum Value를 반환한다
	 * @param code - 코드
	 * @return
	 * @throws ApiException
	 */
	@JsonCreator
	public static TaskDataSourceType getTaskDataSourceType(int code) throws ApiException {
		return EnumSet.allOf(TaskDataSourceType.class).stream()
			.filter(t -> t.getCode() == code)
			.findFirst()
			.orElseThrow(() -> new ApiException(ResultCode.PARAMETER_RULE_ERROR.getApiErrorCode(), ResultCode.PARAMETER_RULE_ERROR.getDisplayMessage()));
	}

	@JsonValue
    public int getCode() {
        return code;
    }
}
