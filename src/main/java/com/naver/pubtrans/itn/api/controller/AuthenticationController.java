package com.naver.pubtrans.itn.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.service.AuthenticationService;
import com.naver.pubtrans.itn.api.vo.auth.LoginVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.member.output.MemberAuthorityOutputVo;

/**
 * 네이버 대중교통 DB 내재화 인증관리 컨트롤러
 * 
 * @author westwind
 *
 */
@Slf4j
@RestController
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@Autowired
	AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * 로그인을 처리한다.
	 * <pre>
	 * Valid를 이용하여 유효성 검사를 진행한다
	 * </pre>
	 * @param loginVo - 로그인 입력 Vo
	 * @param request - HttpServletRequest Interface
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/v1/ntool/api/auth/login")
	public CommonOutput login(@RequestBody @Valid LoginVo loginVo, HttpServletRequest request) throws Exception {

		CommonResult commonResult = authenticationService.loginMember(loginVo, request);

		return new CommonOutput(commonResult);

	}

	/**
	 * 로그아웃을 처리한다.
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/v1/ntool/api/auth/logout")
	public CommonOutput logout() throws Exception {
		
		authenticationService.logoutMember();
		
		return new CommonOutput();

	}

	/**
	 * JWT refresh token으로 access token 갱신
	 * @param request - HttpServletRequest Interface
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/v1/ntool/api/auth/refresh/token")
	public CommonOutput refresh(HttpServletRequest request) throws Exception {
		
		CommonResult commonResult = authenticationService.updateAccessTokenByRefreshToken(request);
		
		return new CommonOutput(commonResult);

	}

}
