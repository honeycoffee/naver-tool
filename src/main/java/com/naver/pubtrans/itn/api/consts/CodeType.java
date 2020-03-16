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
	TASK_STATUS("task_status", "Task 상태"),
	TASK_ASSIGN_TYPE("task_assign_type", "Task 할당 구분"),
	TASK_DATA_TYPE("task_data_type", "Task 데이터 구분"),
	CITYCODE("citycode", "도시코드"),
	BUS_PROVIDER("provider_id", "BIS 데이터 공급지역");

	// 코드 타입
	private String codeName;

	// 코드 설명
	private String codeDescription;


	CodeType(String codeName, String codeDescription){
		this.codeName = codeName;
		this.codeDescription = codeDescription;
	}


}
