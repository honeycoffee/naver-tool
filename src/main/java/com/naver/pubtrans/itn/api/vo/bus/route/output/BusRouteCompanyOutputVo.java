package com.naver.pubtrans.itn.api.vo.bus.route.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스노선 운수회사 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteCompanyOutputVo {

	// 노선ID
	private int routeId;

	// 운수회사 ID
	private int companyId;

	// 운수회사 명
	private String companyName;

	// 전화번호
	private String tel;

	// 도시코드
	private int cityCode;

	// 도시코드명
	private String cityName;
}
