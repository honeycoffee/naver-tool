package com.naver.pubtrans.itn.api.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

import com.naver.pubtrans.itn.api.consts.ResultCode;

/**
 * 네이버 대중교통 DB 내재화 예외처리 컨트롤러
 * 
 * @author westwind
 *
 */
@Slf4j
@Controller
public class ExceptionController {

	/**
	 * Access Denied Exception을 처리한다.
	 * @throws Exception
	 */
	@RequestMapping(value = "/v1/ntool/api/exception/403")
	public void accessDenied() throws Exception {
		
		throw new AccessDeniedException(ResultCode.ACCESS_DENIED.getDisplayMessage());

	}

}
