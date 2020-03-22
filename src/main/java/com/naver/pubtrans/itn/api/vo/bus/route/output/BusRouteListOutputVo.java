package com.naver.pubtrans.itn.api.vo.bus.route.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 노선관리 목록 데이터
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteListOutputVo {

	// 노선ID
	private int routeId;

	// 노선명
    private String routeName;

    // 노선 타입
	private int busClass;

	// 노선 타입 명칭
	private String busClassName;

	// 기점 정류장 명
	private String startPointName;

	// 종점 정류장 명
	private String endPointName;

	// 도시코드
	private String cityCode;

	// 도시코드 명칭
	private String cityName;

	// 우회노선 여부 - 해당 노선이 우회노선 인지
	private String bypassYn;

	// 우회노선 수
	private int bypassRouteCnt;
}
