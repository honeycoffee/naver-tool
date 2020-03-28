package com.naver.pubtrans.itn.api.vo.auth.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 인증 토큰 출력 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class AuthenticationOutputVo {

	/**
	 * JWT Refresh Token
	 */
	private String refreshToken;

	/**
	 * JWT Access Token
	 */
	private String accessToken;

}
