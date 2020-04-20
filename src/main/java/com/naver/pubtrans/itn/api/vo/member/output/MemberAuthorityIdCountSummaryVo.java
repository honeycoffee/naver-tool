package com.naver.pubtrans.itn.api.vo.member.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 총 카운트 및 역할 별 카운트 출력 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberAuthorityIdCountSummaryVo {
	/**
	 * 총 회원 수
	 */
	private int totalRoleCount;

	/**
	 * 총 관리자 수
	 */
	private int totalAdminRoleCount;

	/**
	 * 총 일반 사용자 수
	 */
	private int totalUserRoleCount;

	/**
	 * 총 승인 대기 중인 회원 수
	 */
	private int totalUnauthorizedRoleCount;
}
