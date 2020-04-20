package com.naver.pubtrans.itn.api.vo.bus.company.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 운수사 목록 데이터
 * @author westwind
 *
 */
@Getter
@Setter
public class BusCompanyListOutputVo {

	// 운수사 ID
	private Integer companyId;

	// 운수사 명
	private String companyName;

	// 전화번호
	private String tel;

	// 도시코드
	private Integer cityCode;

	// 도시명
	private String cityName;

}
