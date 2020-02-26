package com.naver.pubtrans.itn.api.consts;

public interface ResultCode {

	int OK = 200 ;
	
	int PARAMETER_ERROR = 400 ;
	String PARAMETER_ERROR_MSG = "잘못된 파라미터입니다" ;
	
	int ACCESS_DENIED = 403 ;
	String ACCESS_DENIED_MSG = "접근권한이 없습니다" ;
	
	int PAGE_NOT_FOUND_ERROR = 404 ;
	String PAGE_NOT_FOUND_ERROR_MSG = "페이지를 찾을 수 없습니다" ;
	
	int INNER_FAIL = 500 ;
	String INNER_FAIL_MSG = "서버오류" ;
	
	int AUTH_TOKEN_EMPTY = 2001 ;
	String AUTH_TOKEN_EMPTY_MSG = "Token 정보가 없습니다" ;
	
	int AUTH_TOKEN_EXPIRED = 2002 ;
	String AUTH_TOKEN_EXPIRED_MSG = "만료된 Token 입니다" ;
	
	int AUTH_TOKEN_VALID_ERROR = 2003 ;
	String AUTH_TOKEN_VALID_ERROR_MSG = "Token 검증 오류" ;
	
}
