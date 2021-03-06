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

	// 이용컬럼
	String USABLE_COLUMN = "usable";

	// 예외컬럼
	String EXCEPTION_COLUMN = "exception";

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

	// 노선 ID 이름
	String KEY_ROUTE = "routeId";

	// 운수사 ID 이름
	String KEY_COMPANY = "companyId";

	// 작업ID 이름
	String KEY_TASK = "taskId";	
	
	// 작업ID 이름 목록	
	String KEY_TASK_LIST = "taskIdList";

	// 버스정류장 Task 입력 정보 VO명
	String BUS_STOP_TASK_INPUT_VO = "BusStopTaskInputVo";

	// 버스노선 Task 입력 정보 Vo명
	String BUS_ROUTE_TASK_INPUT_VO = "BusRouteTaskInputVo";

	// 버스 운수사 Task 입력 정보 Vo명
	String BUS_COMPANY_TASK_INPUT_VO = "BusCompanyTaskInputVo";

	// 요금 룰 Task 입력 정보 VO명
	String FARE_RULE_TASK_INPUT_VO = "FareRuleTaskInputVo";

	// geojson Geometry  type - LineString 명칭
	String GEOJSON_GEOMETRY_TYPE_LINE_STRING = "LineString";

	// geojson feature collection
	String GEOJSON_TYPE_FEATURE_COLLECTION = "FeatureCollection";

	// geojson feature type
	String GEOJSON_TYPE_FEATURE_TYPE = "Feature";

	// 비밀번호 최소 값
	int PASSWORD_MIN = 8;

	// 버스 정류장 매핑 정보에 존재하지 않는 BIS 정류장 메시지
	String NONEXISTENT_BIS_BUS_STOP_IN_BUS_STOP_MAPPING = "BIS 경유 정류장이 변경되었으나, 일부 버스 정류장이 버스 정류장 매핑 정보에 존재하지 않습니다. 정류장 매핑 정보에 존재하지 않는 BIS 정류장 ID-";

	// 개행문자
	String NEWLINE = "\n";

	// 날짜 시간 포맷
	String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	// 공백
	String BLANK = " ";

	// access_token 만료 초 단위 (600 = 10분)
	int ACCESS_TOKEN_EXPIRE_TIME_SEC = 600;

	// refresh_token 만료 초 단위 (86400 = 24시간)
	int REFRESH_TOKEN_EXPIRE_TIME_SEC = 86400;

	// Test의 tokenMap, request 에서 쓰이는 Access Token Key
	String ACCESS_TOKEN_KEY = "accessToken";

	// Test의 tokenMap, request 에서 쓰이는 Refresh Token Key
	String REFRESH_TOKEN_KEY = "refreshToken";

	// Spring Security Config에서 접근 제어에 사용되는 관리자 권한 명
	String ADMIN = "ADMIN";

	// Spring Security Config에서 접근 제어에 사용되는 사용자 권한 명
	String USER = "USER";

	// Spring Security Config에서 접근 제어에 사용되는 익명 사용자 권한 명
	String ANONYMOUS = "ANONYMOUS";

	// 토큰 갱신 API URI
	String REFRESH_TOKEN_API_URI = "/refresh/token";

	// Filter, Handler 에서 Exception 출력시 쓰이는 ContentType
	String CONTENT_TYPE_APPLICATION_JSON = "application/json";

	// Filter, Handler 에서 Exception 출력시 쓰이는 CharacterEncoding
	String CHARACTER_ENCODING_UTF_8 = "UTF-8";

	// 100
	int EXPONENT_TWO = 2;

	// 100
	int NUMBER_ONE_HUNDRED = 100;

	// 100.0
	double DECIMAL_ONE_HUNDRED = 100.0;

	// 요금 룰 ID 이름
	String KEY_FARE = "fareId";

	// 예외 요금 명명에 쓰이는 예외 텍스트
	String EXCEPTION_TEXT = "예외";

	// 예외 요금 명명에 쓰이는 요금 텍스트
	String FARE_TEXT = "요금";

	// 사용자로부터 입력받는 날짜시간 형식
	String INPUT_DATE_TIME_PATTERN = "yyyyMMddHHmmss";

	// 초단위 시작(00초)
	String FIRST_SECONDS = "00";

	// 초단위 마지막(59초)
	String LAST_SECONDS = "59";

	// 지도영역 검색 좌표 길이(2개 : 경도, 위도)
	int MAP_BOUNDS_SEARCH_COORDINATES_LENGTH = 2;

	// 콜론
	String COLON = ":";

}
