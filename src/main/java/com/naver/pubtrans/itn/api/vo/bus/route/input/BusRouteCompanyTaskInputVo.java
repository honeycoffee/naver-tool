package com.naver.pubtrans.itn.api.vo.bus.route.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스노선 운수회사 입력 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteCompanyTaskInputVo {

	// 작업ID
	private long taskId;

	// 노선ID
	private int routeId;

	// 운수회사 ID
	@NotNull
	private Integer companyId;

}
