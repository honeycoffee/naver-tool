package com.naver.pubtrans.itn.api.vo.task;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.TaskStatusType;

/**
 * Task 진행상태 개수 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class TaskStatusTypeCntVo {

	// Task 진행상태 구분
	private TaskStatusType taskStatusType;

	// Task 진행상태 개수
	private int taskStatusTypeCnt;
}
