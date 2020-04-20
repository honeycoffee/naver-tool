package com.naver.pubtrans.itn.api.vo.fare.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * 요금 정책 등록/수정을 위한 작업 입력정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareTaskInputVo {

	/**
	 * 요금 ID
	 */
	private Integer fareId;

	/**
	 * 작업 ID
	 */
	private long taskId;

	/**
	 * 기본요금(일반_카드기준)
	 */
	private Integer baseFare;

	/**
	 * 기본거리
	 */
	private Integer baseDist;

	/**
	 * 단위요금
	 */
	private Integer unitFare;

	/**
	 * 단위거리
	 */
	private Integer unitDist;

	/**
	 * 최대 요금
	 */
	private Integer maxFare;

	/**
	 * 요금 룰의 기본 정보 여부
	 */
	@NotNull
	private String baseYn;

	/**
	 * 연령별 타입
	 */
	private Integer ageId;

	/**
	 * 지불 방식 식별
	 */
	private Integer paymentId;

	/**
	 * 시작 정류장 Array
	 */
	private Integer[] startStopIds;

	/**
	 * 시작 정류장
	 */
	private Integer startStopId;

	/**
	 * 도착 정류장 Array
	 */
	private Integer[] endStopIds;

	/**
	 * 도착 정류장
	 */
	private Integer endStopId;

	/**
	 * 운행 요일 ID
	 */
	private Integer serviceId;

	/**
	 * 지역코드
	 */
	@NotNull
	private Integer cityCode;

	/**
	 * 버스 노선 종류
	 */
	@NotNull
	private Integer busClass;
	
	/**
	 * 출처 명
	 */
	private String sourceName;
	
	/**
	 * 출처 URL
	 */
	private String sourceUrl;

	/**
	 * 설명
	 */
	private String comment;

	/**
	 * 버스노선 ID Array
	 */
	private Integer[] routeIds;

	/**
	 * 버스노선 ID
	 */
	private Integer routeId;

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
