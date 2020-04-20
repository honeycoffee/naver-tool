package com.naver.pubtrans.itn.api.vo.bus.route.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 노선 삭제 작업 등록 입력 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteRemoveTaskInputVo {

	/**
	 * 노선 ID
	 */
	@NotNull
	private int routeId;

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
