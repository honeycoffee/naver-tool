package com.naver.pubtrans.itn.api.vo.member.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 목록 수 출력 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberCountOutputVo {
	/**
	 * 총 회원 수
	 */
	private int totalListCount;

	/**
	 * 총 관리자 수
	 */
	private int totalAdminCount;

	/**
	 * 총 일반 사용자 수
	 */
	private int totalUserCount;

	/**
	 * 총 승인 대기 중인 회원 수
	 */
	private int totalAnonymousCount;
}
