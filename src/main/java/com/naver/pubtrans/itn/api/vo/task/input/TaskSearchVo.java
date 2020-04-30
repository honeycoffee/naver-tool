package com.naver.pubtrans.itn.api.vo.task.input;

import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;

import java.util.EnumSet;

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
	private Integer providerId;

	/**
	 * 작업구분
	 */
	private TaskType taskType;

	/**
	 * 진행상태
	 */
	private TaskStatusType taskStatusType;

	/**
	 * 대중교통ID
	 */
	private Integer pubTransId;

	/**
	 * 데이터 구분
	 */
	private PubTransType pubTransType;

	/**
	 * 작업 데이터 출처 구분
	 */
	private TaskDataSourceType taskDataSourceType;

	/**
	 * 대중교통 명칭
	 */
	private String pubTransName;

	/**
	 * 도시코드
	 */
	private Integer cityCode;

	/**
	 * 등록자 ID
	 */
	private String regUserId;

	/**
	 * 등록 검색 시작일
	 */
	private String startRegDate;

	/**
	 * 등록 검색 종료일
	 */
	private String endRegDate;

	/**
	 * 작업자 ID
	 */
	private String workUserId;

	/**
	 * 작업 검색 시작일
	 */
	private String startWorkDate;

	/**
	 * 작업 검색 종료일
	 */
	private String endWorkDate;

	/**
	 * 검수자 ID
	 */
	private String checkUserId;

	/**
	 * 검수 검색 시작일
	 */
	private String startCheckDate;

	/**
	 * 검수 검색 종료일
	 */
	private String endCheckDate;


}
