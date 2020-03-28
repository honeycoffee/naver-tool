package com.naver.pubtrans.itn.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.filter.JwtAuthenticationFilter;
import com.naver.pubtrans.itn.api.handler.MemberAccessDeniedHandler;

/**
 * 보안관련 설정
 * @author adtec10
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private JwtAdapter jwtAdapter;
	
	private MemberAccessDeniedHandler memberAccessDeniedHandler;
	
	@Autowired
	WebSecurityConfig(JwtAdapter jwtAdapter, MemberAccessDeniedHandler memberAccessDeniedHandler){
		this.jwtAdapter = jwtAdapter;
		this.memberAccessDeniedHandler = memberAccessDeniedHandler;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
				.antMatchers("/**/login", "/**/duplicate/member")
					.permitAll() // 로그인 및 중복 회원 API는 누구나 접속가능
				.antMatchers(HttpMethod.POST, "/**/api/member")
					.permitAll(); // 회원가입 Post Method API는 누구나 접근가능
		
		http
			.authorizeRequests() // 권한관리 API는 관리자만 가능
				.antMatchers(HttpMethod.GET, "/**/list/member")
					.hasRole(CommonConstant.ADMIN) // 
				.antMatchers(HttpMethod.PUT, "/**/member")
					.hasRole(CommonConstant.ADMIN) // 
				.antMatchers(HttpMethod.DELETE, "/**/member")
					.hasRole(CommonConstant.ADMIN) // 
				.anyRequest() // 기타 API는 인증 받은 회원만 가능
					.authenticated()
			.and()
			.exceptionHandling() //접근 권한 에러 시 exception 처리
				.accessDeniedHandler(memberAccessDeniedHandler);
				
		 http
			.addFilterBefore(new JwtAuthenticationFilter(jwtAdapter), UsernamePasswordAuthenticationFilter.class);
		
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/docs/**");
	}

}
