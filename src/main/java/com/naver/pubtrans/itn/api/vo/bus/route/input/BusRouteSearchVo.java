package com.naver.pubtrans.itn.api.vo.bus.route.input;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;

/**
 * 버스 노선 목록 검색조건
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteSearchVo extends SearchVo {

	// 도시코드
	private String cityCode;

	// 노선 타입
	private Integer busClass;

	// 노선 명칭
	private String routeName;

	// 노선ID
	private String routeId;
}
