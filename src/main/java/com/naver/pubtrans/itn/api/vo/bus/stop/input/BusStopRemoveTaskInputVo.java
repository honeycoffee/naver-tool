package com.naver.pubtrans.itn.api.vo.bus.stop.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 정류장 삭제 작업등록 입력 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopRemoveTaskInputVo {

	/**
	 * 정류장 ID
	 */
	@NotNull
	private int stopId;

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
