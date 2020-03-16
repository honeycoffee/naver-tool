package com.naver.pubtrans.itn.api.vo.task;

import lombok.Getter;
import lombok.Setter;

/**
 * 작업 할당정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class TaskAssignInfoVo {

	// 작업ID
	private long taskId;

	// 할당 타입
	private String taskAssignType;

	// 할당 대상자 ID
	private String assigneeUserId;

	// 대상자 이름
	private String assigneeUserName;

	// 등록자 ID
	private String regUserId;

	// 등록자 이름
	private String regUserName;
}
