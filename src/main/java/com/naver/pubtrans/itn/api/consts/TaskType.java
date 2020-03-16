package com.naver.pubtrans.itn.api.consts;

import lombok.Getter;

/**
 * Task 작업 구분
 * @author adtec10
 *
 */
@Getter
public enum TaskType {

	REGISTER("register", "신규등록"),
	MODIFY("modify", "수정"),
	REMOVE("remove", "삭제");

	// 코드
	private String code;

	// 설명
	private String descrition;

	TaskType(String code, String description){
		this.code = code;
		this.descrition = description;
	}
}
