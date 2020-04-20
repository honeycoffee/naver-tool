package com.naver.pubtrans.itn.api.vo.bus.company.output;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.bus.company.BusCompanyVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 버스 운수사 작업 정보를 포함한 버스 운수사 상세 출력정보
 * @author westwind
 *
 */
@Getter
@Setter
public class BusCompanyTaskOutputVo extends BusCompanyVo{

	/**
	 * Task(작업) 정보
	 */
	private TaskOutputVo taskInfo;

}
