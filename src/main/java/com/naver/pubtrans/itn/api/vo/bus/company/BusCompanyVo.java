package com.naver.pubtrans.itn.api.vo.bus.company;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스 운수사 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class BusCompanyVo {

	// 운수사 ID
	private int companyId;

	// 운수사 명
	private String companyName;

	// 전화번호
	private String tel;

	// 주소
	private String addressName;

	// 출처명
	private String sourceName;

	// 출처 URL
	private String sourceUrl;

	// 비고
	private String comment;

	// 도시코드
	private int cityCode;

}
