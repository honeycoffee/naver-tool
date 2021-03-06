package com.naver.pubtrans.itn.api.vo.bus.stop.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopTaskVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스정류장 작업 등록/수정을 위한 입력정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopTaskInputVo extends BusStopTaskVo {



	/**
	 * 정류장명
	 */
	@NotBlank
	private String stopName;

	/**
	 * 경도
	 */
	@NotNull
	private Double longitude;

	/**
	 * 위도
	 */
	@NotNull
	private Double latitude;

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
