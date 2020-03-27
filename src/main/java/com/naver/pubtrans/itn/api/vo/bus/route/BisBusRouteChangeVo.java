package com.naver.pubtrans.itn.api.vo.bus.route;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteDetailOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopVo;

/**
 * BIS 버스노선 변경정보
 * @author adtec10
 *
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BisBusRouteChangeVo extends BusRouteDetailOutputVo {

	// BIS 경유정류장 변경사항 목록 - BIS 지역 아이디로 저장
	private List<BusStopVo> bisBusStopList ;


}
