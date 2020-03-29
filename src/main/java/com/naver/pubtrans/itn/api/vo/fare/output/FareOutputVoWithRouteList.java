package com.naver.pubtrans.itn.api.vo.fare.output;

import java.util.List;

import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusRouteOutputVo;
import com.naver.pubtrans.itn.api.vo.fare.FareVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 관련 노선 List를 포함한 요금 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareOutputVoWithRouteList extends FareVo {

	/**
	 * 요금 관련 노선 List 
	 */
	private List<BusRouteOutputVo> busRouteInfoList;

}
