package com.naver.pubtrans.itn.api.vo.notice.input;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;

@Getter
@Setter
public class NoticeSearchVo extends SearchVo {

	/**
	 * 회원 ID.
	 */
	private String userId;

	/**
	 * 회원 이름
	 */
	private String userName;

}
