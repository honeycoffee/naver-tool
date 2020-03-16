package com.naver.pubtrans.itn.api.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.vo.auth.LoginVo;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

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
			.andExpect(jsonPath("$.code", is(200)))
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
			.andExpect(jsonPath("$.code", is(506)));
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

		mockMvc.perform(post("/v1/ntool/api/auth/refresh/token")
			.header(JwtAdapter.HEADER_NAME, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RfdXNlciIsImV4cCI6MTU4NDQxNjE4NCwidXNlcklkIjoidGVzdCJ9.waTStr6XBn56fgTP6QKojXfw2u-6MZAfiFoBP-kEnB8")
			.content(objectMapper.writeValueAsString(loginVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(200)))
			.andExpect(jsonPath("$.result.data.accessToken", is(notNullValue())))
			.andExpect(jsonPath("$.result.data.refreshToken", is(notNullValue())));
	}
	//
	//	/**
	//	 * 회원 로그인 rest docs 생성
	//	 * @throws Exception
	//	 */
	//	@Test
	//	public void login() throws Exception {
	//
	//		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();
	//
	//		// 회원 정보
	//		MemberOutputVo memberOutputVo = new MemberOutputVo();
	//
	//		memberOutputVo.setUserId("1111");
	//		memberOutputVo.setUserName("테스트유저");
	//		memberOutputVo.setUserPw("$2a$10$VtdsXJoEdcw22nI6uMlAA..QjfuAK.kd8WC8U0W0pWBw6xCuLfVly");
	//		memberOutputVo.setCompany("소속");
	//		memberOutputVo.setRegDate("2020-02-27 16:35:02");
	//		memberOutputVo.setUpdDate("2020-02-27 16:35:02");
	//
	//		// API 호출 토큰 및 갱신 토큰
	//		AuthOutputVo authOutputVo = new AuthOutputVo();
	//
	//		String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzg1MDgzLCJ1c2VySWQiOiIzMzMzIn0.piLNmfoXMYZIh4_-k3Qut7mwqvkjvzItpVwZXJX5zBw";
	//		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzQ1NDg0LCJ1c2VySWQiOiIzMzMzIn0.ueSpRBHHgi5qPJlJl7AAkj9URMGDE6HK78HKT1rfa10";
	//
	//		authOutputVo.setAccessToken(accessToken);
	//		authOutputVo.setRefreshToken(refreshToken);
	//
	//		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authOutputVo);
	//
	//		LoginVo loginVo = new LoginVo();
	//
	//		loginVo.setUserId("test");
	//		loginVo.setUserPw("qwer1234");
	//
	//		//given
	//		given(authService.loginMember(any(LoginVo.class), any(HttpServletRequest.class)))
	//			.willReturn(commonResult);
	//
	//		//when
	//		ResultActions result = this.mockMvc.perform(
	//			post("/v1/ntool/api/auth/login")
	//				.content(objectMapper.writeValueAsString(loginVo))
	//				.contentType(MediaType.APPLICATION_JSON)
	//				.accept(MediaType.APPLICATION_JSON)
	//				.characterEncoding("UTF-8"));
	//
	//		//then
	//		result.andExpect(status().isOk())
	//			.andDo(document("auth/login",
	//				getDocumentRequest(),
	//				getDocumentResponse(),
	//				requestFields(
	//					fieldWithPath("userId").type(JsonFieldType.STRING).description("회원ID"),
	//					fieldWithPath("userPw").type(JsonFieldType.STRING).description("비밀번호")
	//
	//				),
	//				responseFields(
	//					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	//					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	//					fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
	//					fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
	//					fieldWithPath("result.data.accessToken").type(JsonFieldType.STRING).description("API 인증 토큰"),
	//					fieldWithPath("result.data.refreshToken").type(JsonFieldType.STRING)
	//						.description("API 인증 토큰 갱신 용 토큰"))));
	//	}
	//
	//	/**
	//	 * API 호출 토큰 갱신 rest docs 생성
	//	 * @throws Exception
	//	 */
	//	@Test
	//	public void refresh() throws Exception {
	//
	//		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();
	//
	//		String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzg1MDgzLCJ1c2VySWQiOiIzMzMzIn0.piLNmfoXMYZIh4_-k3Qut7mwqvkjvzItpVwZXJX5zBw";
	//		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzQ1NDg0LCJ1c2VySWQiOiIzMzMzIn0.ueSpRBHHgi5qPJlJl7AAkj9URMGDE6HK78HKT1rfa10";
	//
	//		// API 호출 토큰 및 갱신 토큰
	//		AuthOutputVo authOutputVo = new AuthOutputVo();
	//
	//		authOutputVo.setAccessToken(accessToken);
	//		authOutputVo.setRefreshToken(refreshToken);
	//
	//		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authOutputVo);
	//
	//		//given
	//		given(authService.refreshToken(any(HttpServletRequest.class)))
	//			.willReturn(commonResult);
	//
	//		//when
	//		ResultActions result = this.mockMvc.perform(
	//			post("/v1/ntool/api/auth/refresh/token")
	//				.header(JwtAdapter.HEADER_NAME, refreshToken)
	//				.contentType(MediaType.APPLICATION_JSON)
	//				.accept(MediaType.APPLICATION_JSON)
	//				.characterEncoding("UTF-8"));
	//
	//		//then
	//		result.andExpect(status().isOk())
	//			.andDo(document("auth/refreshToken",
	//				getDocumentRequest(),
	//				getDocumentResponse(),
	//				responseFields(
	//					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	//					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	//					fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
	//					fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
	//					fieldWithPath("result.data.accessToken").type(JsonFieldType.STRING).description("API 인증 토큰"),
	//					fieldWithPath("result.data.refreshToken").type(JsonFieldType.STRING)
	//						.description("API 인증 토큰 갱신 용 토큰"))));
	//	}

}
