package com.naver.pubtrans.itn.api.vo.common.output;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonMeta {

	// 전체 목록 수 
	private long totalCount ;
	
	private long listCountPerPage ;
	
	// 전체 페이지 수
	private int totalPageCount ;
	
	// 현제 페이지 번호
	private int currentPage ;
	
	// 첫번째 페이지 여부
	private boolean firstPage ;
	
	// 마지막 페이지 여부
	private boolean lastPage ;
	
	
}
