package com.naver.pubtrans.itn.api.consts;

import java.util.stream.Stream;

import lombok.Getter;

/**
 * 작업 데이터 구분
 * @author adtec10
 *
 */
@Getter
public enum TaskDataType {

	ROUTE("route", "노선정보"),
	STOP("stop", "정류장 정보"),
	COMPANY("company", "운수사");

	// 코드
	private String code;

	// 설명
	private String descrition;

	TaskDataType(String code, String description){
		this.code = code;
		this.descrition = description;
	}

	public static Stream<TaskDataType> stream() {
		return Stream.of(TaskDataType.values());
	}
}
