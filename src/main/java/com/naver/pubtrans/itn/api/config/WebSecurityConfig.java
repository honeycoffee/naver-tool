package com.naver.pubtrans.itn.api.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 보안관련 설정
 * @author adtec10
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.httpBasic()
		;
	}

	@Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/docs/**");
    }

}
