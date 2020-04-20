package com.naver.pubtrans.itn.api.vo.bus.company.input;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 운수사 검색 조건
 * @author westwind
 *
 */
@Getter
@Setter
public class CompanySearchVo {

	// 운수사 ID
	private Integer companyId;

	// 운수사명
	private Integer companyName;

	// 지역코드
	private Integer cityCode;

	// 전화번호
	private Integer tel;

}
