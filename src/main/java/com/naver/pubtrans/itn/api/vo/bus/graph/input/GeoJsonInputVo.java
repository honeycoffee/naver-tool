package com.naver.pubtrans.itn.api.vo.bus.graph.input;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.CommonConstant;

/**
 * 노선 경유정류장 목록 및 그래프 입력정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class GeoJsonInputVo {
	// Geojson 타입
	private String type = CommonConstant.GEOJSON_TYPE_FEATURE_COLLECTION;

	// feature 목록
	private List<GeoJsonFeatureInputVo> features;

}
