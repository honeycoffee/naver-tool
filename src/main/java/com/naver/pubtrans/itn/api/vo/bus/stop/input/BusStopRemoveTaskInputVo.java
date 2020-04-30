package com.naver.pubtrans.itn.api.vo.bus.stop.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;

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
