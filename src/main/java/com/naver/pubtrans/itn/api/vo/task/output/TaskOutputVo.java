package com.naver.pubtrans.itn.api.vo.task.output;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * 작업 기본정보
 * @author adtec10
 * @param
 *
 */
@Getter
@Setter
public class TaskOutputVo {

	// Task ID
	private long taskId;

	// BIS 지자체 ID
	private int providerId;

	// BIS 지자체명
	private String providerName;

	// 작업구분
	private String taskType;

	// 진행상태
	private String taskStatus;

	// 대중교통 ID
	private int pubTransId;

	// 데이터 구분
	private String taskDataType;

	// 데이터 이름
	private String taskDataName;

	// 작업내용
	private String taskComment;

	// BIS 자동 변경감지 내용(Text)
	@JsonIgnore
	private String bisAutoChangeData;


	// BIS 자동 변경감지 내용
	private Object bisChangeDataInfo;


	// 등록부분
	private String taskRegisterType;

	//등록자명
	private String regUserName;

	// 등록자 ID
	private String regUserId;

	// 등록일
	private String regDate;

	// 작업자명
	private String workUserName;

	// 작업자 ID
	private String workUserId;

	// 작업 할당일
	private String workAssignDate;

	// 작업 완료일
	private String workCompleteDate;

	// 검수자명
	private String checkUserName;

	// 검수자 ID
	private String checkUserId;

	// 검수 할당일
	private String checkAssignDate;

	// 검수 완료일
	private String checkCompleteDate;
}
