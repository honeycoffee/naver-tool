package com.naver.pubtrans.itn.api.vo.auth;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 검증 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class LoginVo {
	
	@NotEmpty
	@Size(max=30)
	private String userId ;

	@NotEmpty
	@Size(min=8, max=20)
	private String userPw ;
	
}
