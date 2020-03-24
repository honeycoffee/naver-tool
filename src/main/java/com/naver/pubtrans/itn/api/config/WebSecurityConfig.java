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
import com.naver.pubtrans.itn.api.filter.JwtAuthenticationFilter;

/**
 * 보안관련 설정
 * @author adtec10
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private JwtAdapter jwtAdapter;
	
	@Autowired
	WebSecurityConfig(JwtAdapter jwtAdapter){
		this.jwtAdapter = jwtAdapter;
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
					.permitAll() // 로그인 API는 누구나 접속가능
				.antMatchers(HttpMethod.POST, "/**/api/member")
					.permitAll(); // 회원가입 Post Method API는 누구나 접근가능
		
				http
				.authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
					.antMatchers(HttpMethod.GET, "/**/member")
						.hasRole("ADMIN")
					.anyRequest()  
						.authenticated()
				.and()
					.exceptionHandling() //접근 권한 에러 시 exception 처리
						.accessDeniedPage("/v1/ntool/api/exception/403");
				
		 http
			.addFilterBefore(new JwtAuthenticationFilter(jwtAdapter), UsernamePasswordAuthenticationFilter.class);
		
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/docs/**");
	}

}
