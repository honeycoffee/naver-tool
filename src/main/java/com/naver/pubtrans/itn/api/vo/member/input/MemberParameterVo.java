package com.naver.pubtrans.itn.api.vo.member.input;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 정보 파라미터 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberParameterVo {

	/**
	 * 회원 ID.
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Size(max = 30)
	private String userId;
	/**
	 * 비밀번호
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Size(min = 8, max = 20)
	private String userPw;

}
