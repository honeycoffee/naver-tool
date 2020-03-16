package com.naver.pubtrans.itn.api.vo.common;

import lombok.Getter;
import lombok.Setter;

/**
 * BIS 데이터 공급지역 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusProviderVo {

	/**
	 * BIS 지역 ID
	 */
	private String providerId;

	/**
	 * BIS 지역명
	 */
	private String providerName;

	/**
	 * 도시코드 범위(최소)
	 */
	private int cityCodeMin;

	/**
	 * 도시코드 범위(최대)
	 */
	private int cityCodeMax;
}
