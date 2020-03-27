package com.naver.pubtrans.itn.api.controller.document;

import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.controller.AuthenticationController;
import com.naver.pubtrans.itn.api.handler.MemberAccessDeniedHandler;
import com.naver.pubtrans.itn.api.service.AuthenticationService;
import com.naver.pubtrans.itn.api.vo.auth.LoginVo;
import com.naver.pubtrans.itn.api.vo.auth.output.AuthenticationOutputVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

/**
 * 사용자 인증 관리 Rest Doc 생성 Test Class
 * @author westwind
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerDocumentTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthenticationService authenticationService;

	@MockBean
	private JwtAdapter jwtAdapter;

	@MockBean
	private MemberAccessDeniedHandler memberAccessDeniedHandler;
	
	/**
	 * 회원 로그인 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void login() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();

		// 회원 정보
		MemberOutputVo memberOutputVo = new MemberOutputVo();

		memberOutputVo.setUserId("1111");
		memberOutputVo.setUserName("테스트유저");
		memberOutputVo.setEncodedUserPw("$2a$10$VtdsXJoEdcw22nI6uMlAA..QjfuAK.kd8WC8U0W0pWBw6xCuLfVly");
		memberOutputVo.setCompany("소속");
		memberOutputVo.setRegDate("2020-02-27 16:35:02");
		memberOutputVo.setUpdDate("2020-02-27 16:35:02");

		// API 호출 토큰 및 갱신 토큰
		AuthenticationOutputVo authenticationOutputVo = new AuthenticationOutputVo();

		String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzg1MDgzLCJ1c2VySWQiOiIzMzMzIn0.piLNmfoXMYZIh4_-k3Qut7mwqvkjvzItpVwZXJX5zBw";
		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzQ1NDg0LCJ1c2VySWQiOiIzMzMzIn0.ueSpRBHHgi5qPJlJl7AAkj9URMGDE6HK78HKT1rfa10";

		authenticationOutputVo.setAccessToken(accessToken);
		authenticationOutputVo.setRefreshToken(refreshToken);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authenticationOutputVo);

		LoginVo loginVo = new LoginVo();

		loginVo.setUserId("test");
		loginVo.setUserPw("qwer1234");

		//given
		given(authenticationService.loginMember(any(LoginVo.class), any(HttpServletRequest.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			post("/v1/ntool/api/auth/login")
				.content(objectMapper.writeValueAsString(loginVo))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("auth/login",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					fieldWithPath("userId").type(JsonFieldType.STRING).description("[필수]회원ID"),
					fieldWithPath("userPw").type(JsonFieldType.STRING).description("[필수]비밀번호")

				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
					fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
					fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
					fieldWithPath("result.data.accessToken").type(JsonFieldType.STRING).description("API 인증 토큰"),
					fieldWithPath("result.data.refreshToken").type(JsonFieldType.STRING)
						.description("API 인증 토큰 갱신 용 토큰"))));
	}
	
	/**
	 * 회원 로그아웃 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void logout() throws Exception {
		
		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzQ1NDg0LCJ1c2VySWQiOiIzMzMzIn0.ueSpRBHHgi5qPJlJl7AAkj9URMGDE6HK78HKT1rfa10";

		//when
		ResultActions result = this.mockMvc.perform(
			post("/v1/ntool/api/auth/logout")
				.header(JwtAdapter.HEADER_NAME, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("auth/logout",
				getDocumentRequest(),
				getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"))));
	}

	/**
	 * API 호출 토큰 갱신 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void refresh() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();

		String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzg1MDgzLCJ1c2VySWQiOiIzMzMzIn0.piLNmfoXMYZIh4_-k3Qut7mwqvkjvzItpVwZXJX5zBw";
		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzQ1NDg0LCJ1c2VySWQiOiIzMzMzIn0.ueSpRBHHgi5qPJlJl7AAkj9URMGDE6HK78HKT1rfa10";

		// API 호출 토큰 및 갱신 토큰
		AuthenticationOutputVo authenticationOutputVo = new AuthenticationOutputVo();

		authenticationOutputVo.setAccessToken(accessToken);
		authenticationOutputVo.setRefreshToken(refreshToken);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authenticationOutputVo);
		
		//given
		given(authenticationService.updateAccessTokenByRefreshToken(any(MockHttpServletRequest.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			post("/v1/ntool/api/auth/refresh/token")
				.header(JwtAdapter.HEADER_NAME, refreshToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("auth/refreshAccessToken",
				getDocumentRequest(),
				getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
					fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
					fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
					fieldWithPath("result.data.accessToken").type(JsonFieldType.STRING).description("API 인증 토큰"),
					fieldWithPath("result.data.refreshToken").type(JsonFieldType.STRING)
						.description("API 인증 토큰 갱신 용 토큰"))));
	}

}
