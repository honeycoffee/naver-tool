package com.naver.pubtrans.itn.api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.ApiUtils;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.ResultCode;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class TaskControllerTest {
	@Autowired
    private MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	private ApiUtils apiUtils;

	// 테스트 header에 전달 될 Token Map
	private LinkedHashMap<String, String> tokenMap;

	@BeforeEach
	public void setup() throws Exception {
		//Api Test Utils 초기화
		apiUtils = new ApiUtils(mockMvc, objectMapper);
		tokenMap = apiUtils.getTokenMap();
	}

	/**
	 * 작업 목록 - 데이터가 존재할때
	 * @throws Exception
	 */
	@Test
	public void caseExistsTaskList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/task")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
				.param("pubTransType", "route")
            	.param("taskDataSourceType", "7")
            	.param("pageNo", "1")
            	.param("listSize", "20")
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(200)))
				.andExpect(jsonPath("$.result.data", is(not(hasSize(0)))));
	}

	/**
	 * 작업 목록 - 검색 데이터가 없을 때
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsTaskList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/task")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
				.param("pubTransType", "route")
            	.param("taskDataSourceType", "7")
            	.param("startWorkDate", "2020-02-01")
            	.param("endWorkDate", "2020-02-05")
            	.param("pageNo", "1")
            	.param("listSize", "20")
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(200)))
				.andExpect(jsonPath("$.result.data", is(hasSize(0))));
	}

	/**
	 * 작업 통계정보 - 데이터가 있을때
	 * @throws Exception
	 */
	@Test
	public void caseExistsTaskSummaryStatistics() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/task/summaryStatistics")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
				.param("pubTransType", "route")
            	.param("taskDataSourceType", "7")
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(200)))
				.andExpect(jsonPath("$.result.data.totalCnt", greaterThan(0)));
	}

	/**
	 * 작업 목록 - 검색 데이터가 없을 때
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsTaskSummaryStatistics() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/task/summaryStatistics")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
				.param("pubTransType", "route")
            	.param("taskDataSourceType", "7")
            	.param("startWorkDate", "2020-02-01")
            	.param("endWorkDate", "2020-02-05")
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(200)))
				.andExpect(jsonPath("$.result.data.totalCnt", is(0)));
	}


	/**
	 * 작업 상세정보 - 일치하는 정보가 있을경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchTaskInfo() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/task/{taskId}", 1367)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(notNullValue())));
	}

	/**
	 * 작업 상세정보 - 일치하는  정보가 없을경우
	 * @throws Exception
	 */
	@Test
	public void caseNonMatchTaskInfo() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/task/{taskId}", 0)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())))
				.andExpect(jsonPath("$.message", is(ResultCode.NOT_MATCH.getDisplayMessage())));
	}

}
