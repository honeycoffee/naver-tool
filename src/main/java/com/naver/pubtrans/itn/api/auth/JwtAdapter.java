package com.naver.pubtrans.itn.api.auth;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.exception.AccessTokenNotFoundException;

@Component
public class JwtAdapter {
	
	// JWT Key Header Name
	public final String HEADER_NAME = "pubtrans-X-Authorization" ;
	
	//
	private String secretKey = "pubtrans-a9163ef3ad71";

	
	/**
	 * 현재시간 기준에서 특정 시/일/월 만큼 더한다
	 * @param calUnit
	 * @param add
	 * @return
	 */
	public Date getDateForExpire(int calUnit, int add) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(calUnit, add);
		
		return cal.getTime();
    }
	
	/**
	 * 엑세스 토큰을 생성한다
	 * <pre>
	 *  - 24시간 토큰 생성 예 : createToken(userId, Calendar.HOUR, 24)
	 *  - 1주일 토큰 생성 예 : createToken(userId, Calendar.WEEK_OF_MONTH, 1)
	 * </pre>
	 * @param userId - 사용자 ID
	 * @param calUnit - Calendar unit
	 * @param add - 길이
	 * @return
	 */
	public String createToken(String userId, int calUnit, int add) {
		
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		
		String token = JWT.create()
		        .withClaim("userId", userId)
		        .withExpiresAt(getDateForExpire(calUnit, add))
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
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
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
		
		if(Util.isEmpty(token)) {
			throw new AccessTokenNotFoundException("AccessToken is empty") ;
		}else {
			DecodedJWT jwt = this.validateToken(token) ;
			userId = jwt.getClaim("userId").asString() ;
		}
		
		return userId ;
	}
	
}
