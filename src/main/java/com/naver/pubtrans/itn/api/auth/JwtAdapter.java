package com.naver.pubtrans.itn.api.auth;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.naver.pubtrans.itn.api.common.MemberUtil;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.exception.AccessTokenNotFoundException;
import com.naver.pubtrans.itn.api.service.MemberService;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

/**
 * JWT 관련 토큰을 생성하거나 검증한다.
 * @author adtec10
 *
 */
@Component
public class JwtAdapter {

	// JWT Key Header Name
	public static final String HEADER_NAME = "X-Pubtrans-Authorization";

	// 회원ID Claim value Key
	public static final String USER_ID = "userId";

	// 회원이름 Claim value Key
	public static final String USER_NAME = "userName";

	// 회원권한 Claim value Key
	public static final String AUTHORITIES = "authorities";

	// JWT Secret Key
	@Value("${security.jwt.secret-key}")
	private String SECRET_KEY;

	private final MemberService memberService;
	
	@Autowired
	JwtAdapter(MemberService memberService) {
		this.memberService = memberService;
	}

	/**
	 * 현재시간 기준에서 특정 시/일/월 만큼 더한다
	 * @param chronoUnit - 토큰 만료 기간 단위
	 * @param add - 단위에 더할 기간
	 * @return
	 */
	public Date getDateForExpire(ChronoUnit chronoUnit, int add) {
		LocalDateTime currentDateTime = LocalDateTime.now();

		currentDateTime = currentDateTime.plus(add, chronoUnit);

		return Util.localDateTimeToDate(currentDateTime);
	}

	/**
	 * 엑세스 토큰을 생성한다
	 * <pre>
	 *  - 24시간 토큰 생성 예 : createToken(memberOutputVo, memberAuthorityIdArray, ChronoUnit.HOURS, 24)
	 *  - 1주일 토큰 생성 예 : createToken(memberOutputVo, memberAuthorityIdArray, ChronoUnit.WEEKS, 1)
	 * </pre>
	 * @param memberOutputVo - 회원 데이터 출력 Vo
	 * @param memberAuthorityIdArray - 회원 권한 ID array
	 * @param chronoUnit - 토큰 만료 기간 단위
	 * @param add - 단위에 더할 기간
	 * @return
	 */
	public String createToken(MemberOutputVo memberOutputVo, String[] memberAuthorityIdArray, ChronoUnit chronoUnit, int add) {

		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
		
		String token = JWT.create()
			.withClaim(USER_ID, memberOutputVo.getUserId())
			.withClaim(USER_NAME, memberOutputVo.getUserName())
			.withArrayClaim(AUTHORITIES, memberAuthorityIdArray)
			.withExpiresAt(getDateForExpire(chronoUnit, add))
			.sign(algorithm);
		
		return token;
	}

	/**
	 * 토큰을 검증한다
	 * @param token - 검증이 필요한 토큰 (accessToken 또는 refreshToken)
	 * @return
	 * @throws JWTVerificationException
	 */
	public DecodedJWT validateToken(String token) throws JWTVerificationException, TokenExpiredException {
		DecodedJWT decodedJWT = null;
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
		decodedJWT = JWT.require(algorithm).build().verify(token);

		return decodedJWT;
	}

	/**
	 * 토큰에서 회원 정보를 추출한다
	 * @param token - 회원정보를 추출할 토큰 (accessToken 또는 refreshToken)
	 * @return
	 * @throws AccessTokenNotFoundException
	 * @throws TokenExpiredException
	 * @throws JWTDecodeException
	 * @throws JWTVerificationException
	 */
	public MemberOutputVo extractUserDataFromToken(String token)
		throws AccessTokenNotFoundException, TokenExpiredException, JWTDecodeException, JWTVerificationException {

		MemberOutputVo memberOutputVo = new MemberOutputVo();

		if (StringUtils.isEmpty(token)) {
			throw new AccessTokenNotFoundException(ResultCode.AUTH_TOKEN_EMPTY.getApiErrorCode(),
				ResultCode.AUTH_TOKEN_EMPTY.getDisplayMessage());
		} else {
			DecodedJWT decodedJWT = this.validateToken(token);
			memberOutputVo.setUserId(decodedJWT.getClaim(USER_ID).asString());
			memberOutputVo.setUserName(decodedJWT.getClaim(USER_NAME).asString());
		}

		return memberOutputVo;
	}

	/**
	 * httpServletRequest에서 JWT refresh token을 가져온다.
	 * @param request - HttpServletRequest Interface
	 * return
	 */
	public String getRefreshTokenFromHeader(HttpServletRequest request) {
		return request.getHeader(HEADER_NAME);
	}

	/**
	 * httpServletRequest에서 JWT access token을 가져온다.
	 * @param request - HttpServletRequest Interface
	 * return
	 */
	public String getAccessTokenFromHeader(HttpServletRequest request) {
		return request.getHeader(HEADER_NAME);
	}

	/**
	 * access token 또는 refresh token으로 회원 권한을 가져온다.
	 * @param token
	 * @return
	 * @throws AccessTokenNotFoundException 
	 */

	public Authentication getAuthentication(String token) throws AccessTokenNotFoundException {
		MemberOutputVo memberOutputVo = this.extractUserDataFromToken(token);

		UserDetails userDetails = memberService.loadUserByUsername(memberOutputVo.getUserId());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

}
