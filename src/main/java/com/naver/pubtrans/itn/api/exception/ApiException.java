package com.naver.pubtrans.itn.api.exception;

/**
 * API Custom 예외
 * @author adtec10
 *
 */
public class ApiException extends Exception {

	private static final long serialVersionUID = 5873400351395084126L;

	public ApiException(String message) {
		super(message) ;
	}

}
