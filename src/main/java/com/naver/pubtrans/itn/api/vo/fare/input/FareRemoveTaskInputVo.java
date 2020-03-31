package com.naver.pubtrans.itn.api.vo.fare.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * 요금 룰 삭제 작업등록 입력 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareRemoveTaskInputVo {

	/**
	 * 요금 룰 ID
	 */
	@NotNull
	private int fareId;

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
