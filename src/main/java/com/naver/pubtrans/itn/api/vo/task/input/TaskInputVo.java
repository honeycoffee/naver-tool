package com.naver.pubtrans.itn.api.vo.task.input;


import lombok.Getter;
import lombok.Setter;

/**
 * 작업 입력 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class TaskInputVo {

	// Task ID
	private long taskId;

	// BIS 지자체 ID
	private int providerId;

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

	// BIS 자동 변경내용
	private String bisAutoChangeData;

	// 등록부분
	private String taskRegisterType;

	//등록자명
	private String regUserName;

	// 등록자 ID
	private String regUserId;

	// 작업자명
	private String workUserName;

	// 작업자 ID
	private String workUserId;

	// 검수자명
	private String checkUserName;

	// 검수자 ID
	private String checkUserId;


}
