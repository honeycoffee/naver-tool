package com.naver.pubtrans.itn.api.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naver.pubtrans.itn.api.consts.URI;
import com.naver.pubtrans.itn.api.service.MemberService;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.member.MemberVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;

import lombok.extern.slf4j.Slf4j;


/**
 * 네이버 대중교통 DB 내재화 사용자관리 컨트롤러
 * 
 * @author westwind
 *
 */

@Slf4j
@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberSvc ;    
    
    /**
     * 회원 데이터 입력
     * <pre>
     * Valid를 이용하여 유효성 검사를 진행한다
     * </pre>
     * @param memberInputVo
     * @return
     * @throws Exception
     */
    @PostMapping(value = URI.MEMBER_REGISTER)
    public CommonOutput memberRegister(@RequestBody @Valid MemberInputVo memberInputVo) throws Exception {
    	
    	// 데이터 저장 서비스
    	memberSvc.setMember(memberInputVo, "ins");
    	
    	return new CommonOutput() ;
    	
    }  
    
    /**
     * ID 중복 체크
     * @param memberVo
     * @return
     * @throws Exception
     */
    @GetMapping(value = URI.MEMBER_DUPLICATE)
    public CommonOutput checkDuplicate(@RequestParam(name = "userId", required = true) String userId) throws Exception {
    	
    	// ID 중복 체크
    	CommonResult rs = memberSvc.checkDuplicate(userId);
    	
    	return new CommonOutput(rs) ;
    	
    }
	
    /**
     * 회원 데이터 상세 구조를 조회한다
     * @return
     * @throws Exception
     */
    @GetMapping(value = URI.MEMBER_DATA_STRUCTURE)
    public CommonOutput memberDataStructure() throws Exception {
    	
    	// 회원 데이터 상세 구조 조회
    	CommonResult rs = memberSvc.getMemberDataStructure() ;
    	
    	return new CommonOutput(rs) ;
    }
  
}
