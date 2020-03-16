package com.naver.pubtrans.itn.api.vo.notice.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * 공지사항 파라미터 Vo
 * 
 * @author westwind
 *
 */
@Getter
@Setter
public class NoticeParameterVo {

	/**
	 * 공지사항 ID
	 */
	@NotNull
	private int seq;
}
