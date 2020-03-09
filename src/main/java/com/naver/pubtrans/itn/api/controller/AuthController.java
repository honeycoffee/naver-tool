package com.naver.pubtrans.itn.api.controller;



import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naver.pubtrans.itn.api.common.MemberPasswordEncoder;
import com.naver.pubtrans.itn.api.service.AuthService;
import com.naver.pubtrans.itn.api.vo.auth.LoginVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;

import lombok.extern.slf4j.Slf4j;


/**
 * 네이버 대중교통 DB 내재화 인증관리 컨트롤러
 * 
 * @author westwind
 *
 */
@Slf4j
@RestController
public class AuthController {
	
	@Autowired
	private AuthService authSvc ;   
	
    /**
     * 로그인을 처리한다.
     * <pre>
     * Valid를 이용하여 유효성 검사를 진행한다
     * </pre>
     * @param loginVo - 로그인 입력 Vo
     * @param httpServletRequest - HttpServletRequest Interface
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/v1/ntool/api/auth/login")
    public CommonOutput login(@Valid LoginVo loginVo, HttpServletRequest request) throws Exception {

    	CommonResult commonResult = authSvc.loginMember(loginVo, request);
    	
    	return new CommonOutput(commonResult) ;
    	
    }  
    
    /**
     * JWT refresh token으로 access token 갱신
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/v1/ntool/api/auth/refresh/token")
    public CommonOutput refresh(HttpServletRequest request) throws Exception {
    	
    	CommonResult commonResult = authSvc.refreshToken(request);
    	
    	return new CommonOutput(commonResult) ;
    	
    }
  
}
