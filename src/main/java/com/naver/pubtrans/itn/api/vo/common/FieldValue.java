package com.naver.pubtrans.itn.api.vo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 데이터 스키마 Enum 상세 항목 정의
 * @author adtec10
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class FieldValue {

	/**
	 * 속성 값
	 */
	private String value;

	/**
	 * 속성 명칭
	 */
	private String text;
}