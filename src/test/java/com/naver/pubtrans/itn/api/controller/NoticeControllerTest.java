package com.naver.pubtrans.itn.api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedHashMap;

import org.junit.Before;
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
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeInputVo;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeParameterVo;

/**
 * 공지사항 관리 Test
 * @author westwind
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NoticeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private ApiUtils apiUtils;
	
	private LinkedHashMap<String, String> tokenMap;

	@Before
	public void setup() throws Exception {
		//Api Test Utils 초기화
		apiUtils = new ApiUtils(mockMvc, objectMapper);
		
		tokenMap = apiUtils.getTokenMap();
	}

	/**
	 * 공지사항등록 - 정상적으로 공지사항 등록 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessInsertNotice() throws Exception {

		NoticeInputVo noticeInputVo = new NoticeInputVo();
		noticeInputVo.setTitle("test notice");
		noticeInputVo.setContent("test content");
		noticeInputVo.setImportantYn("Y");

		mockMvc.perform(post("/v1/ntool/api/notice")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.content(objectMapper.writeValueAsString(noticeInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(notNullValue())));
	}

	/**
	 * 공지사항 등록 - 필수값 오류인 경우
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorInsertNotice() throws Exception {

		NoticeInputVo noticeInputVo = new NoticeInputVo();
		noticeInputVo.setContent("test content");
		noticeInputVo.setImportantYn("Y");

		mockMvc.perform(post("/v1/ntool/api/notice")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.content(objectMapper.writeValueAsString(noticeInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is("[title](은)는 " + ResultCode.PARAMETER_ERROR.getDisplayMessage())));
	}

	/**
	 * 공지사항 조회 - 정상적으로 조회 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessGetNotice() throws Exception {

		int seq = apiUtils.getNoticeSeq();

		mockMvc.perform(get("/v1/ntool/api/notice/{seq}", seq)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(notNullValue())));
	}

	/**
	 * 공지사항 조회 - 일치하는 정보가 없을 때 오류 발생
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchGetNotice() throws Exception {

		mockMvc.perform(get("/v1/ntool/api/notice/{seq}", 0)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.NOT_MATCH.getDisplayMessage())));
	}

	/**
	 * 공지사항 수정 - 정상적으로 수정 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessUpdateNotice() throws Exception {

		int seq = apiUtils.getNoticeSeq();

		NoticeInputVo noticeInputVo = new NoticeInputVo();
		noticeInputVo.setSeq(seq);
		noticeInputVo.setTitle("test notice");
		noticeInputVo.setContent("test content");
		noticeInputVo.setImportantYn("Y");

		mockMvc.perform(put("/v1/ntool/api/notice")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.content(objectMapper.writeValueAsString(noticeInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 공지사항 수정 - 필수값 오류인 경우
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorUpdateNotice() throws Exception {

		NoticeInputVo noticeInputVo = new NoticeInputVo();
		noticeInputVo.setContent("test content");
		noticeInputVo.setImportantYn("Y");

		mockMvc.perform(put("/v1/ntool/api/notice")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.content(objectMapper.writeValueAsString(noticeInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is("[title](은)는 " + ResultCode.PARAMETER_ERROR.getDisplayMessage())));
	}

	/**
	 * 공지사항 수정 - 일치하는 정보가 없을 경우 데이터 저장 오류
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchUpdateNotice() throws Exception {

		NoticeInputVo noticeInputVo = new NoticeInputVo();
		noticeInputVo.setSeq(0);
		noticeInputVo.setTitle("test notice");
		noticeInputVo.setContent("test content");
		noticeInputVo.setImportantYn("Y");

		mockMvc.perform(put("/v1/ntool/api/notice")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.content(objectMapper.writeValueAsString(noticeInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.UPDATE_FAIL.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.UPDATE_FAIL.getDisplayMessage())));
	}

	/**
	 * 공지사항 삭제 - 정상적으로 삭제 됐을 때
	 * @throws Exception
	 */
	@Test
	public void caseSuccessDeleteNotice() throws Exception {

		int seq = apiUtils.getNoticeSeq();

		NoticeParameterVo noticeParameterVo = new NoticeParameterVo();
		noticeParameterVo.setSeq(seq);

		mockMvc.perform(delete("/v1/ntool/api/notice")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.content(objectMapper.writeValueAsString(noticeParameterVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 공지사항 삭제 - 일치하는 정보가 없을 경우 데이터 삭제 오류
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchDeleteNotice() throws Exception {

		NoticeParameterVo noticeParameterVo = new NoticeParameterVo();
		noticeParameterVo.setSeq(0);

		mockMvc.perform(delete("/v1/ntool/api/notice")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.content(objectMapper.writeValueAsString(noticeParameterVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.DELETE_FAIL.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.DELETE_FAIL.getDisplayMessage())));
	}

	/**
	 * 공지사항 목록 - 목록이 존재하는 경우
	 * @throws Exception
	 */
	@Test
	public void caseExistsNoticeList() throws Exception {

		mockMvc.perform(get("/v1/ntool/api/list/notice")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
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
	 * 공지사항 목록 - 목록이 존재하지 않는 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsNoticeList() throws Exception {

		mockMvc.perform(get("/v1/ntool/api/list/notice")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.param("searchType", "title")
			.param("searchKeyword", "no_match_keyword")
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
	 * 공지사항 테이블 스키마 조회
	 * @throws Exception
	 */
	@Test
	public void caseNoticeSchema() throws Exception {

		mockMvc.perform(get("/v1/ntool/api/schema/notice")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN))
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.schema", is(not(hasSize(0)))));
	}

}
