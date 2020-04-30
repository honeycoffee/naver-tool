package com.naver.pubtrans.itn.api.vo.member.input;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 수정 시 입력 값
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberUpdateVo {
	
	/**
	 * 회원 ID
	 */
	@NotEmpty
	@Size(max = 30)
	private String userId;

	/**
	 * 회원 권한 ID
	 */
	@NotEmpty
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String authorityId;

	/**
	 * 대중교통 구분
	 */
	private String pubTransType;

	/**
	 * 도시코드
	 */
	private List<Integer> cityCodes;
	
	/**
	 * 할당 타입
	 */
	private String taskAssignType;
	
	/**
	 * 등록자 ID
	 */
	private String regUserId;
	
	/**
	 * 등록자 이름
	 */
	private String regUserName;

}
