package com.naver.pubtrans.itn.api.vo.bus.route;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스노선 작업정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteTaskVo {

	// 작업ID
	private long taskId;

	// 노선ID
	private Integer routeId;

	// 노선명
	private String routeName;

	// 도시코드
	private Integer cityCode;

	// 버스 노선 클래스
	private Integer busClass;

	// 버스 노선 부가명칭
	private String busAdditionalName;

	// 기점 정류장 명
	private String startPointName;

	// 종점 정류장 명
	private String endPointName;

	// 회차 정류장 순번
	private int turningPointSequence;

	// 평일 기점 기준 첫차 출발시간
	private String weekdayStartPointFirstTime;

	// 평일 기점 기준 첫차 도착시간
	private String weekdayStartPointLastTime;

	// 평일 종점 기준  첫차 출발시간
	private String weekdayEndPointFirstTime;

	// 평일 종점 기준 첫차 도착시간
	private String weekdayEndPointLastTime;

	// 평일 최소 배차간격(분)
	private Integer weekdayIntervalMin;

	// 평일 최대 배차간격(분)
	private Integer weekdayIntervalMax;

	// 평일 배차간격(횟수)
	private Integer weekdayIntervalCount;

	// 토요일 기점 기준 첫차 출발시간
	private String saturdayStartPointFirstTime;

	// 토요일 기점 기준 첫차 도착시간
	private String saturdayStartPointLastTime;

	// 토요일 종점 기준 첫차 출발시간
	private String saturdayEndPointFirstTime;

	// 토요일 종점 기준 첫차 도착시간
	private String saturdayEndPointLastTime;

	// 토요일 최소 배차간격(분)
	private Integer saturdayIntervalMin;

	// 토요일 최대 배차간격(분)
	private Integer saturdayIntervalMax;

	// 토요일 배차간격(횟수)
	private Integer saturdayIntervalCount;

	// 일요일 기점 기준 첫차 출발시간
	private String sundayStartPointFirstTime;

	// 일요일 기점 기준 첫차 도착시간
	private String sundayStartPointLastTime;

	// 일요일 종점 기준 첫차 출발시간
	private String sundayEndPointFirstTime;

	// 일요일 종점 기준 첫차 도착시간
	private String sundayEndPointLastTime;

	// 일요일 최소 배차간격(분)
	private Integer sundayIntervalMin;

	// 일요일 최대 배차간격(분)
	private Integer sundayIntervalMax;

	// 일요일 배차간격(횟수)
	private Integer sundayIntervalCount;

	// 저상버스 운행여부
	private String nonstepBusYn;

	// 예약 전화번호
	private String telReservation;

	// BIS 지역 ID
	private Integer providerId;

	// 우회노선 여부
	private String bypassYn;

	// BIS 노선 ID
	private String localRouteId;

	// 월요일 운행여부
	private String mondayYn;

	// 화요일 운행여부
	private String tuesdayYn;

	// 수요일 운행여부
	private String wednesdayYn;

	// 목요일 운행여부
	private String thursdayYn;

	// 금요일 운행여부
	private String fridayYn;

	// 토요일 운행여부
	private String saturdayYn;

	// 일요일 운행여부
	private String sundayYn;

	// 본 노선 ID
	private String parentRouteId;

	// 우회 시작일시
	private String bypassStartDateTime;

	// 우회 종료일시
	private String bypassEndDateTime;
}
