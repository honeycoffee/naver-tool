package com.naver.pubtrans.itn.api.vo.bus.stop.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopTaskVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스정류장 등록/수정을 위한 작업 입력정보
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
