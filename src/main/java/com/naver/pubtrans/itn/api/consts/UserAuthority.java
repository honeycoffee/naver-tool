package com.naver.pubtrans.itn.api.consts;

import lombok.Getter;

@Getter
public enum UserAuthority {

	ROLE_UNAUTHORIZED("가입 승인 대기자"),
	ROLE_USER("일반 사용자 권한"),
	ROLE_ADMIN("관리자 권한");

	// 설명
	private String description;

	UserAuthority(String description) {
		this.description = description;
	}
}