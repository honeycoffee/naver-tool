package com.naver.pubtrans.itn.api.vo.member.output;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 권한 데이터 출력 Vo
 * @author westwind
 *
 */
@Getter
@Setter
public class MemberAuthOutputVo implements UserDetails{

	/**
	 * 회원 ID
	 */
	private String userId;

	/**
	 * 회원 이름
	 * - UserDetails override 하여 cameCase 미적용 
	 */
	private String username;

	/**
	 * 소속
	 */
	private String company;

	/**
	 * UserDetails override 변수
	 */
	private Collection<? extends GrantedAuthority> authorities;
    private boolean isEnabled = true;
    private String password;
    private boolean isCredentialsNonExpired = true;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;

	@JsonIgnore
	/**
	 * 권한 ID 
	 */
	private String authId;

	@JsonIgnore
	/**
	 * 권한 이름
	 */
	private String authName;

}
