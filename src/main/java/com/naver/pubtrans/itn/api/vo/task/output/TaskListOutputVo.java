package com.naver.pubtrans.itn.api.vo.task.output;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;

/**
 * 작업목록 출력 정보
 * @author adtec10
 *
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class TaskListOutputVo {

	// Task ID
	private long taskId;

	// 도시코드
	private int cityCode;

	// 도시코드명
	private String cityName;

	// BIS 지자체 ID
	private int providerId;

	// BIS 지자체명
	private String providerName;

	// 진행상태
	@JsonIgnore
	private TaskStatusType taskStatusType;

	// 작업 상태구분 명칭
	private String taskStatusTypeName;

	// 데이터 출처
	@JsonIgnore
	private TaskDataSourceType taskDataSourceType;

	// 데이터 출처 구분 명칭
	private String taskDataSourceTypeName;

	// 대중교통 ID
	private int pubTransId;

	// 데이터 구분
	@JsonIgnore
	private PubTransType pubTransType;

	// 대중교통 구분 명칭
	private String pubTransTypeName;

	// 데이터 이름
	private String pubTransName;

	// 작업내용
	private String taskComment;

	//등록자명
	private String regUserName;

	// 등록일
	private String regDate;

	// 작업자명
	private String workUserName;

	// 작업 할당일
	private String workAssignDate;

	// 작업 완료일
	private String workCompleteDate;

	// 검수자명
	private String checkUserName;

	// 검수 완료일
	private String checkCompleteDate;


	/**
	 * 작업 진행상태 구분 명칭
	 */
	public String getTaskStatusTypeName() {
		if(taskStatusType != null) {
			return taskStatusType.getDescription();
		}
		return null;
	}

	/**
	 * 작업 데이터 출처 구분 명칭
	 */
	public String getTaskDataSourceTypeName() {
		if(taskDataSourceType != null) {
			return taskDataSourceType.getCodeName();
		}
		return null;
	}

	/**
	 *작업 데이터 구분 명칭
	 */
	public String getPubTransTypeName() {
		if(pubTransType != null) {
			return pubTransType.getDescription();
		}
		return null;
	}

}
