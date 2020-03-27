package com.naver.pubtrans.itn.api.vo.bus.route.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 우회노선 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteBypassOutputVo {

	// 우회노선 ID
	private int bypassRouteId;

	// 본 노선ID(부모노선 ID)
	private int parentRouteId;

	// 노선명
	private String routeName;

	// 우회 시작 일시(YYYYmmddHHmm)
	private String bypassStartDateTime;

	// 우회 종료 일시(YYYYmmddHHmm)
	private String bypassEndDateTime;
}
