package com.naver.pubtrans.itn.api.vo.bus.stop.output;

import java.util.List;

import com.naver.pubtrans.itn.api.vo.bus.stop.BusRouteVo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 정류장 경유노선 정보
 * @author adtec10
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class BusRouteOutputVo {

	/**
	 * 정류장 경유노선 수
	 */
	private int busRouteCnt;

	/**
	 * 정류장 경유노선 목록
	 */
	private List<BusRouteVo> busRouteList;

}
