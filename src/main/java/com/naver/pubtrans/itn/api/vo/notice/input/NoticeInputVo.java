package com.naver.pubtrans.itn.api.vo.notice.input;

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
public class NoticeInputVo {

	/**
	 * 회원 ID.
	 */
	@NotEmpty
	@Size(max = 30)
	private String userId;

	/**
	 * 이름
	 */
	@NotEmpty
	@Size(max = 30)
	private String userName;

	/**
	 * 비밀번호
	 */
	@Size(min = 8, max = 20)
	private String userPw;

	/**
	 * 소속
	 */
	@Size(max = 50)
	private String company;

}
