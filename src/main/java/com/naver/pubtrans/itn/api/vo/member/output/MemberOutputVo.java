package com.naver.pubtrans.itn.api.vo.member.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 데이터 출력 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberOutputVo {

	private String userId ;
	
	private String userName ;
	
	private String userPw ;
	
	private String company ;
	
	private String updDate ;
	
	private String regDate ;
	
}
