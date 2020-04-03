package com.naver.pubtrans.itn.api.service;

import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.MemberPasswordEncoder;
import com.naver.pubtrans.itn.api.common.MemberUtil;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.exception.AccessTokenNotFoundException;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.AuthenticationRepository;
import com.naver.pubtrans.itn.api.vo.auth.LoginVo;
import com.naver.pubtrans.itn.api.vo.auth.input.AuthenticationInputVo;
import com.naver.pubtrans.itn.api.vo.auth.output.AuthenticationOutputVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

/**
 * 네이버 대중교통 내재화 인증관리 서비스
 * @author westwind
 *
 */
@Service
public class AuthenticationService {

	private final OutputFmtUtil outputFmtUtil;

	private final MemberService memberService;

	private final JwtAdapter jwtAdapter;

	private final AuthenticationRepository authenticationRepository;

	private final MemberPasswordEncoder memberPasswordEncoder;

	@Autowired
	AuthenticationService(OutputFmtUtil outputFmtUtil, MemberService memberService, JwtAdapter jwtAdapter,
		AuthenticationRepository authenticationRepository, MemberPasswordEncoder memberPasswordEncoder) {
		this.outputFmtUtil = outputFmtUtil;
		this.memberService = memberService;
		this.jwtAdapter = jwtAdapter;
		this.authenticationRepository = authenticationRepository;
		this.memberPasswordEncoder = memberPasswordEncoder;
	}

	/**
	 * 회원 로그인 기록을 저장한다.
	 * @param authenticationInputVo - 회원 로그인 인증 입력 Vo
	 */
	public void insertMemberLoginLog(AuthenticationInputVo authenticationInputVo) {

		authenticationRepository.insertMemberLoginLog(authenticationInputVo);
	}

	/**
	 * 회원 Token 정보를 저장한다.
	 * @param authenticationInputVo - 회원 로그인 인증 입력 Vo
	 */
	public void insertMemberTokenInfo(AuthenticationInputVo authenticationInputVo) {

		authenticationRepository.insertMemberTokenInfo(authenticationInputVo);
	}

	/**
	 * 회원 Token 정보를 가져온다.
	 * @param authenticationInputVo - 회원 로그인 인증 입력 Vo
	 * @return
	 */
	public AuthenticationOutputVo getMemberTokenInfo(AuthenticationInputVo authenticationInputVo) {

		AuthenticationOutputVo authenticationOutputVo = authenticationRepository
			.getMemberTokenInfo(authenticationInputVo);

		return authenticationOutputVo;
	}

	/**
	 * 회원 Token 정보를 삭제한다.
	 * @param authenticationInputVo - 회원 로그인 인증 입력 Vo
	 */
	public void deleteMemberTokenInfo(AuthenticationInputVo authenticationInputVo) {

		authenticationRepository.deleteMemberTokenInfo(authenticationInputVo);
	}

