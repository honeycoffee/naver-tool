package com.naver.pubtrans.itn.api.vo.bus.graph;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * GeoJson geometry 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class GeoJsonFeatureGeometryVo {

	// geometry 타입
	private String type;

	// 좌표정보
	private List<List<Double>> coordinates;
}
