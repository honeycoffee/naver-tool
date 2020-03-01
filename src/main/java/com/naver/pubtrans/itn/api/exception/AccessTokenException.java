package com.naver.pubtrans.itn.api.exception;

/**
 * 엑세스 토큰 공통 예외
 * @author adtec10
 *
 */
public class AccessTokenException extends Exception {

	private static final long serialVersionUID = -8901712868024779493L;

	public AccessTokenException(String message) {
		super(message);
	}

}
