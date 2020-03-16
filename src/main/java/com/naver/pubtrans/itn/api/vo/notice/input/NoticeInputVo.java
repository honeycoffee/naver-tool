package com.naver.pubtrans.itn.api.vo.notice.input;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * 공지사항 입력 값
 * 
 * @author westwind
 *
 */
@Getter
@Setter
public class NoticeInputVo {

	/**
	 * 공지사항 ID
	 */
	private int seq;

	/**
	 * 제목
	 */
	@NotEmpty
	@Size(max = 100)
	private String title;

	/**
	 * 내용
	 */
	private String content;

	/**
	 * 중요여부
	 */
	@NotEmpty
	private String importantYn;

	/**
	 * 등록자 ID
	 */
	private String regUserId;

	/**
	 * 등록자명
	 */
	private String regUserName;

}