	/**
	 * AccessToken을 생성한다.
	 * @param refreshToken - 인증 갱신 토큰
	 * @return
	 * @throws AccessTokenNotFoundException
	 * @throws JWTVerificationException
	 * @throws JWTDecodeException
	 * @throws TokenExpiredException
	 * @throws ApiException
	 */
	public String createAccessToken(String refreshToken)
		throws TokenExpiredException, JWTDecodeException, JWTVerificationException, AccessTokenNotFoundException,
		ApiException {

		MemberOutputVo memberOutputVo = jwtAdapter.extractUserDataFromToken(refreshToken);

		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId(memberOutputVo.getUserId());

		String[] memberAuthorityIdArray = memberService.getMemberAuthorityIdArray(memberSearchVo);

		if (memberAuthorityIdArray == null) {
			throw new ApiException(ResultCode.MEMBER_AUTHORITY_NULL.getApiErrorCode(),
				ResultCode.MEMBER_AUTHORITY_NULL.getDisplayMessage());
		}

		String accessToken = jwtAdapter.createToken(memberOutputVo, memberAuthorityIdArray, ChronoUnit.SECONDS,
			CommonConstant.ACCESS_TOKEN_EXPIRE_TIME_SEC);

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

		AuthenticationInputVo authenticationInputVo = new AuthenticationInputVo();
		AuthenticationOutputVo authenticationOutputVo = new AuthenticationOutputVo();
		MemberSearchVo memberSearchVo = new MemberSearchVo();

		memberSearchVo.setUserId(loginVo.getUserId());

		// 로그인 정보로 회원 데이터 조회
		MemberOutputVo memberOutputVo = memberService.getMember(memberSearchVo);

		if (memberOutputVo == null) {
			throw new ApiException(ResultCode.MEMBER_DATA_NULL.getApiErrorCode(),
				ResultCode.MEMBER_DATA_NULL.getDisplayMessage());
		}

		authenticationInputVo.setUserId(memberOutputVo.getUserId());
		authenticationInputVo.setAccessIp(Util.getClientIpAddress(request));

		// 입력한 비밀번호와 DB에 있는 인코딩된 비밀번호 비교
		if (!memberPasswordEncoder.matches(loginVo.getUserPw(), memberOutputVo.getEncodedUserPw())) {
			authenticationInputVo.setLoginSuccessYn(CommonConstant.N);

			this.insertMemberLoginLog(authenticationInputVo);

			throw new ApiException(ResultCode.PASSWORD_NOT_MATCH.getApiErrorCode(),
				ResultCode.PASSWORD_NOT_MATCH.getDisplayMessage());
		}

		authenticationInputVo.setLoginSuccessYn(CommonConstant.Y);

		String[] memberAuthorityIdArray = memberService.getMemberAuthorityIdArray(memberSearchVo);
		if (memberAuthorityIdArray == null) {
			throw new ApiException(ResultCode.MEMBER_AUTHORITY_NULL.getApiErrorCode(),
				ResultCode.MEMBER_AUTHORITY_NULL.getDisplayMessage());
		}
		String refreshToken = jwtAdapter.createToken(memberOutputVo, memberAuthorityIdArray, ChronoUnit.SECONDS,
			CommonConstant.REFRESH_TOKEN_EXPIRE_TIME_SEC);
		String accessToken = this.createAccessToken(refreshToken);

		authenticationInputVo.setRefreshToken(refreshToken);

		this.insertMemberLoginLog(authenticationInputVo);
		this.deleteMemberTokenInfo(authenticationInputVo);
		this.insertMemberTokenInfo(authenticationInputVo);

		authenticationOutputVo.setAccessToken(accessToken);
		authenticationOutputVo.setRefreshToken(refreshToken);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authenticationOutputVo);

		return commonResult;
	}

	/**
	 * 로그아웃을 처리한다.
	 * @return
	 * @throws Exception
	 */
	public void logoutMember() throws Exception {
		AuthenticationInputVo authenticationInputVo = new AuthenticationInputVo();
		authenticationInputVo.setUserId(MemberUtil.getMemberFromAccessToken().getUserId());

		this.deleteMemberTokenInfo(authenticationInputVo);
	}

	/**
	 * JWT refresh_token으로 access_token을 갱신한다.
	 * @param request - HttpServletRequest Interface
	 * @return
	 * @throws Exception
	 */
	public CommonResult updateAccessTokenByRefreshToken(HttpServletRequest request) throws Exception {

		AuthenticationInputVo authenticationInputVo = new AuthenticationInputVo();

		String refreshToken = (String)request.getAttribute(CommonConstant.REFRESH_TOKEN_KEY);

		if (StringUtils.isEmpty(refreshToken)) {
			throw new AccessTokenNotFoundException(ResultCode.AUTH_TOKEN_EMPTY.getApiErrorCode(),
				ResultCode.AUTH_TOKEN_EMPTY.getDisplayMessage());
		}

		authenticationInputVo.setRefreshToken(refreshToken);

		AuthenticationOutputVo authenticationOutputVo = this.getMemberTokenInfo(authenticationInputVo);

		if (authenticationOutputVo == null) {
			throw new AccessTokenNotFoundException(ResultCode.AUTH_TOKEN_EMPTY.getApiErrorCode(),
				ResultCode.AUTH_TOKEN_EMPTY.getDisplayMessage());
		}

		String accessToken = this.createAccessToken(refreshToken);

		authenticationOutputVo.setAccessToken(accessToken);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authenticationOutputVo);

		return commonResult;
	}

}
