package com.naver.pubtrans.itn.api.exception;

import lombok.Getter;

/**
 * API Custom 예외
 * @author adtec10
 *
 */
@Getter
public class ApiException extends Exception {

	private int errorCode ;

	private static final long serialVersionUID = 5873400351395084126L;

	public ApiException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode ;
	}

}
