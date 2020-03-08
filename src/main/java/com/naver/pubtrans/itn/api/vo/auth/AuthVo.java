package com.naver.pubtrans.itn.api.vo.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 인증 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class AuthVo{

	private String refreshToken ;
	
	private String accessToken ;
	
}
