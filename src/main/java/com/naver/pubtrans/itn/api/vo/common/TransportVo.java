package com.naver.pubtrans.itn.api.vo.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 대중교통 코드 정보 Vo
 * @author adtec10
 *
 */
@Getter
@Setter
public class TransportVo {

	// 대중교통 ID
	private int tranportId;

	// 대중교통 명칭
	private String name;

	// 교통수단 유형
	private int stationClass;
}
