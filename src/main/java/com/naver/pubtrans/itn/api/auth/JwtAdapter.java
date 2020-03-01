package com.naver.pubtrans.itn.api.auth;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.exception.AccessTokenNotFoundException;

/**
 * JWT 관련 토큰을 생성하거나 검증한다.
 * @author adtec10
 *
 */
@Component
public class JwtAdapter {

	// JWT Key Header Name
	public static final String HEADER_NAME = "X-Pubtrans-Authorization" ;

	// 회원ID Claim value Key
	public static final String USER_ID = "userId" ;

	// JWT Secret Key
	@Value("${security.jwt.secret-key}")
	private String SECRET_KEY ;


	/**
	 * 현재시간 기준에서 특정 시/일/월 만큼 더한다
	 * @param calUnit
	 * @param add
	 * @return
	 */
	public Date getDateForExpire(ChronoUnit unit, int add) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		currentDateTime.plus(add, unit) ;

		return Util.localDateTimeToDate(currentDateTime) ;
    }

	/**
	 * 엑세스 토큰을 생성한다
	 * <pre>
	 *  - 24시간 토큰 생성 예 : createToken(userId, ChronoUnit.HOURS, 24)
	 *  - 1주일 토큰 생성 예 : createToken(userId, ChronoUnit.WEEKS, 1)
	 * </pre>
	 * @param userId - 사용자 ID
	 * @param unit - ChronoUnit
	 * @param add - 길이
	 * @return
	 */
	public String createToken(String userId, ChronoUnit unit, int add) {

		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

		String token = JWT.create()
		        .withClaim(USER_ID, userId)
		        .withExpiresAt(getDateForExpire(unit, add))
		        .sign(algorithm);

		return token ;
	}


	/**
	 * 토큰을 검증한다
	 * @param token
	 * @return
	 * @throws JWTVerificationException
	 */
	public DecodedJWT validateToken(String token) throws JWTVerificationException{
        DecodedJWT jwt = null;
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        jwt = JWT.require(algorithm).build().verify(token);

        return jwt;
    }


	/**
	 * 토큰에서 회원ID를 추출한다
	 * @param token
	 * @return
	 * @throws AccessTokenNotFoundException
	 * @throws TokenExpiredException
	 * @throws JWTDecodeException
	 * @throws JWTVerificationException
	 */
	public String getUserIdByToken(String token) throws AccessTokenNotFoundException, TokenExpiredException, JWTDecodeException, JWTVerificationException {

		String userId = "" ;

		if(StringUtils.isEmpty(token)) {
			throw new AccessTokenNotFoundException("AccessToken is empty") ;
		}else {
			DecodedJWT jwt = this.validateToken(token) ;
			userId = jwt.getClaim(USER_ID).asString() ;
		}

		return userId ;
	}

}
