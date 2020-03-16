package com.naver.pubtrans.itn.api.consts;

import lombok.Getter;

/**
 * 테이블 컬럼 Key 정의
 * @author adtec10
 *
 */
@Getter
public enum ColumnKeyType {

	PK("PRI", "Primary Key"),
	FK("MUL", "Foreign Key");

	// 코드
	private String code;

	// 설명
	private String descrition;

	ColumnKeyType(String code, String description){
		this.code = code;
		this.descrition = description;
	}

}
