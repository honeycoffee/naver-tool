package com.naver.pubtrans.itn.api.consts;

/**
 * 공통 상수 모음
 * @author adtec10
 *
 */
public interface CommonConstant {

	String Y = "Y";
	String N = "N";
	String YES = "YES";
	String NO = "NO";


	// 문자형
	String STRING = "string";

	// 숫자형
	String NUMBER = "number";

	// 열거형
	String ENUM = "enum";

	// 내림차순 정렬
	String DESC = "DESC";

	// 요름차순 정렬
	String ASC = "ASC";

	// 괄호 열기
	String BRACKET_START = "(";

	// 괄호 닫기
	String BRACKET_END = ")";

	// 쉼표
	String COMMA = ",";

	String UNDERSCORE = "_";

	String SHOW_SCHEMA = "showSchema";

	// 자동
	String AUTO = "A";

	// 수동
	String MANUAL = "M";

	// 이용컬럼
	String USABLE_COLUMN = "usable";

	// 제외컬럼
	String IGNORE_COLUMN = "ignore";

	// 작업ID Prefix
	String TASK_ID_PREFIX = "T";

	// 작업ID 길이
	int TASK_ID_LENGTH = 15;

	// 영문 알파벳  A ~ Z 길이
	int ALPHABET_LENGTH = 26;

	// 영문 A 아스키 번호
	int ASCII_CHAR_A_NUMBER = 65;

	// 정류장 ID 이름
	String KEY_STOP = "stopId";

	// 작업ID 이름
	String KEY_TASK = "taskId";

	// 버스정류장 Task 입력 정보 VO명
	String BUS_STOP_TASK_INPUT_VO = "BusStopTaskInputVo" ;

}
