package com.naver.pubtrans.itn.api.vo.task.output;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.consts.TaskType;

/**
 * 작업 목록 요약정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class TaskSummaryOutputVo {

	/**
	 * 작업 ID
	 */
	private long taskId;

	/**
	 * 작업구분
	 */
	private TaskType taskType;

	/**
	 * 진행상태
	 */
	private TaskStatusType taskStatusType;

	/**
	 * 데이터 구분
	 */
	private PubTransType pubTransType;

	/**
	 * 변경내용
	 */
	private String taskComment;

	/**
	 * 최초 등록일
	 */
	private String regDate;

	/**
	 * 작업자 이름
	 */
	private String workUserName;

}
