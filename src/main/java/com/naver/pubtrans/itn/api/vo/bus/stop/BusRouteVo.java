package com.naver.pubtrans.itn.api.vo.bus.stop;

import lombok.Getter;
import lombok.Setter;

/**
 * 노선정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteVo {


	/**
	 * 버스 노선ID
	 */
	private int routeId;

	/**
	 * 버스 노선명
	 */
    private String routeName;

    /**
     * 노선 타입
     */
	private int busClass;

	/**
	 * 노선 타입명
	 */
	private String busClassName;
}
