package com.naver.pubtrans.itn.api.vo.bus.route.input;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.GeoJsonInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteTaskVo;

/**
 * 버스노선 작업 등록정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteTaskInputVo extends BusRouteTaskVo {

	// 노선명
	@NotBlank
	private String routeName;

	// 도시코드
	@NotNull
	private Integer cityCode;

	// 버스 노선 클래스
	@NotNull
	private Integer busClass;


	// 평일 기점 기준 첫차 출발시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String weekdayStartPointFirstTime;

	// 평일 기점 기준 첫차 도착시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String weekdayStartPointLastTime;

	// 평일 종점 기준  첫차 출발시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String weekdayEndPointFirstTime;

	// 평일 종점 기준 첫차 도착시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String weekdayEndPointLastTime;

	// 평일 최소 배차간격(분)
	private Integer weekdayIntervalMin;

	// 평일 최대 배차간격(분)
	private Integer weekdayIntervalMax;

	// 평일 배차간격(횟수)
	private Integer weekdayIntervalCount;

	// 토요일 기점 기준 첫차 출발시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String saturdayStartPointFirstTime;

	// 토요일 기점 기준 첫차 도착시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String saturdayStartPointLastTime;

	// 토요일 종점 기준 첫차 출발시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String saturdayEndPointFirstTime;

	// 토요일 종점 기준 첫차 도착시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String saturdayEndPointLastTime;

	// 토요일 최소 배차간격(분)
	private Integer saturdayIntervalMin;

	// 토요일 최대 배차간격(분)
	private Integer saturdayIntervalMax;

	// 토요일 배차간격(횟수)
	private Integer saturdayIntervalCount;

	// 일요일 기점 기준 첫차 출발시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String sundayStartPointFirstTime;

	// 일요일 기점 기준 첫차 도착시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String sundayStartPointLastTime;

	// 일요일 종점 기준 첫차 출발시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String sundayEndPointFirstTime;

	// 일요일 종점 기준 첫차 도착시간
	@Pattern(regexp="^((0[1-9]|2[0-3])([0-5][09]))$")
	private String sundayEndPointLastTime;

	// 일요일 최소 배차간격(분)
	private Integer sundayIntervalMin;

	// 일요일 최대 배차간격(분)
	private Integer sundayIntervalMax;

	// 일요일 배차간격(횟수)
	private Integer sundayIntervalCount;

	// 저상버스 운행여부
	@Pattern(regexp="^[YN]$")
	private String nonstepBusYn;

	// 예약 전화번호
	private String telReservation;

	// BIS 지역 ID
	private Integer providerId;

	// 우회노선 여부
	@Pattern(regexp="^[YN]$")
	private String bypassYn = "N";

	// BIS 노선 ID
	private String localRouteId;

	// 월요일 운행여부
	@NotBlank
	@Pattern(regexp="^[YN]$")
	private String mondayYn;

	// 화요일 운행여부
	@NotBlank
	@Pattern(regexp="^[YN]$")
	private String tuesdayYn;

	// 수요일 운행여부
	@NotBlank
	@Pattern(regexp="^[YN]$")
	private String wednesdayYn;

	// 목요일 운행여부
	@NotBlank
	@Pattern(regexp="^[YN]$")
	private String thursdayYn;

	// 금요일 운행여부
	@NotBlank
	@Pattern(regexp="^[YN]$")
	private String fridayYn;

	// 토요일 운행여부
	@NotBlank
	@Pattern(regexp="^[YN]$")
	private String saturdayYn;

	// 일요일 운행여부
	@NotBlank
	@Pattern(regexp="^[YN]$")
	private String sundayYn;

	// 본 노선 ID
	private Integer parentRouteId;

	// 우회 시작일시
	private String bypassStartDateTime;

	// 우회 종료일시
	private String bypassEndDateTime;

	// 작업내용
	@NotBlank
	private String taskComment;

	// 검수요청 구분 - 기본값 : 검수요청
	private TaskCheckRequestType taskCheckRequestType = TaskCheckRequestType.CHECK_REQUEST;

	// 데이터 출처 - 기본값 : 7(기타)
	private TaskDataSourceType taskDataSourceType = TaskDataSourceType.ETC;

	// 검수자 ID
	@NotBlank
	private String checkUserId;

	// 운수회사 정보
	@NotNull
	private List<@Valid BusRouteCompanyTaskInputVo> companyList;

	// 노선 경유정류장 구간 정보
	@NotNull
	private GeoJsonInputVo busStopGraphInfo;


}
