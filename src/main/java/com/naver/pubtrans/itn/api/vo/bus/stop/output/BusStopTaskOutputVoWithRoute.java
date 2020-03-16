package com.naver.pubtrans.itn.api.vo.bus.stop.output;

import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 정류장 작업정보와 경유노선을 포함하는 버스 정류장 상세 출력정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopTaskOutputVoWithRoute extends BusStopVo {


	/**
	 * Task(작업) 정보
	 */
	private TaskOutputVo taskInfo;

	/**
	 * 정류장 경유노선 정보
	 */
	private BusRouteOutputVo busRouteInfo;

}
