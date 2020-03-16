package com.naver.pubtrans.itn.api.vo.notice.input;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;

/**
 * 공지사항 검색 조건
 * 
 * @author westwind
 *
 */
@Getter
@Setter
public class NoticeSearchVo extends SearchVo {

	/**
	 * 공지사항 ID
	 */
	private int seq;

	/**
	 * 검색 조건
	 */
	private String searchType;

	/**
	 * 검색 키워드
	 */
	private String searchKeyword;
}
