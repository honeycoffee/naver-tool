package com.naver.pubtrans.itn.api.vo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 스키마VO의 컬럼 이름 변경정보를 담는다.
 * @author adtec10
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class AliasColumnNameVo {

	/**
	 * 테이블 원 컬럼명
	 */
	private String originalColumnName;

	/**
	 * Alias 컬럼명
	 */
	private String aliasColumnName;
}
