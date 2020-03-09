package com.naver.pubtrans.itn.api.service;

import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.MemberPasswordEncoder;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.exception.AccessTokenNotFoundException;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.AuthRepository;
import com.naver.pubtrans.itn.api.vo.auth.LoginVo;
import com.naver.pubtrans.itn.api.vo.auth.input.AuthInputVo;
import com.naver.pubtrans.itn.api.vo.auth.output.AuthOutputVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

import lombok.RequiredArgsConstructor;

/**
 * 네이버 대중교통 내재화 인증관리 서비스
 * @author westwind
 *
 */
@Service
@RequiredArgsConstructor
public class AuthService {
	
	@Autowired
	private OutputFmtUtil outputFmtUtil ;

	@Autowired
	private AuthRepository authRepository ;  
	
	@Autowired
	private MemberService memberSvc ;   
	
	@Autowired
	private JwtAdapter jwtAdapter ;    
	
	@Autowired
	private MemberPasswordEncoder memberPasswordEncoder; 
		
	/**
	 * 회원 로그인 기록을 저장한다.
	 * @param authInputVo - 회원 로그인 인증 입력 Vo
	 */
	public void insertMemberLoginLog(AuthInputVo authInputVo) {
		
		authRepository.insertMemberLoginLog(authInputVo);
	}
	
	/**
	 * 회원 Token 정보를 저장한다.
	 * @param authInputVo - 회원 로그인 인증 입력 Vo
	 */
	public void insertMemberTokenInfo(AuthInputVo authInputVo) {
		

		authRepository.insertMemberTokenInfo(authInputVo);
	}

	/**
	 * 회원 Token 정보를 가져온다.
	 * @param authInputVo - 회원 로그인 인증 입력 Vo
	 * @return
	 */
	public AuthOutputVo getMemberTokenInfo(AuthInputVo authInputVo) {
		
		AuthOutputVo authOutPutVo = authRepository.selectMemberTokenInfo(authInputVo);
		
		return authOutPutVo;
	}

	/**
	 * 회원 Token 정보를 삭제한다.
	 * @param authInputVo - 회원 로그인 인증 입력 Vo
	 */
	public void deleteMemberTokenInfo(AuthInputVo authInputVo) {
		
		authRepository.deleteMemberTokenInfo(authInputVo);
	}

	/**
	 * AccessToken을 생성한다.
	 * @param refreshToken - 인증 갱신 토큰
	 * @return
	 * @throws AccessTokenNotFoundException 
	 * @throws JWTVerificationException 
	 * @throws JWTDecodeException 
	 * @throws TokenExpiredException 
	 */
	public String createAccessToken(String refreshToken) throws TokenExpiredException, JWTDecodeException, JWTVerificationException, AccessTokenNotFoundException {
		
		MemberOutputVo memberOutputVo = jwtAdapter.getUserDataByToken(refreshToken);
		
		String accessToken = jwtAdapter.createToken(memberOutputVo, ChronoUnit.HOURS, 1);
		
		return accessToken;
	}
	
	
	/**
	 * 로그인을 처리한다.
     * @param loginVo - 로그인 입력 Vo
     * @param request - HttpServletRequest Interface
	 * @return
	 * @throws Exception 
	 */
	public CommonResult loginMember(LoginVo loginVo, HttpServletRequest request) throws Exception {
		
		AuthInputVo authInputVo = new AuthInputVo();
    	AuthOutputVo authOutputVo = new AuthOutputVo();
    	MemberSearchVo memberSearchVo = new MemberSearchVo();
    	
    	memberSearchVo.setUserId(loginVo.getUserId());
    	
    	// 로그인 정보로 회원 데이터 조회
    	MemberOutputVo memberOutputVo = memberSvc.getMemberData(memberSearchVo);
    	
    	if(memberOutputVo == null) {
    		throw new ApiException("회원정보가 존재하지 않습니다.");
    	}else{
    		authInputVo.setUserId(memberOutputVo.getUserId());
    		authInputVo.setAccessIp(Util.getClientIpAddress(request));
    		    		
    		if(!memberPasswordEncoder.matches(loginVo.getUserPw(), memberOutputVo.getUserPw())){
    			authInputVo.setLoginSuccessYn(CommonConstant.N);

    			this.insertMemberLoginLog(authInputVo);
        		
	    		throw new ApiException("비밀번호가 일치하지 않습니다.");
	    	}else{
	    		authInputVo.setLoginSuccessYn(CommonConstant.Y);
	    		
	    		String refreshToken = jwtAdapter.createToken(memberOutputVo, ChronoUnit.HOURS, 12);
	    		String accessToken = this.createAccessToken(refreshToken);
	        	
	    		authInputVo.setRefreshToken(refreshToken);

	    		this.insertMemberLoginLog(authInputVo);
	    		this.deleteMemberTokenInfo(authInputVo);
	    		this.insertMemberTokenInfo(authInputVo);
	    		
	    		authOutputVo.setAccessToken(accessToken);
	    		authOutputVo.setRefreshToken(refreshToken);
	    	}
    	}
    	
    	CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authOutputVo);
		
		return commonResult ;
	}
	
	
	/**
	 * JWT refresh_token으로 access_token을 갱신한다.
     * @param request - HttpServletRequest Interface
	 * @return
	 * @throws Exception 
	 */
	public CommonResult refreshToken(HttpServletRequest request) throws Exception {
    	
    	AuthInputVo authInputVo = new AuthInputVo();
    	
    	String refreshToken = request.getHeader(JwtAdapter.HEADER_NAME);
    	
    	authInputVo.setRefreshToken(refreshToken);
    	
    	AuthOutputVo authOutputVo = this.getMemberTokenInfo(authInputVo);
    	
    	if(authOutputVo == null ) {
    		throw new ApiException("존재하지 않는 refreshToken 입니다.");
    	}else{
    		jwtAdapter.validateToken(authOutputVo.getRefreshToken());
    		
    		String accessToken = this.createAccessToken(refreshToken);
    		
    		authOutputVo.setAccessToken(accessToken);
    		authOutputVo.setRefreshToken(authOutputVo.getRefreshToken());
    	}
    	
    	CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authOutputVo);
		
		return commonResult ;
	}
	
	
}
