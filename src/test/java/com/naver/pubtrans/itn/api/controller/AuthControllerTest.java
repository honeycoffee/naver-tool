package com.naver.pubtrans.itn.api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.ApiUtils;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.vo.auth.LoginVo;

/**
 * 사용자 인증 관리 Test
 * @author westwind
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ApiUtils apiUtils;

	/**
	 * 로그인 - 정상적으로 로그인 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessLogin() throws Exception {

		LoginVo loginVo = new LoginVo();
		loginVo.setUserId("test");
		loginVo.setUserPw("qwer1234");

		mockMvc.perform(post("/v1/ntool/api/auth/login")
			.content(objectMapper.writeValueAsString(loginVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data.accessToken", is(notNullValue())))
			.andExpect(jsonPath("$.result.data.refreshToken", is(notNullValue())));
	}

	/**
	 * 로그인 - 회원 정보가 없을 때
	 * @throws Exception
	 */
	@Test
	public void caseNotExistMember() throws Exception {

		LoginVo loginVo = new LoginVo();
		loginVo.setUserId("no_user");
		loginVo.setUserPw("qwer1234");

		mockMvc.perform(post("/v1/ntool/api/auth/login")
			.content(objectMapper.writeValueAsString(loginVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.MEMBER_DATA_NULL.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.MEMBER_DATA_NULL.getDisplayMessage())));
	}

	/**
	 * 로그인 - 비밀번호가 일치 하지 않을 때 
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchPassword() throws Exception {

		LoginVo loginVo = new LoginVo();
		loginVo.setUserId("test");
		loginVo.setUserPw("qwer12345");

		mockMvc.perform(post("/v1/ntool/api/auth/login")
			.content(objectMapper.writeValueAsString(loginVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.PASSWORD_NOT_MATCH.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.PASSWORD_NOT_MATCH.getDisplayMessage())));
	}

	/**
	 * AccessToken 갱신 - 정상적으로 토큰이 갱신 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessRefreshToken() throws Exception {

		LoginVo loginVo = new LoginVo();
		loginVo.setUserId("test");
		loginVo.setUserPw("qwer1234");

		LinkedHashMap<String, String> tokenMap = apiUtils.getTokenMap();

		String refreshToken = tokenMap.get("refreshToken");

		mockMvc.perform(post("/v1/ntool/api/auth/refresh/token")
			.header(JwtAdapter.HEADER_NAME, refreshToken)
			.content(objectMapper.writeValueAsString(loginVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data.accessToken", is(notNullValue())))
			.andExpect(jsonPath("$.result.data.refreshToken", is(notNullValue())));
	}

	/**
	 * AccessToken 갱신 - refreshToken 이 만료 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseExpiredRefreshToken() throws Exception {

		// DB에 존재하는 테스트용 refreshToken
		String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6Iu2FjOyKpO2KuF_snbTrpoQiLCJleHAiOjE1ODM4NDcxMjEsInVzZXJJZCI6Ijg4ODgifQ.-N2WO3xBYBtUGZsn8Vd4c3pTcE4JuU10FNS2C3YHTkc";

		mockMvc.perform(post("/v1/ntool/api/auth/refresh/token")
			.header(JwtAdapter.HEADER_NAME, refreshToken)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.code", is(ResultCode.AUTH_TOKEN_EXPIRED.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.AUTH_TOKEN_EXPIRED.getDisplayMessage())));
	}

	/**
	 * AccessToken 갱신 - 일치하는 refreshToken 이 없을 때
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchRefreshToken() throws Exception {

		String refreshToken = "";

		mockMvc.perform(post("/v1/ntool/api/auth/refresh/token")
			.header(JwtAdapter.HEADER_NAME, refreshToken)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.code", is(ResultCode.AUTH_TOKEN_EMPTY.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.AUTH_TOKEN_EMPTY.getDisplayMessage())));
	}

}
