package com.naver.pubtrans.itn.api.vo.task.input;

import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 작업정보 검색정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class TaskSearchVo extends SearchVo {


	/**
	 * 작업ID
	 */
	private Long taskId;

	/**
	 * BIS 지자체 ID
	 */
	private int providerId;

	/**
	 * 작업구분
	 */
	private String taskType;

	/**
	 * 진행상태
	 */
	private String taskStatus;

	/**
	 * 대중교통ID
	 */
	private int pubTransId;

	/**
	 * 데이터 구분
	 */
	private String taskDataType;
}
