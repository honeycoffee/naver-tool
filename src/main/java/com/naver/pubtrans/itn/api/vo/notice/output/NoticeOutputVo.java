package com.naver.pubtrans.itn.api.vo.notice.output;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * 공지사항 출력 Vo
 * 
 * @author westwind
 *
 */
@Getter
@Setter
public class NoticeOutputVo {

	/**
	 * 공지사항 ID
	 */
	private int seq;

	/**
	 * 제목
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String title;

	/**
	 * 내용
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String content;

	/**
	 * 중요여부
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String importantYn;

	/**
	 * 등록자 ID
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String regUserId;

	/**
	 * 등록자 명
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String regUserName;

	/**
	 * 등록일
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String regDate;

}
