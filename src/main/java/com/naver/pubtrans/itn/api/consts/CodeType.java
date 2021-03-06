package com.naver.pubtrans.itn.api.consts;

import lombok.Getter;

/**
 * 공통 코드타입 명칭을 정의한다
 * @author adtec10
 *
 */
@Getter
public enum CodeType {

	Y_N("Y_N", "Y/N"),
	TASK_TYPE("task_type", "Task 작업 구분"),
	TASK_STATUS_TYPE("task_status_type", "Task 상태 구분"),
	TASK_ASSIGN_TYPE("task_assign_type", "Task 할당 구분"),
	PUB_TRANS_TYPE("pub_trans_type", "대중교통 구분"),
	TASK_CHECK_REQUEST_TYPE("task_check_request_type", "Task 검수요청 구분"),
	TASK_DATA_SOURCE_TYPE("task_data_source_type", "데이터 출처 구분"),
	CITYCODE("city_code", "도시코드"),
	BUS_PROVIDER("provider_id", "BIS 데이터 공급지역"),
	BUS_ROUTE_CLASS("bus_route_class", "버스 노선 클래스"),
	AUTHORITY_ID("authority_id", "권한 ID"),
	TRANSPORT_ID("transport_id", "대중교통 구분 ID");

	// 코드 타입
	private String codeName;

	// 코드 설명
	private String codeDescription;

	CodeType(String codeName, String codeDescription) {
		this.codeName = codeName;
		this.codeDescription = codeDescription;
	}

}
