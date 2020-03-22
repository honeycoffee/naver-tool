package com.naver.pubtrans.itn.api.vo.bus.graph;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.bus.graph.output.BusStopGraphOutputVo;

/**
 * GeoJson Feature 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class GeoJsonFeatureVo {

	// 타입
	private String type;

	// geometry 정보
	private GeoJsonFeatureGeometryVo geometry;

	// 그래프 구간 정보
	private BusStopGraphOutputVo properties;
}
