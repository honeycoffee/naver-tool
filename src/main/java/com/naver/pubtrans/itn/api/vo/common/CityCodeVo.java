package com.naver.pubtrans.itn.api.vo.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 도시코드 Vo
 * @author adtec10
 *
 */
@Getter
@Setter
public class CityCodeVo {

	/**
	 * 도시코드
	 */
	private int cityCode ;

	/**
	 * 도시명
	 */
	private String cityName ;

	/**
	 * 도단위 구분코드
	 */
	private int superCode ;

	/**
	 *
	 */
	private int exceptCode ;

	/**
	 * 네이버 지역코드
	 */
	private String naverCode ;

	/**
	 * 도 명칭
	 */
	private String doName ;

	/**
	 * 도 단축 명칭
	 */
	private String abbrDoName ;

	/**
	 * 시/군 명칭
	 */
	private String siName ;

	/**
	 * 도 레벨(광역시 구분)
	 */
	private int level ;
}
