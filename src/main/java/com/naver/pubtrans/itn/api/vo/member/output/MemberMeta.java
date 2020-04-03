package com.naver.pubtrans.itn.api.vo.member.output;


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

	// 총 관리자 수
	private int totalAdminCount;

	// 총 일반 사용자 수
	private int totalUserCount;

	// 총 승인 대기중인 회원 수
	private int totalAnonymousCount;

}
