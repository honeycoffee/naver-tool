package com.naver.pubtrans.itn.api.vo.bus.route.output;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.bus.graph.output.GeoJsonOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteVo;

/**
 * 버스 노선 상세정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteDetailOutputVo extends BusRouteVo {

	// 노선 운행 경로 정보
	private GeoJsonOutputVo busStopGraphInfo;

	// 운수회사 목록
	private List<BusRouteCompanyOutputVo> companyList;

	// 우회노선 목록
	private List<BusRouteBypassOutputVo> bypassChildList;

}
