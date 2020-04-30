package com.naver.pubtrans.itn.api.vo.bus.route.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;

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
