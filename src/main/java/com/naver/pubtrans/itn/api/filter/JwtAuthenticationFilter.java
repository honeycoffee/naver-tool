package com.naver.pubtrans.itn.api.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.exception.AccessTokenNotFoundException;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;

/**
 * JWT 권한 검증을 위한 필터
 * 
 * @author westwind
 *
 */
public class JwtAuthenticationFilter extends GenericFilterBean {

	private JwtAdapter jwtAdapter;

	@Autowired
	public JwtAuthenticationFilter(JwtAdapter jwtAdapter) {
		this.jwtAdapter = jwtAdapter;
	}

	/**
	 * JwtToken 유효성 검증을 수행하는 필터를 chain에 등록
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		
		ObjectMapper objectMapper = new ObjectMapper();

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		response.setCharacterEncoding(CommonConstant.CHARACTER_ENCODING_UTF_8);
		response.setContentType(CommonConstant.CONTENT_TYPE_APPLICATION_JSON);

		try {
			
			String requestURI = request.getRequestURI();
			String token = "";
			
			if(StringUtils.contains(requestURI, CommonConstant.REFRESH_TOKEN_API_URI)) {
				token = jwtAdapter.getRefreshTokenFromHeader(request);
				
				request.setAttribute(CommonConstant.REFRESH_TOKEN_KEY, token);
			}else {
				token = jwtAdapter.getAccessTokenFromHeader(request);

				request.setAttribute(CommonConstant.ACCESS_TOKEN_KEY, token);
			}
			
			if (token != null) {
				Authentication auth = jwtAdapter.getAuthentication(token);

				SecurityContextHolder.getContext().setAuthentication(auth);
			}

			chain.doFilter(request, response);

		} catch (AccessTokenNotFoundException accessTokenNotFoundException) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			PrintWriter printWriter = response.getWriter();
			CommonOutput commonOutput = new CommonOutput(ResultCode.AUTH_TOKEN_EMPTY.getApiErrorCode(),
					ResultCode.AUTH_TOKEN_EMPTY.getDisplayMessage());
			printWriter.println(objectMapper.writeValueAsString(commonOutput));
			printWriter.close();
		} catch (TokenExpiredException tokenExpiredException) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			PrintWriter printWriter = response.getWriter();
			CommonOutput commonOutput = new CommonOutput(ResultCode.AUTH_TOKEN_EXPIRED.getApiErrorCode(),
					ResultCode.AUTH_TOKEN_EXPIRED.getDisplayMessage());
			printWriter.println(objectMapper.writeValueAsString(commonOutput));
			printWriter.close();
		} catch (JWTDecodeException jWTDecodeException) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			PrintWriter printWriter = response.getWriter();
			CommonOutput commonOutput = new CommonOutput(ResultCode.AUTH_TOKEN_DECODE_ERROR.getApiErrorCode(),
					ResultCode.AUTH_TOKEN_DECODE_ERROR.getDisplayMessage());
			printWriter.println(objectMapper.writeValueAsString(commonOutput));
			printWriter.close();
		} catch (JWTVerificationException jWTVerificationException) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			PrintWriter printWriter = response.getWriter();
			CommonOutput commonOutput = new CommonOutput(ResultCode.AUTH_TOKEN_VALID_ERROR.getApiErrorCode(),
					ResultCode.AUTH_TOKEN_VALID_ERROR.getDisplayMessage());
			printWriter.println(objectMapper.writeValueAsString(commonOutput));
			printWriter.close();
		} catch (Exception exception) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			PrintWriter printWriter = response.getWriter();
			CommonOutput commonOutput = new CommonOutput(ResultCode.AUTH_TOKEN_EMPTY.getApiErrorCode(),
					ResultCode.AUTH_TOKEN_EMPTY.getDisplayMessage());
			printWriter.println(objectMapper.writeValueAsString(commonOutput));
			printWriter.close();
		}

	}

}