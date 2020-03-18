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
import com.naver.pubtrans.itn.api.service.MemberService;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberParameterVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;

/**
 * 사용자 관리 Test
 * @author westwind
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ApiUtils apiUtils;

	/**
	 * 회원등록 - 정상적으로 회원등록 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessInsertMember() throws Exception {

		// 회원정보가 존재하면 삭제
		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId("test");

		memberService.deleteTestMember(memberSearchVo);

		MemberInputVo memberInputVo = new MemberInputVo();
		memberInputVo.setUserId("test");
		memberInputVo.setUserName("test_name");
		memberInputVo.setUserPw("qwer1234");
		memberInputVo.setCompany("test_company");

		mockMvc.perform(post("/v1/ntool/api/member")
			.content(objectMapper.writeValueAsString(memberInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 회원등록 - 필수값 오류인 경우
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorInsertMember() throws Exception {

		MemberInputVo memberInputVo = new MemberInputVo();
		memberInputVo.setUserName("test_name");
		memberInputVo.setUserPw("qwer1234");
		memberInputVo.setCompany("test_company");

		mockMvc.perform(post("/v1/ntool/api/member")
			.content(objectMapper.writeValueAsString(memberInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is("[userId](은)는 " + ResultCode.PARAMETER_ERROR.getDisplayMessage())));
	}

	/**
	 * 회원 ID 중복 체크 - 중복된 ID가 없을 때
	 * @throws Exception
	 */
	@Test
	public void caseNotDuplicateUserId() throws Exception {

		mockMvc.perform(get("/v1/ntool/api/duplicate/member")
			.param("userId", "noDuplicateId")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data.duplicate", is(false)));
	}

	/**
	 * 회원 ID 중복 체크 - 중복된 ID가 존재할 때
	 * @throws Exception
	 */
	@Test
	public void caseDuplicateUserId() throws Exception {

		mockMvc.perform(get("/v1/ntool/api/duplicate/member")
			.param("userId", "test")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data.duplicate", is(true)));
	}

	/**
	 * 회원 비밀번호 검증 - 비밀번호가 일치할 때
	 * @throws Exception
	 */
	@Test
	public void caseMatchPassword() throws Exception {

		LinkedHashMap<String, String> tokenMap = apiUtils.getTokenMap();

		String accessToken = tokenMap.get("accessToken");

		MemberParameterVo memberParameterVo = new MemberParameterVo();
		memberParameterVo.setUserPw("qwer1234");

		mockMvc.perform(post("/v1/ntool/api/verify/password")
			.content(objectMapper.writeValueAsString(memberParameterVo))
			.header(JwtAdapter.HEADER_NAME, accessToken)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data.verify", is(true)));

	}

	/**
	 * 회원 비밀번호 검증 - 비밀번호가 일치하지 않을 때
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchPassword() throws Exception {

		LinkedHashMap<String, String> tokenMap = apiUtils.getTokenMap();
		String accessToken = tokenMap.get("accessToken");

		MemberParameterVo memberParameterVo = new MemberParameterVo();
		memberParameterVo.setUserPw("qwer12345");

		mockMvc.perform(post("/v1/ntool/api/verify/password")
			.content(objectMapper.writeValueAsString(memberParameterVo))
			.header(JwtAdapter.HEADER_NAME, accessToken)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data.verify", is(false)));

	}

	/**
	 * 자신의 정보 조회 - 정상적으로 조회 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessGetMe() throws Exception {

		LinkedHashMap<String, String> tokenMap = apiUtils.getTokenMap();
		String accessToken = tokenMap.get("accessToken");

		mockMvc.perform(get("/v1/ntool/api/me")
			.header(JwtAdapter.HEADER_NAME, accessToken)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(notNullValue())));

	}

	/**
	 * 자신의 정보 조회 - 만료된 토큰으로 조회 했을 때
	 * @throws Exception
	 */
	@Test
	public void caseExpiredAccessTokenGetMe() throws Exception {

		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzg1MDgzLCJ1c2VySWQiOiIzMzMzIn0.piLNmfoXMYZIh4_-k3Qut7mwqvkjvzItpVwZXJX5zBw";

		mockMvc.perform(get("/v1/ntool/api/me")
			.header(JwtAdapter.HEADER_NAME, accessToken)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.code", is(ResultCode.AUTH_TOKEN_EXPIRED.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.AUTH_TOKEN_EXPIRED.getDisplayMessage())));

	}

	/**
	 * 자신의 정보 수정 - 정상적으로 수정 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessUpdateMe() throws Exception {

		LinkedHashMap<String, String> tokenMap = apiUtils.getTokenMap();
		String accessToken = tokenMap.get("accessToken");

		MemberInputVo memberInputVo = new MemberInputVo();
		memberInputVo.setUserId("test");
		memberInputVo.setUserName("test_name");
		memberInputVo.setUserPw("qwer1234");
		memberInputVo.setCompany("test_company");

		mockMvc.perform(put("/v1/ntool/api/me")
			.header(JwtAdapter.HEADER_NAME, accessToken)
			.content(objectMapper.writeValueAsString(memberInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));

	}

	/**
	 * 자신의 정보 수정 - 필수값 오류인 경우
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorUpdateMe() throws Exception {

		LinkedHashMap<String, String> tokenMap = apiUtils.getTokenMap();
		String accessToken = tokenMap.get("accessToken");

		MemberInputVo memberInputVo = new MemberInputVo();
		memberInputVo.setUserName("test_name");
		memberInputVo.setUserPw("qwer1234");
		memberInputVo.setCompany("test_company");

		mockMvc.perform(put("/v1/ntool/api/me")
			.header(JwtAdapter.HEADER_NAME, accessToken)
			.content(objectMapper.writeValueAsString(memberInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is("[userId](은)는 " + ResultCode.PARAMETER_ERROR.getDisplayMessage())));

	}

	/**
	 * 자신의 정보 수정 - 만료된 토큰으로 수정 했을 때
	 * @throws Exception
	 */
	@Test
	public void caseExpiredAccessTokenUpdateMe() throws Exception {

		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzg1MDgzLCJ1c2VySWQiOiIzMzMzIn0.piLNmfoXMYZIh4_-k3Qut7mwqvkjvzItpVwZXJX5zBw";

		MemberInputVo memberInputVo = new MemberInputVo();
		memberInputVo.setUserId("test");
		memberInputVo.setUserName("test_name");
		memberInputVo.setUserPw("qwer1234");
		memberInputVo.setCompany("test_company");

		mockMvc.perform(put("/v1/ntool/api/me")
			.header(JwtAdapter.HEADER_NAME, accessToken)
			.content(objectMapper.writeValueAsString(memberInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.code", is(ResultCode.AUTH_TOKEN_EXPIRED.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.AUTH_TOKEN_EXPIRED.getDisplayMessage())));
	}

	/**
	 * 권한관리 회원 조회 - 정상적으로 조회 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessGetMember() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/member/{userId}", "test")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(notNullValue())));
	}

	/**
	 * 권한관리 회원 조회 - 존재하지 않는 회원 ID 조회 했을 때
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchGetMember() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/member/{userId}", "no_match_user")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.MEMBER_DATA_NULL.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.MEMBER_DATA_NULL.getDisplayMessage())));
	}

	/**
	 * 권한관리 회원 수정 - 정상적으로 수정 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessUpdateMember() throws Exception {
		MemberInputVo memberInputVo = new MemberInputVo();
		memberInputVo.setUserId("test");
		memberInputVo.setUserName("test_name");
		memberInputVo.setCompany("test_company");

		mockMvc.perform(put("/v1/ntool/api/member")
			.content(objectMapper.writeValueAsString(memberInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 권한관리 회원 수정 - 필수값 오류인 경우
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorUpdateMember() throws Exception {
		MemberInputVo memberInputVo = new MemberInputVo();
		memberInputVo.setUserName("test_name");
		memberInputVo.setUserPw("qwer1234");
		memberInputVo.setCompany("test_company");

		mockMvc.perform(put("/v1/ntool/api/member")
			.content(objectMapper.writeValueAsString(memberInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is("[userId](은)는 " + ResultCode.PARAMETER_ERROR.getDisplayMessage())));
	}

	/**
	 * 권한관리 회원 수정 - 없는 회원일 경우 데이터 저장 오류
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchUpdateMember() throws Exception {

		MemberInputVo memberInputVo = new MemberInputVo();
		memberInputVo.setUserId("no_match_user");
		memberInputVo.setUserName("test_name");
		memberInputVo.setUserPw("qwer1234");
		memberInputVo.setCompany("test_company");

		mockMvc.perform(put("/v1/ntool/api/member")
			.content(objectMapper.writeValueAsString(memberInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.UPDATE_FAIL.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.UPDATE_FAIL.getDisplayMessage())));
	}

	/**
	 * 권한관리 회원 삭제 - 정상적으로 삭제 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessDeleteMember() throws Exception {
		MemberParameterVo memberParameterVo = new MemberParameterVo();
		memberParameterVo.setUserId("test");

		mockMvc.perform(delete("/v1/ntool/api/member")
			.content(objectMapper.writeValueAsString(memberParameterVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 권한관리 회원 삭제 - 없는 회원일 경우 데이터 삭제 오류
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchDeleteMember() throws Exception {
		MemberParameterVo memberParameterVo = new MemberParameterVo();
		memberParameterVo.setUserId("no_match_user");

		mockMvc.perform(delete("/v1/ntool/api/member")
			.content(objectMapper.writeValueAsString(memberParameterVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.DELETE_FAIL.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.DELETE_FAIL.getDisplayMessage())));
	}

	/**
	 * 회원 목록 - 목록이 존재하는 경우
	 * @throws Exception
	 */
	@Test
	public void caseExistsMemberList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/member")
			.param("pageNo", "1")
			.param("listSize", "20")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(not(hasSize(0)))));
	}

	/**
	 * 회원 목록 - 목록이 존재하지 않는 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsMemberList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/member")
			.param("userName", "no_match_user")
			.param("pageNo", "1")
			.param("listSize", "20")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(hasSize(0))));
	}

	/**
	 * 회원 테이블 스키마 조회
	 * @throws Exception
	 */
	@Test
	public void caseMemberSchema() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/schema/member")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.schema", is(not(hasSize(0)))));
	}

}