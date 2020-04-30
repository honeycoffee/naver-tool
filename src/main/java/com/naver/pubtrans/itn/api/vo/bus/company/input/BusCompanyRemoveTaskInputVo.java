package com.naver.pubtrans.itn.api.vo.bus.company.input;

import java.util.EnumSet;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.vo.bus.company.BusCompanyTaskVo;

/**
 * 버스 운수사 삭제 작업등록 입력 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class BusCompanyRemoveTaskInputVo {

	/**
	 * 운수사 ID
	 */
	@NotNull
	private int companyId;

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
