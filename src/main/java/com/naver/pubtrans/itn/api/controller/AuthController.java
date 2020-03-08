package com.naver.pubtrans.itn.api.controller;



import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.MemberPasswordEncoder;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.service.MemberService;
import com.naver.pubtrans.itn.api.vo.auth.LoginVo;
import com.naver.pubtrans.itn.api.vo.auth.input.AuthInputVo;
import com.naver.pubtrans.itn.api.vo.auth.output.AuthOutputVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

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
	private OutputFmtUtil outputFmtUtil ;
	
	@Autowired
	private MemberService memberSvc ;   
	
	@Autowired
	private JwtAdapter jwtAdapter ;    
	
	@Autowired
	private MemberPasswordEncoder memberPasswordEncoder;
    
    /**
     * 로그인을 처리한다.
     * <pre>
     * Valid를 이용하여 유효성 검사를 진행한다
     * </pre>
     * @param loginVo - 입력값
     * @param httpServletRequest - HttpServletRequest Interface
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/v1/ntool/api/auth/login")
    public CommonOutput login(@RequestBody @Valid LoginVo loginVo, HttpServletRequest httpServletRequest) throws Exception {

    	AuthInputVo authInputVo = new AuthInputVo();
    	AuthOutputVo authOutputVo = new AuthOutputVo();
    	MemberSearchVo memberSearchVo = new MemberSearchVo();
    	
    	memberSearchVo.setUserId(loginVo.getUserId());
    	
    	MemberOutputVo memberOutputVo = memberSvc.getMemberData(memberSearchVo);
    	
    	if(memberOutputVo == null) {
    		throw new ApiException("회원정보가 존재하지 않습니다.");
    	}else{
    		authInputVo.setUserId(memberOutputVo.getUserId());
    		authInputVo.setAccessIp(Util.getClientIpAddress(httpServletRequest));
    		    		
    		if(!memberPasswordEncoder.matches(loginVo.getUserPw(), memberOutputVo.getUserPw())){
    			authInputVo.setLoginSuccessYn(CommonConstant.N);

        		memberSvc.insertMemberLoginLog(authInputVo);
        		
	    		throw new ApiException("비밀번호가 일치하지 않습니다.");
	    	}else{
	    		authInputVo.setLoginSuccessYn(CommonConstant.Y);
	    		
	    		String accessToken = jwtAdapter.createToken(memberOutputVo, ChronoUnit.HOURS, 1);
	    		String refreshToken = jwtAdapter.createToken(memberOutputVo, ChronoUnit.SECONDS, 1);
	        	
	    		authInputVo.setRefreshToken(refreshToken);

	    		memberSvc.insertMemberLoginLog(authInputVo);
	    		memberSvc.deleteMemberTokenInfo(authInputVo);
	    		memberSvc.insertMemberTokenInfo(authInputVo);
	    		
	    		authOutputVo.setAccessToken(accessToken);
	    		authOutputVo.setRefreshToken(refreshToken);
	    	}
    	}
    	
    	CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authOutputVo);
    	
    	return new CommonOutput(commonResult) ;
    	
    }  
    
    /**
     * JWT refresh token으로 access token 갱신
     * @param userId - 체크할 회원 ID
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/v1/ntool/api/auth/token")
    public CommonOutput token(@RequestParam(name = "userId", required = true) String userId) throws Exception {
    	
    	AuthInputVo authInputVo = new AuthInputVo();
    	
    	authInputVo.setUserId(userId);
    	
    	AuthOutputVo authOutputVo = memberSvc.getMemberTokenInfo(authInputVo);
    	
    	if(authOutputVo == null) {
    		throw new ApiException("회원정보가 존재하지 않습니다.");
    	}else{
    		
    	}
    	}
    	try {

        	jwtAdapter.validateToken(authOutputVo.getRefreshToken());
    	}catch(JWTVerificationException e) {
    		System.out.println("afsasfasf");
    	}
    	
//    	System.out.println(jwtAdapter.getUserIdByToken(userId));
    	
    	// ID 중복 체크
    	CommonResult commonResult = memberSvc.checkDuplicate(userId);
    	
    	return new CommonOutput(commonResult) ;
    	
    }
  
}
