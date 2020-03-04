package com.naver.pubtrans.itn.api.controller;



import java.time.temporal.ChronoUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.service.MemberService;
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
	private MemberService memberSvc ;   
	
	@Autowired
	private JwtAdapter jwtAdapter ;    
    
    /**
     * 로그인을 처리한다.
     * <pre>
     * Valid를 이용하여 유효성 검사를 진행한다
     * </pre>
     * @param loginVo - 입력값
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/v1/ntool/api/auth/login")
    public CommonOutput login(@RequestBody @Valid LoginVo loginVo) throws Exception {
    	
    	System.out.println(jwtAdapter.createToken("test", ChronoUnit.HOURS, 1));
    	
System.out.println(jwtAdapter.createToken("test1", ChronoUnit.SECONDS, 5));
    	
    	CommonResult commonResult = memberSvc.checkDuplicate(loginVo.getUserId());
    	
    	// 데이터 저장 서비스
//    	memberSvc.insertMember(memberInputVo);
    	
    	return new CommonOutput(commonResult) ;
    	
    }  
    
    /**
     * 
     * @param userId - 체크할 회원 ID
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/v1/ntool/api/auth/token")
    public CommonOutput token(@RequestParam(name = "userId", required = true) String userId) throws Exception {
    	
    	System.out.println(userId);
    	System.out.println(jwtAdapter.validateToken(userId));
    	
    	System.out.println(jwtAdapter.getUserIdByToken(userId));
    	
    	// ID 중복 체크
    	CommonResult commonResult = memberSvc.checkDuplicate(userId);
    	
    	return new CommonOutput(commonResult) ;
    	
    }
  
}
