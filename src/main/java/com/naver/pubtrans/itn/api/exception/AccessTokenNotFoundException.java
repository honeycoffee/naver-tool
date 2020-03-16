package com.naver.pubtrans.itn.api.exception;

import lombok.Getter;

/**
 * 엑세스 토큰 Not found 예외
 * @author adtec10
 *
 */
@Getter
public class AccessTokenNotFoundException extends Exception {

	private int errorCode;

	private static final long serialVersionUID = -8901712868024779493L;

	public AccessTokenNotFoundException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

}
