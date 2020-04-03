package com.naver.pubtrans.itn.api.vo.bus.graph.input;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureGeometryVo;

/**
 * 노선 경유정류장 구간 그래프 입력정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class GeoJsonFeatureInputVo {

	// 타입
	private String type;

	// geometry 정보
	private GeoJsonFeatureGeometryVo geometry;

	// 그래프 구간 정보
	private BusStopGraphInputVo properties;
}
