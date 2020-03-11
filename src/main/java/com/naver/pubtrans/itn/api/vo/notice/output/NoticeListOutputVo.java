package com.naver.pubtrans.itn.api.vo.notice.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 목록 결과 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class NoticeListOutputVo {

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
	 * 가입일
	 */
	private String regDate;

}
