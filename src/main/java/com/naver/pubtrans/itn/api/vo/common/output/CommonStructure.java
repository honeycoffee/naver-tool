package com.naver.pubtrans.itn.api.vo.common.output;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonStructure {

	/**
	 * 필드 정보
	 */
	private String fieldLabel ;
	
	/**
	 * 필드 명
	 */
	private String fieldName ;
	
	/**
	 * 필드 타입
	 */
	private String fieldType ;
		
	/**
	 * 필드 길이
	 */
	private String fieldLength ;
	
	/**
	 * 필드 선택값 목록
	 */
	private List<FieldValues> filedValues ;
	
	/**
	 * Null 허용 여부(Y/N)
	 */
	private String nullAble ;
	
	/**
	 * Primary Key 여부 (Y/N)
	 */
	private String pkYn ;
	
	/**
	 * 읽기 전용 여부
	 */
	private String readOnlyYn ;
	
	
	@Getter
	@Setter
	public class FieldValues {
		
		/**
		 * 속성 값
		 */
		private String value ;
		
		/**
		 * 속성 명칭
		 */
		private String text ;
	}
	
}
