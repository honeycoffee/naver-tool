package com.naver.pubtrans.itn.api.vo.bus.stop.input;

import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 정류장 목록 검색조건
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopSearchVo extends SearchVo {

	/**
	 * 도시코드
	 */
	private String cityCode ;

	/**
	 * 정류장명
	 */
	private String stopName ;

	/**
	 * 정류장 ID
	 */
	private int stopId ;
}
