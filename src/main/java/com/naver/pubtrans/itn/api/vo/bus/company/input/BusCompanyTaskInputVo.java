package com.naver.pubtrans.itn.api.vo.bus.company.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.vo.bus.company.BusCompanyTaskVo;

/**
 * 버스 운수사 작업 등록/수정을 위한 입력정보
 * @author westwind
 *
 */
@Getter
@Setter
public class BusCompanyTaskInputVo extends BusCompanyTaskVo{

	/**
	 * 운수사명
	 */
	@NotBlank
	private String companyName;

	/**
	 * 도시코드
	 */
	@NotNull
	private int cityCode;


	/**
	 * 작업ID
	 */
	private long taskId;

	/**
	 * 검수요청 구분 - 기본값 : 검수요청
	 */
	private TaskCheckRequestType taskCheckRequestType = TaskCheckRequestType.CHECK_REQUEST;

	/**
	 * 데이터 출처 - 기본값 : 7(기타)
	 */
	private TaskDataSourceType taskDataSourceType = TaskDataSourceType.ETC;

	/**
	 * 작업내용
	 */
	@NotBlank
	private String taskComment;


	/**
	 * 검수자 ID
	 */
	@NotBlank
	private String checkUserId;


}
