package com.naver.pubtrans.itn.api.vo.member.output;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 데이터 출력 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberOutputVo {
	/**
	 * 회원 ID
	 */
	private String userId;

	/**
	 * 회원 이름
	 */
	private String userName;

	/**
	 * 소속
	 */
	private String company;

	@JsonIgnore
	/**
	 * 인코딩된 비밀번호
	 */
	private String encodedUserPw;

	/**
	 * 가입일
	 */
	private String regDate;

	@JsonIgnore
	/**
	 * 수정일
	 */
	private String updDate;

}
