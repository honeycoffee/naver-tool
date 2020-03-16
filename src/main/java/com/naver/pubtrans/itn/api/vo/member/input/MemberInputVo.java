package com.naver.pubtrans.itn.api.vo.member.input;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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

	@NotEmpty
	@Size(max = 30)
	private String userId;

	@NotEmpty
	@Size(max = 30)
	private String userName;

	@Size(min = 8, max = 20)
	private String userPw;

	@Size(max = 50)
	private String company;

}
