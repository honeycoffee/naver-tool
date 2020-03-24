package com.naver.pubtrans.itn.api.consts;

import lombok.Getter;

/**
 * 대중교통 내재화 툴에서 사용되는 DB Table 정의
 * @author adtec10
 *
 */
@Getter
public enum PubTransTable {

	TB_BUS_COMPANY("tb_bus_company", "버스 운수사 정보"),
	TB_BUS_COMPANY_MAPPING("tb_bus_company_mapping", "노선별 운수사 매핑"),
	TB_BUS_PROVIDER("tb_bus_provider","실시간 버스정보 공급처"),
	TB_BUS_ROUTE_CLASS("tb_bus_route_class","버스노선 클래스 정보"),
	TB_BUS_ROUTE_MAPPING("tb_bus_route_mapping", "버스노선 매핑"),
	TB_BUS_ROUTES_INFO("tb_bus_routes_info", "노선 부가 정보"),
	TB_BUS_STOP_MAPPING("tb_bus_stop_mapping", "버스정류장 매핑"),
	TB_BUS_STOP_TIMES("tb_bus_stop_times", "운행 스케쥴정보"),
	TB_BUS_STOPS_INFO("tb_bus_stops_info", "정류장 부가 정보"),
	TB_CALENDAR("tb_calendar", "서비스 날짜"),
	TB_CALENDAR_DATES("tb_calendar_dates","서비스 날짜 예외 처리"),
	TB_CITY_CODE("tb_city_code", "도시코드"),
	TB_FARE_RULE("tb_fare_rule", "요금 룰"),
	TB_ROUTE_FARE_MAPPING("tb_route_fare_mapping","요금 매핑"),
	TB_ROUTE_STOPS("tb_route_stops", "노선별 정류장"),
	TB_ROUTES("tb_routes", "노선"),
	TB_STOP_GRAPH("tb_stop_graph", "정류장 구간 그래프 정보"),
	TB_STOPS("tb_stops", "정류장 기본정보"),
	TB_TRANSPORT("tb_transport", "대중교통 수단"),
	TB_TRIPS("tb_trips", "각 경로에 대한 이동 정보"),

	TB_Z_SVC_MEMBER("tb_z_svc_member", "회원정보"),
	TB_Z_SVC_AUTH_INFO("tb_z_svc_auth_info", "권한정보"),
	TB_Z_SVC_NOTICE("tb_z_svc_notice", "공지사항"),
	TB_Z_SVC_TASK("tb_z_svc_task", "작업정보");

	// 테이블명
	private String name;

	// 테이블 설명
	private String comment;

	PubTransTable(String name, String comment){
		this.name = name;
		this.comment = comment;
	}

}