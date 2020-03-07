package com.naver.pubtrans.itn.api.vo.common.input;

import org.apache.commons.lang3.StringUtils;

import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.CommonConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * 검색관련 공통 VO
 * @author adtec10
 *
 */
@Getter
@Setter
public class SearchVo {

	// 요청 페이지 번호
	private int pageNo ;

	// 요청 페이지 목록 수
	private int listSize ;

	/**
	 * 정렬 - 사용자 전송 파라미터
	 * <pre>
	 * 예1 : idx,DESC - 정렬기준,정렬방법
	 * 예2 : idx - 정렬기준. 정렬방법 미지정시 내림차순
	 * </pre>
	 */
	private String sort ;

	// 목록조회 시작 번호
	private int startPageLimit;

	// 조회 게시글 수(페이지당 게시글 노출 수)
	private int endPageLimit ;

	// 목록조회 정렬기준
	private String sortKey ;

	// 목록조회 정렬방법
	private String sortType = CommonConstant.DESC;


	/**
	 * 정렬키 가져오기
	 * @return
	 */
	public String getSortKey() {

		String s = this.sort ;

		if(StringUtils.isNotEmpty(s)) {

			if(s.indexOf(CommonConstant.COMMA) > -1) {
				String[] arrTmp = s.split(CommonConstant.COMMA) ;
				if(arrTmp.length == 2) {
					this.sortKey = Util.camelCaseToSnakeCase(arrTmp[0]) ;
					this.sortType = arrTmp[1] ;
				}
			}else {
				this.sortKey = s ;
			}
		}


		return sortKey ;
	}

	/**
	 * 정렬방법 가져오기
	 * @return
	 */
	public String getSortType() {

		if(StringUtils.isEmpty(sortKey) || (!CommonConstant.DESC.equals(sortType.toUpperCase()) && !CommonConstant.ASC.equals(sortType.toUpperCase()))) {
			return CommonConstant.DESC ;
		}else {
			return sortType ;
		}

	}
}
