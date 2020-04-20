package com.naver.pubtrans.itn.api.vo.bus.company.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

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
