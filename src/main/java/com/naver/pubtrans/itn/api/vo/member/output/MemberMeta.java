package com.naver.pubtrans.itn.api.vo.member.output;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.common.output.CommonMeta;

/**
 * 회원 리스트 메타정보를 정의
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberMeta extends CommonMeta{
	
	// 전체 목록 수
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private long totalListCount;

	// 전체 회원 수
	private long totalRoleCount;

	//페이지당 목록 수
	private long listCountPerPage;

	// 전체 페이지 수
	private int totalPageCount;

	// 현제 페이지 번호
	private int currentPage;

	// 첫번째 페이지 여부
	private boolean firstPage;

	// 마지막 페이지 여부
	private boolean lastPage;

	// 총 관리자 수
	private int totalAdminRoleCount;

	// 총 일반 사용자 수
	private int totalUserRoleCount;

	// 총 승인 대기중인 회원 수
	private int totalUnauthorizedRoleCount;

}
