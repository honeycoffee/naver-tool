package com.naver.pubtrans.itn.api.vo.member.input;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;

/**
 * 회원 정보 검색조건 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberSearchVo extends SearchVo {

	/**
	 * 회원 ID.
	 */
	private String userId;

	/**
	 * 회원 이름
	 */
	private String userName;

	/**
	 * 회원 권한 ID
	 */
	private String authId;

}
