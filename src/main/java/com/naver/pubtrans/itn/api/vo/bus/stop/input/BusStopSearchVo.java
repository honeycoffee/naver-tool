package com.naver.pubtrans.itn.api.vo.bus.stop.input;

import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;

import java.util.List;

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
	private Integer cityCode;

	/**
	 * 정류장명
	 */
	private String stopName;

	/**
	 * 정류장 ID
	 */
	private Integer stopId;

	/**
	 * 지도 우측상단 좌표
	 */
	List<Double> rightTopCoordinates;

	/**
	 * 지도 좌측하단 좌표
	 */
	List<Double> leftBottomCoordinates;
}
