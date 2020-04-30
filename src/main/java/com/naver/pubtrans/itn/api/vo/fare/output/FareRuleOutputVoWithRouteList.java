package com.naver.pubtrans.itn.api.vo.fare.output;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.fare.FareRuleVo;

/**
 * 관련 노선 List를 포함한 요금 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareRuleOutputVoWithRouteList extends FareRuleVo {

	/**
	 * 요금 관련 노선 List 
	 */
	private List<BusRouteListOutputVo> busRouteInfoList;

}
