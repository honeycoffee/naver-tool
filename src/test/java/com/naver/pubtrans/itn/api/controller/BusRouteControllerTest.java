package com.naver.pubtrans.itn.api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.consts.ResultCode;

/**
 * 버스 노선관리 테스트
 * @author adtec10
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BusRouteControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;


	/**
	 * 버스 노선목록 - 데이터가 존재할때
	 * @throws Exception
	 */
	@Test
	public void caseExistsBusRouteList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busRoute")
            	.param("routeName", "")
            	.param("cityCode", "1000")
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
	 * 버스 노선목록 - 검색 데이터가 없을때
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsBusRouteList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busRoute")
            	.param("routeName", "11")
            	.param("cityCode", "3000")
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
	 * 버스 노선 작업목록 - 데이터가 존재할때
	 * @throws Exception
	 */
	@Test
	public void caseExistsBusRouteTaskList() throws Exception {
		// TODO 버스노선 등록 Task 개발 완료시 , 등록부분 TC 완료후 등록된 ID값을 가지고 테스트 진행할것.
		mockMvc.perform(get("/v1/ntool/api/list/busRouteTask/{busRouteId}", 11000000)
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
	 * 버스 노선 작업목록 - 데이터가 없을때
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsBusRouteTaskList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busRouteTask/{busRouteId}", 11000005)
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
	 * 연속되는 정류장간의 그래프 정보 목록 - 그래프 데이터 존재시
	 * @throws Exception
	 */
	@Test
	public void caseExistsGraphBetweenBusStop() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busStopsGraph")
        	.param("busStopIds", "55000837,55000520,55000529,55000845,55000524")
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(200)))
			.andExpect(jsonPath("$.result.data.features", is(hasSize(4))));

	}

	/**
	 * 연속되는 정류장간의 그래프 정보 목록 - 데이터 미 존재시
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsGraphBetweenBusStop() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busStopsGraph")
        	.param("busStopIds", "55000005,55000009")
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(200)))
			.andExpect(jsonPath("$.result.data.features[0].properties.matchGraphInfoYn", is("N")));


	}

	/**
	 * 연속되는 정류장간의 그래프 정보 목록 - 파라미터 오류
	 * @throws Exception
	 */
	@Test
	public void caseExistsGraphBetweenBusStopParameterError() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busStopsGraph")
        	.param("busStopIds", "55000837")
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_RULE_ERROR.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.PARAMETER_RULE_ERROR.getDisplayMessage())));
	}
}
