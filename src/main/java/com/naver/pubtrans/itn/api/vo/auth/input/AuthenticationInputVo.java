package com.naver.pubtrans.itn.api.vo.auth.input;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 로그인 인증 입력 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class AuthenticationInputVo {

	/**
	 * 회원 ID
	 */
	private String userId;

	/**
	 * JWT Refresh Token
	 */
	private String refreshToken;

	/**
	 * 로그인 성공여부
	 */
	private String loginSuccessYn;

	/**
	 * 접근IP
	 */
	private String accessIp;

}
