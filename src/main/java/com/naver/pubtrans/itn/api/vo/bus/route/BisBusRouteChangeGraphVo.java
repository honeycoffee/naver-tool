package com.naver.pubtrans.itn.api.vo.bus.route;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.bus.graph.output.GeoJsonOutputVo;

/**
 * BIS 노선 경유정류장 변경 그래프 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BisBusRouteChangeGraphVo {

	// 정류장 목록
	private List<String> busStopIds;

	// 버스 정류장 매핑 정보에서 매핑되지 않는 BIS 정류장 ID 목록
	private List<String> nonMatchLocalStopIds;

	// 경유 정류장 그래프 정보
	private GeoJsonOutputVo geoJsonOutputVo;
}
