package com.naver.pubtrans.itn.api.vo.fare.output;

import java.util.List;

import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusRouteOutputVo;
import com.naver.pubtrans.itn.api.vo.fare.FareVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 요금 룰  정보
 * @author westwind
 *
 */
@Getter
@Setter
public class IgnoredFareListOutputVo extends FareVo {

	/**
	 * 요금
	 */
	private List<BusRouteOutputVo> busRouteInfoList;

}
