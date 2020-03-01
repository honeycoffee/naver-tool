package com.naver.pubtrans.itn.api.consts;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * 공통포맷 응답코드 및 메세지 정의
 * @author adtec10
 *
 */
@Getter
public enum ResultCode {

	OK(HttpStatus.OK, 200, "정상 처리되었습니다"),
	PARAMETER_ERROR(HttpStatus.BAD_REQUEST, 400, "잘못된 파라미터입니다"),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED, 403, "접근권한이 없습니다"),
	PAGE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, 404, "페이지를 찾을 수 없습니다"),
	INNER_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버오류"),
	AUTH_TOKEN_EMPTY(HttpStatus.OK, 2001, "Token 정보가 없습니다"),
	AUTH_TOKEN_EXPIRED(HttpStatus.OK, 2002, "만료된 Token 입니다"),
	AUTH_TOKEN_VALID_ERROR(HttpStatus.OK, 2003, "Token 검증 오류"),
	AUTH_TOKEN_DECODE_ERROR(HttpStatus.OK, 2004, "Token 복호화 오류");


	private HttpStatus httpStatus;
	private int apiErrorCode;
	private String displayMessage;


	ResultCode(HttpStatus httpStatus, int apiErrorCode, String displayMessage){
		this.httpStatus = httpStatus ;
		this.apiErrorCode = apiErrorCode ;
		this.displayMessage = displayMessage ;
	}
}
