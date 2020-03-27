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
	BINDING_NUMBER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 501, "반드시 값이 존재하고 길이 혹은 크기가 0보다 커야 합니다"),
	SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 502, "데이터 저장에 실패하였습니다"),
	NOT_MATCH(HttpStatus.INTERNAL_SERVER_ERROR, 503, "일치하는 정보가 없습니다"),
	STOP_REMOVE_EXISTS_BUS_ROUTE(HttpStatus.INTERNAL_SERVER_ERROR, 504, "경유노선이 존재하여 해당 정류장을 삭제할 수 없습니다"),
	TASK_STATUS_NOT_MATCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 505, "작업 정보 수정은 대기/진행 상태만 가능합니다"),
	MEMBER_DATA_NULL(HttpStatus.INTERNAL_SERVER_ERROR, 506, "회원정보가 존재하지 않습니다"),
	PASSWORD_NOT_MATCH(HttpStatus.INTERNAL_SERVER_ERROR, 507, "비밀번호가 일치하지 않습니다"),
	MEMBER_TOKEN_NOT_MATCH(HttpStatus.INTERNAL_SERVER_ERROR, 508, "accessToken과 내 정보가 일치하지 않습니다"),
	DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 509, "데이터 삭제에 실패하였습니다"),
	UPDATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 510, "데이터 갱신에 실패하였습니다"),
	PARAMETER_RULE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 511, "파라미터 구문이 잘못되었습니다"),
	PASSWORD_NOT_VALID(HttpStatus.INTERNAL_SERVER_ERROR, 512, "비밀번호는 반드시 값이 존재하고 길이 혹은 크기가 8보다 크고 20보다 작아야 합니다"),
	MEMBER_AUTHORITY_NULL(HttpStatus.INTERNAL_SERVER_ERROR, 513, "회원 권한이 없습니다"),
	AUTH_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, 2001, "Token 정보가 없습니다"),
	AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 2002, "만료된 Token 입니다"),
	AUTH_TOKEN_VALID_ERROR(HttpStatus.UNAUTHORIZED, 2003, "Token 검증 오류"),
	AUTH_TOKEN_DECODE_ERROR(HttpStatus.UNAUTHORIZED, 2004, "Token 복호화 오류");

	private HttpStatus httpStatus;
	private int apiErrorCode;
	private String displayMessage;

	ResultCode(HttpStatus httpStatus, int apiErrorCode, String displayMessage) {
		this.httpStatus = httpStatus;
		this.apiErrorCode = apiErrorCode;
		this.displayMessage = displayMessage;
	}
}
