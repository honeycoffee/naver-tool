package com.naver.pubtrans.itn.api.controller;



import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.service.MemberService;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

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
	private MemberService memberService ;   
	
	@Autowired
	private OutputFmtUtil outputFmtUtil ;
	
	@Autowired
	private JwtAdapter jwtAdapter ;    
    
    /**
     * 회원 정보를 등록한다.
     * <pre>
     * Valid를 이용하여 유효성 검사를 진행한다
     * </pre>
     * @param memberInputVo - 회원 정보 입력값
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/v1/ntool/api/register/member")
    public CommonOutput registerMember(@RequestBody @Valid MemberInputVo memberInputVo, BindingResult bindingResult) throws Exception {
    	
    	// 비밀번호 입력값 체크
    	if(StringUtils.isEmpty(memberInputVo.getUserPw())) {
    		bindingResult.addError(new FieldError("MemberInputVo", "userPw", "반드시 값이 존재하고 최소값 8과 최대값 20 사이의 크기이어야 합니다."));
    		throw new MethodArgumentNotValidException(null, bindingResult) ;
    	}
    	
    	// 데이터 저장 서비스
    	memberService.insertMember(memberInputVo);
    	
    	return new CommonOutput() ;
    	
    }  
    
    /**
     * 회원 ID 중복 체크
     * @param userId - 체크할 회원 ID
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/v1/ntool/api/duplicate/member")
    public CommonOutput checkDuplicate(@RequestParam(name = "userId", required = true) String userId) throws Exception {
    	
    	// ID 중복 체크
    	CommonResult commonResult = memberService.checkDuplicate(userId);
    	
    	return new CommonOutput(commonResult) ;
    	
    }
    
    /**
     * 회원 비밀번호 검증
     * @param userPw - 현재 회원 비밀번호
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/v1/ntool/api/verify/password")
    public CommonOutput verifyPassword(@RequestParam(name = "userPw", required = true) String userPw) throws Exception {
    	
    	// TODO : Spring Security AccessToken 적용은 Sprint4에 작업 예정으로 test를 위해 만료기한 3개월의 accessToken 임시 사용 (userId : test, userPw : qwer1234)
    	String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RfdXNlciIsImV4cCI6MTU5MTgzMTM2NCwidXNlcklkIjoidGVzdCJ9.J3jWR6IDJU6Ly_okU-T3F8lSQXC9tpgbX6TSH7R8hHo";
    	
    	MemberOutputVo memberOutputVo = jwtAdapter.getUserDataByToken(accessToken);
    	
    	MemberSearchVo memberSearchVo = new MemberSearchVo();
    	
    	memberSearchVo.setUserId(memberOutputVo.getUserId());
    	
    	// ID 중복 체크
    	CommonResult commonResult = memberService.verifyPassword(memberSearchVo, userPw);
    	
    	return new CommonOutput(commonResult) ;
    	
    }
    
    /**
     * 자신의 정보를 조회한다.
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/v1/ntool/api/info/me")
    public CommonOutput infoMe() throws Exception {
    	
    	// TODO : Spring Security AccessToken 적용은 Sprint4에 작업 예정으로 test를 위해 만료기한 3개월의 accessToken 임시 사용 (userId : test, userPw : qwer1234)
    	String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RfdXNlciIsImV4cCI6MTU5MTgzMTM2NCwidXNlcklkIjoidGVzdCJ9.J3jWR6IDJU6Ly_okU-T3F8lSQXC9tpgbX6TSH7R8hHo";
    	
    	MemberOutputVo memberOutputVo = jwtAdapter.getUserDataByToken(accessToken);
    	
    	MemberSearchVo memberSearchVo = new MemberSearchVo();
    	
    	memberSearchVo.setUserId(memberOutputVo.getUserId());
    	
    	// 데이터 저장 서비스
    	CommonResult commonResult = outputFmtUtil.setCommonDocFmt(memberService.selectMemberSchema(), memberService.getMemberData(memberSearchVo)) ;
    	
    	return new CommonOutput(commonResult) ;
    	
    }  
    
    /**
     * 자신의 정보를 수정한다.
     * <pre>
     * Valid를 이용하여 유효성 검사를 진행한다
     * </pre>
     * @param memberInputVo - 회원 정보 입력값
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/v1/ntool/api/modify/me")
    public CommonOutput modifyMe(@RequestBody @Valid MemberInputVo memberInputVo) throws Exception {

    	// TODO : Spring Security AccessToken 적용은 Sprint4에 작업 예정으로 test를 위해 만료기한 3개월의 accessToken 임시 사용 (userId : test, userPw : qwer1234)
    	String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RfdXNlciIsImV4cCI6MTU5MTgzMTM2NCwidXNlcklkIjoidGVzdCJ9.J3jWR6IDJU6Ly_okU-T3F8lSQXC9tpgbX6TSH7R8hHo";
    	
    	MemberOutputVo memberOutputVo = jwtAdapter.getUserDataByToken(accessToken);
    	
    	if(!memberOutputVo.getUserId().equals(memberInputVo.getUserId())) {
    		throw new ApiException("accessToken과 내 정보가 일치하지 않습니다.") ;
    	}else {// 데이터 저장 서비스
        	memberService.updateMember(memberInputVo);	
    	}
    	
    	return new CommonOutput() ;
    	
    }  
	
    /**
     * 권한관리에서 회원 정보를 조회한다
     * @param userId - 체크할 회원 ID
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/v1/ntool/api/info/member/{userId}")
    public CommonOutput infoMember(@PathVariable String userId) throws Exception {
    	
    	MemberSearchVo memberSearchVo = new MemberSearchVo();
    	
    	memberSearchVo.setUserId(userId);
    	
    	// 회원 데이터 상세 구조 조회
    	CommonResult commonResult = outputFmtUtil.setCommonDocFmt(memberService.selectMemberSchema(), memberService.getMemberData(memberSearchVo)) ;
    	
    	return new CommonOutput(commonResult) ;
    	
    }
    
    /**
     * 권한관리에서 회원 정보를 수정한다.
     * <pre>
     * Valid를 이용하여 유효성 검사를 진행한다
     * </pre>
     * @param memberInputVo - 회원 정보 입력 값
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/v1/ntool/api/modify/member")
    public CommonOutput modifyMember(@RequestBody @Valid MemberInputVo memberInputVo) throws Exception {
    	    	
    	// 데이터 저장 서비스
    	memberService.updateMember(memberInputVo);
    	
    	return new CommonOutput() ;
    	
    }  
    
    /**
     * 회원 정보를 삭제한다.
     * @param userId - 회원 ID
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/v1/ntool/api/remove/member")
    public CommonOutput removeMember(@RequestParam(name = "userId", required = true) String userId) throws Exception {
    	
    	MemberSearchVo memberSearchVo = new MemberSearchVo();
    	
    	memberSearchVo.setUserId(userId);
    	
    	// ID 중복 체크
    	memberService.deleteMember(memberSearchVo);
    	
    	return new CommonOutput() ;
    	
    }


	/**
	 * 회원 목록 조회
	 * @param memberSearchVo - 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/member")
	public CommonOutput listMember(MemberSearchVo memberSearchVo) throws Exception {
		CommonResult commonResult = memberService.selectMemberList(memberSearchVo) ;
		return new CommonOutput(commonResult) ;
	}
	
    /**
     * 회원 데이터 상세 구조를 조회한다
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/v1/ntool/api/schema/member")
    public CommonOutput selectMemberSchema() throws Exception {
    	
    	// 회원 데이터 상세 구조 조회
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(memberService.selectMemberSchema()) ;
    	
    	return new CommonOutput(commonResult) ;
    }
  
}
