package com.naver.pubtrans.itn.api.vo.bus.company;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 운수사 작업정보
 * @author westwind
 *
 */
@Getter
@Setter
public class CompanyTaskVo extends CompanyVo {

	// 작업ID
	private long taskId;

}
