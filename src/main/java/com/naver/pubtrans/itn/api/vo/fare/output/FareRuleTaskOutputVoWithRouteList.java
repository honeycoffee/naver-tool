package com.naver.pubtrans.itn.api.vo.fare.output;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.fare.FareRuleVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 관련 노선 List를 포함한 요금 룰 작업 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareRuleTaskOutputVoWithRouteList extends FareRuleVo {
	
	/**
	 * Task(작업) 정보
	 */
	private TaskOutputVo taskInfo;

	/**
	 * 요금 관련 노선 List 
	 */
	private List<BusRouteListOutputVo> busRouteInfoList;

}
