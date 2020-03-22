package com.naver.pubtrans.itn.api.vo.bus.graph.output;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureVo;

/**
 * GeoJson 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class GeoJsonOutputVo {
	// Geojson 타입
	private String type = CommonConstant.GEOJSON_TYPE_FEATURE_COLLECTION;

	// feature 목록
	private List<GeoJsonFeatureVo> features;
}
