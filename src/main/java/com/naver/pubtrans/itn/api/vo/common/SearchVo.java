package com.naver.pubtrans.itn.api.vo.common;

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

	// 목록조회 시작 번호
	private int startLimit ;

	// 조회 게시글 수(페이지당 게시글 노출 수)
	private int endLimit ;
}
