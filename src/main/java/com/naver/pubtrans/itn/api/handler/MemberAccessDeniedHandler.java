package com.naver.pubtrans.itn.api.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;

@Component
public class MemberAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setCharacterEncoding(CommonConstant.CHARACTER_ENCODING_UTF_8);
		response.setContentType(CommonConstant.CONTENT_TYPE_APPLICATION_JSON);
		
		PrintWriter printWriter = response.getWriter();
		CommonOutput commonOutput = new CommonOutput(ResultCode.ACCESS_DENIED.getApiErrorCode(),
			ResultCode.ACCESS_DENIED.getDisplayMessage());
		printWriter.println(objectMapper.writeValueAsString(commonOutput));
		printWriter.close();
	}

}