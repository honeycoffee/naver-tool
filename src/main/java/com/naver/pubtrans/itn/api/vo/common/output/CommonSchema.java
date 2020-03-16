package com.naver.pubtrans.itn.api.vo.common.output;

import java.util.List;

import com.naver.pubtrans.itn.api.vo.common.FieldValue;

import lombok.Getter;
import lombok.Setter;

/**
 * 공통 result 하위 schema 항목 정의
 *
 * @author adtec10
 *
 */
@Getter
@Setter
public class CommonSchema {

	/**
	 * 필드 정보
	 */
	private String fieldLabel;

	/**
	 * 필드 명
	 */
	private String fieldName;

	/**
	 * 필드 타입
	 */
	private String fieldType;

	/**
	 * 필드 길이
	 */
	private String fieldLength;

	/**
	 * 필드 선택값 목록
	 */
	private List<FieldValue> fieldValues;

	/**
	 * Null 허용 여부(Y/N)
	 */
	private String nullable;

	/**
	 * Primary Key 여부 (Y/N)
	 */
	private String pkYn;

	/**
	 * 읽기 전용 여부
	 */
	private String readOnlyYn;


}
