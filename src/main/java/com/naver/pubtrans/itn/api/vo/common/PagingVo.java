package com.naver.pubtrans.itn.api.vo.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 전체 페이징 정보를 정의하는 VO
 * @author adtec10
 *
 */
@Getter
@Setter
public class PagingVo {

	// 전체 목록 수
	private long totalListCount;

	// 전페 페이지 번호
	private int totalPageCount;

	// 현제 페이지 번호
	private int currentPage;

	// 페이지당 목록 수
	private int listCntPerPage;

	// 조회 시작번호
	private int startPageLimit;

	// 조회 목록 수
	private int endPageLimit;



	public PagingVo(int totalCount) {

		int currentPage = 1;
		int listCntPerPage = 20;

		new PagingVo(totalCount, currentPage, listCntPerPage);
	}


	public PagingVo(int totalListCount, int currentPage, int listCntPerPage) {


		if(currentPage == 0) currentPage = 1;
		if(listCntPerPage == 0) listCntPerPage = 20;


		this.totalListCount = totalListCount;
		this.currentPage = currentPage;
		this.listCntPerPage = listCntPerPage;

		this.totalPageCount = (int) Math.ceil((double) totalListCount / listCntPerPage);

		this.startPageLimit = (currentPage-1) * listCntPerPage;
		this.endPageLimit = listCntPerPage;

	}



	public boolean isFirstPage() {
		if(this.currentPage <= 1)
			return true;
		else
			return false;
	}

	public boolean isLastPage() {
		if(totalPageCount == currentPage)
			return true;
		else
			return false;
	}



}
