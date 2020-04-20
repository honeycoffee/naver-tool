package com.naver.pubtrans.itn.api.vo.member.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

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

	/**
	 * 인코딩된 비밀번호
	 */
	@JsonIgnore
	private String encodedUserPw;

	/**
	 * 가입일
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String regDate;

	/**
	 * 수정일
	 */
	@JsonIgnore
	private String updDate;

	/**
	 * 권한 ID 
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String authorityId;

	/**
	 * 권한 이름 
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String authorityName;

}
