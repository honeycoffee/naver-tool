package com.naver.pubtrans.itn.api.consts;

import lombok.Getter;

/**
 * Task 할당 구분
 * @author adtec10
 *
 */
@Getter
public enum TaskAssignType {

	REGISTER("reg", "등록"),
	WORK("work", "작업"),
	CHECK("check", "검수");

	// 코드
	private String code;

	// 설명
	private String descrition;

	TaskAssignType(String code, String description){
		this.code = code;
		this.descrition = description;
	}
}
