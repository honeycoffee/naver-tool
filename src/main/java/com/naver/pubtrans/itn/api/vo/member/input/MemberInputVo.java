package com.naver.pubtrans.itn.api.vo.member.input;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 입력 값
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberInputVo {
	
	/**
	 * 회원 ID
	 */
	@NotEmpty
	@Size(max = 30)
	private String userId;
	
	/**
	 * 회원 이름
	 */
	@NotEmpty
	@Size(max = 30)
	private String userName;

	/**
	 * 회원 비밀번호 - 등록 시에는 사용할 비밀번호. 수정 시에는 변경할 새 비밀번호가 입력됨
	 * - 수정 시에는 비밀번호 변경을 안 할 경우 공백 데이터가 넘어와서 size min 설정하지 않음. 등록 시에는 별도로 검증
	 */
	@Size(max = 20)
	private String userPw;

	/**
	 * 현재 비밀번호 - 내 정보 수정할 때 검증에 쓰이는 비밀번호
	 */
	@Size(min = 8, max = 20)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String currentUserPw;

	/**
	 * 소속
	 */
	@Size(max = 50)
	private String company;

	/**
	 * 회원 권한 ID
	 */
	private String authid;

}
