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
public class CompanyVo {

	// 운수사 ID
	private Integer companyId;

	// 운수사 명
	private Integer companyName;

	// 전화번호
	private Integer tel;

	// 주소
	private Integer addressName;

	// 출처명
	private Integer sourceName;

	// 출처 URL
	private Integer sourceUrl;

	// 비고
	private Integer comment;

	// 도시코드
	private Integer cityCode;

}
