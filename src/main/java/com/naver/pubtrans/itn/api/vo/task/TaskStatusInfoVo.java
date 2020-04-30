package com.naver.pubtrans.itn.api.vo.task;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.TaskStatusType;

/**
 * 작업 상태 변경정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class TaskStatusInfoVo {

	// 작업 ID
	private long taskId;

	// 작업 상태
	private TaskStatusType taskStatusType;

	// 등록일
	private String regDate;

	// 등록자 ID
	private String regUserId;

	// 등록자 이름
	private String regUserName;

}
