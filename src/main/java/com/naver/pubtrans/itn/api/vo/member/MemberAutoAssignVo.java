package com.naver.pubtrans.itn.api.vo.member;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 작업 자동할당 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberAutoAssignVo{

	/**
	 * 권한 ID 
	 */
	private String authorityId;

	/**
	 * 권한 이름
	 */
	private String authorityName;

	/**
	 * 권한 설명
	 */
	private String authorityMemo;


}
