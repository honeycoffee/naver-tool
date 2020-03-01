package com.naver.pubtrans.itn.api.exception;

/**
 * 엑세스 토큰 Not found 예외
 * @author adtec10
 *
 */
public class AccessTokenNotFoundException extends Exception {

	private static final long serialVersionUID = -8901712868024779493L;

	public AccessTokenNotFoundException(String message) {
		super(message);
	}

}
