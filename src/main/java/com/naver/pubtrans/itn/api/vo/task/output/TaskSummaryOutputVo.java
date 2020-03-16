package com.naver.pubtrans.itn.api.vo.task.output;

import lombok.Getter;
import lombok.Setter;

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
	private String taskType;

	/**
	 * 진행상태
	 */
	private String taskStatus;

	/**
	 * 데이터 구분
	 */
	private String taskDataType;

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
