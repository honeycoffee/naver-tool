package com.naver.pubtrans.itn.api.consts;

import lombok.Getter;

/**
 * Task 진행상태
 * @author adtec10
 *
 */
@Getter
public enum TaskStatus {

	WAIT("00", "대기"),
	PROGRESS("01", "진행"),
	COMPLETE("02", "완료"),
	IGNORE("03", "제외");

	// 코드
	private String code;

	// 설명
	private String descrition;

	TaskStatus(String code, String description){
		this.code = code;
		this.descrition = description;
	}
}
