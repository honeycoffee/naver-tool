package com.naver.pubtrans.itn.api.vo.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchVo {

	// 요청 페이지 번호
	private int pageNo ;
	
	// 요청 페이지 목록 수
	private int listSize ;
	
	// 목록조회 시작 번호
	private int startLimit ;
	
	// 목록조회 갯수
	private int endLimit ;
}
