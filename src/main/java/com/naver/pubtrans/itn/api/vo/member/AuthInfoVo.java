package com.naver.pubtrans.itn.api.vo.member;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 권한 데이터 출력 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class AuthInfoVo{

	/**
	 * 권한 ID 
	 */
	private String authId;

	/**
	 * 권한 이름
	 */
	private String authName;

	/**
	 * 권한 설명
	 */
	private String authMemo;


}
