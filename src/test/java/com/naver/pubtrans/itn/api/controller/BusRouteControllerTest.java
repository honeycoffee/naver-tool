package com.naver.pubtrans.itn.api.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.ApiUtils;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.service.BusRouteService;
import com.naver.pubtrans.itn.api.vo.bus.graph.BusStopGraphVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.GeoJsonInputVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.output.GeoJsonOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteCompanyTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteDetailOutputVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 버스 노선관리 테스트
 * @author adtec10
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class BusRouteControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@Autowired
	private BusRouteService busRouteService;

	private ApiUtils apiUtils;

	// 작업ID
	private static long TASK_ID;

	// 테스트 header에 전달 될 Token Map
	private LinkedHashMap<String, String> tokenMap;

	@Before
	public void setup() throws Exception {
		//Api Test Utils 초기화
		apiUtils = new ApiUtils(mockMvc, objectMapper);

		tokenMap = apiUtils.getTokenMap();
	}


	/**
	 * 버스 노선목록 - 데이터가 존재할때
	 * @throws Exception
	 */
	@Test
	public void caseExistsBusRouteList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busRoute")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
				.param("routeId", "11000009")
            	.param("routeName", "60")
            	.param("cityCode", "")
            	.param("busClass", "11")
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
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
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
		mockMvc.perform(get("/v1/ntool/api/list/busRouteTask/{busRouteId}", 11000000)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
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
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
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
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
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
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
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
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.param("busStopIds", "55000837")
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_RULE_ERROR.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.PARAMETER_RULE_ERROR.getDisplayMessage())));
	}

	/**
	 * 버스노선 상세정보 테이블 스키마 조회
	 * @throws Exception
	 */
	@Test
	public void busRouteSchema() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/schema/busRoute")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
            .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.schema", is(not(hasSize(0)))));
	}

	/**
	 * 버스노선 상세정보 - 일치하는 정보가 있을경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchBusRouteInfo() throws Exception {
		// TODO 버스노선 생성 및 배포부분 개발 완료후 TC 연계하여 노선ID를 가져와 테스트 할 수 있도록 수정 필요
		mockMvc.perform(get("/v1/ntool/api/info/busRoute/{routeId}", 11000000)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(notNullValue())));
	}

	/**
	 * 버스노선 상세정보 - 일치하는 노선 정보가 없을경우
	 * @throws Exception
	 */
	@Test
	public void caseNonMatchBusRouteInfo() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busRoute/{routeId}", 12000000)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())))
				.andExpect(jsonPath("$.message", is(ResultCode.NOT_MATCH.getDisplayMessage())));
	}

	/**
	 * 버스노선 상세정보 - 우회노선 목록을 가지고 있는경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchBusRouteInfoWithBypassChild() throws Exception {
		// TODO 버스노선 생성 및 배포부분 개발 완료후 TC 연계하여 노선ID를 가져와 테스트 할 수 있도록 수정 필요
		mockMvc.perform(get("/v1/ntool/api/info/busRoute/{routeId}", 11000009)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.bypassChildList", is(not(hasSize(0)))));
	}

	/**
	 * 버스노선 상세정보 - 우회노선 목록이 없는경우
	 * @throws Exception
	 */
	@Test
	public void caseNonMatchBusRouteInfoWithBypassChild() throws Exception {
		// TODO 버스노선 생성 및 배포부분 개발 완료후 TC 연계하여 노선ID를 가져와 테스트 할 수 있도록 수정 필요
		mockMvc.perform(get("/v1/ntool/api/info/busRoute/{routeId}", 11000000)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.bypassChildList", is(nullValue())));
	}

	/**
	 * 버스노선 작업 상세정보 - 일치하는 정보가 있을경우
	 * @throws Exception
	 */
	@Test
	@Order(2)
	public void caseMatchBusRouteTaskInfo() throws Exception {
		if(TASK_ID == 0) {
			this.caseBusRouteAddTask();
		}

		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", TASK_ID)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(notNullValue())));
	}

	/**
	 * 버스노선 작업 상세정보 - 일치하는 정보가 없을경우
	 * @throws Exception
	 */
	@Test
	public void caseNonMatchBusRouteTaskInfo() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 0)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())))
				.andExpect(jsonPath("$.message", is(ResultCode.NOT_MATCH.getDisplayMessage())));
	}

	/**
	 * 버스노선 작업 상세정보 - 우회노선인 경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchBusRouteTaskInfoWithBypassChild() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 130)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.bypassYn", is(CommonConstant.Y)));
	}

	/**
	 * 버스노선 작업 상세정보 - 우회노선이 아닌경우
	 * @throws Exception
	 */
	@Test
	public void caseNonMatchBusRouteTaskInfoWithBypassChild() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 129)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.bypassYn", is(CommonConstant.N)));
	}

	/**
	 * 버스노선 작업 상세정보 - 우회노선 목록을 가지고 있는경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchBusRouteInfoTaskWithBypassChild() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 129)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.bypassChildList", is(not(hasSize(0)))));
	}

	/**
	 * 버스노선 작업 상세정보 - 우회노선 목록이 없는경우
	 * @throws Exception
	 */
	@Test
	public void caseNonMatchBusRouteInfoTaskWithBypassChild() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 130)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.bypassChildList", is(nullValue())));
	}

	/**
	 * 버스노선 작업정보에 그래프 정보가 포함되어 있는지 확인한다
	 * @throws Exception
	 */
	@Test
	public void busGraphTask() throws Exception {
		TaskOutputVo taskOutputVo = new TaskOutputVo();
		taskOutputVo.setTaskId(122);	// 경유 정류장 정보가 수정된 Task ID
		taskOutputVo.setPubTransId(11000000);

		GeoJsonOutputVo geoJsonOutputVo = busRouteService.getBusRouteGraphTaskInfo(taskOutputVo);

		int graphRouteCnt = geoJsonOutputVo.getFeatures().size();
		int graphRouteCheckCnt = 68;		// Task에 저장된 경유장류장 수

		assertEquals(graphRouteCnt, graphRouteCheckCnt);
	}

	/**
	 * 버스노선 작업정보에 그래프 정보가 미포함인지 확인한다
	 * @throws Exception
	 */
	@Test
	public void busGraphTaskWithoutTask() throws Exception {
		TaskOutputVo taskOutputVo = new TaskOutputVo();
		taskOutputVo.setTaskId(131);	// 경유 정류장 정보가 수정되지 않은 Task ID
		taskOutputVo.setPubTransId(11000000);

		GeoJsonOutputVo geoJsonOutputVo = busRouteService.getBusRouteGraphTaskInfo(taskOutputVo);

		int graphRouteCnt = geoJsonOutputVo.getFeatures().size();
		int graphRouteCheckCnt = 70;		// 해당 노선이 서비스중인 경유 정류장 수

		assertEquals(graphRouteCnt, graphRouteCheckCnt);
	}


	/**
	 * 버스노선 작업정보 - BIS 변경사항으로 경유정류장이 변경되었을경우 모든 정류장 ID가 내부 정류장 ID와 일치할 경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchBusRouteInfoTaskWithBisAutoGraph() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 129)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.taskInfo.bisChangeDataInfo.busStopGraphInfo.features", is(hasSize(2))));
	}

	/**
	 * 버스노선 작업정보 - BIS 변경사항으로 경유정류장이 변경되었을때 BIS 정류장ID가 내부 ID와 일치하지 않은경우
	 * @throws Exception
	 */
	@Test
	public void caseNonMatchBusRouteInfoTaskWithBisAutoGraph() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 130)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.taskInfo.bisChangeDataInfo", is(not(hasKey("busStopGraphInfo")))));
	}

	/**
	 * 버스노선을 생성하는 Task를 등록한다 - 성공일때 taskId를 반환한다
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	public void caseBusRouteAddTask() throws Exception {

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputInfo(TaskType.REGISTER.getCode());

		MvcResult mvcResult = mockMvc.perform(post("/v1/ntool/api/busRouteTask/addTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	            .content(objectMapper.writeValueAsString(busRouteTaskInputVo))
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andReturn();

		String resultString = mvcResult.getResponse().getContentAsString();
		CommonOutput rsCommonOutput = objectMapper.readValue(resultString, CommonOutput.class);
		CommonResult rsCommonReuslt = rsCommonOutput.getResult();

		Map<String, Object> map = (Map<String, Object>)rsCommonReuslt.getData();
		TASK_ID = ((Number)map.get(CommonConstant.KEY_TASK)).longValue();
	}

	/**
	 * 버스노선을 생성하는 Task를 등록한다 - 필수값 형식 오류 일때
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorBusRouteAddTask() throws Exception {

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputInfo(TaskType.REGISTER.getCode());

		// Y/N만 입력 가능한 필드
		busRouteTaskInputVo.setMondayYn("YY");

		mockMvc.perform(post("/v1/ntool/api/busRouteTask/addTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	            .content(objectMapper.writeValueAsString(busRouteTaskInputVo))
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())))
				.andExpect(jsonPath("$.message", is("[mondayYn](은)는 " + ResultCode.PARAMETER_ERROR.getDisplayMessage())));

	}

	/**
	 * 버스노선을 수정하는 Task를 등록한다 - 성공
	 * @throws Exception
	 */
	@Test
	public void caseBusRouteEditTask() throws Exception {

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputInfo(TaskType.MODIFY.getCode());

		mockMvc.perform(post("/v1/ntool/api/busRouteTask/editTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	            .content(objectMapper.writeValueAsString(busRouteTaskInputVo))
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 버스노선을 수정하는 Task를 등록한다 - 필수값 오류일때
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorBusRouteEditTask() throws Exception {

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputInfo(TaskType.MODIFY.getCode());

		// 노선ID 제거
		busRouteTaskInputVo.setRouteId(0);

		mockMvc.perform(post("/v1/ntool/api/busRouteTask/editTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	            .content(objectMapper.writeValueAsString(busRouteTaskInputVo))
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())))
				.andExpect(jsonPath("$.message", is("[routeId](은)는 " + ResultCode.PARAMETER_ERROR.getDisplayMessage())));

	}

	/**
	 * 사용자 입력정보를 통해 경유정류장 포맷 목록을 생성하는 테스트
	 * @throws Exception
	 */
	@Test
	public void makeBusStopGraphByBusRouteTaskInfo() throws Exception {

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputInfo(TaskType.MODIFY.getCode());
		List<BusStopGraphVo> busStopGraphVoList = busRouteService.makeBusStopGraphVoList(busRouteTaskInputVo);

		int graphRouteCnt = busStopGraphVoList.size();
		int graphRouteCheckCnt = 71;		// 해당 노선이 서비스중인 경유 정류장 수

		assertEquals(graphRouteCnt, graphRouteCheckCnt);
	}



	/**
	 * 사용자 입력정보를 통해 생성한 경유정류장 목록이 DB에 저장된 항목과 일치하여 True를 리턴하는지 확인한다
	 * @throws Exception
	 */
	@Test
	public void nonChangeBusRouteStop() throws Exception {

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputInfo(TaskType.MODIFY.getCode());
		List<BusStopGraphVo> busStopGraphVoList = busRouteService.makeBusStopGraphVoList(busRouteTaskInputVo);

		boolean isSame = busRouteService.isTheSameAsBusRouteStopVoListOfDb(TaskType.MODIFY.getCode(), busRouteTaskInputVo.getRouteId(), busStopGraphVoList);

		assertTrue(isSame);
	}

	/**
	 * 사용자 입력정보를 통해 생성한 경유정류장 목록이 DB에 저장된 항목과 일치하지 않아, 신규 경유정류장 목록을 생성하는지 확인한다
	 * @throws Exception
	 */
	@Test
	public void changeBusRouteStop() throws Exception {

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputInfo(TaskType.MODIFY.getCode());
		List<BusStopGraphVo> busStopGraphVoList = busRouteService.makeBusStopGraphVoList(busRouteTaskInputVo);

		// 임의로 뒤 1개 정류장을 삭제한다
		busStopGraphVoList.remove(busStopGraphVoList.size()-1);

		boolean isSame = busRouteService.isTheSameAsBusRouteStopVoListOfDb(TaskType.MODIFY.getCode(), busRouteTaskInputVo.getRouteId(), busStopGraphVoList);
		assertFalse(isSame);
	}

	/**
	 * 그래프 변경사항이 존재하지 않아 빈(empty) 목록을 반환하는지 확인한다
	 * @throws Exception
	 */
	@Test
	public void nonChangeBusRouteGraph() throws Exception {

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputInfo(TaskType.MODIFY.getCode());
		List<BusStopGraphVo> busStopGraphVoList = busRouteService.makeBusStopGraphVoList(busRouteTaskInputVo);

		List<BusStopGraphVo> changeGraphList = busRouteService.extractChangedBusStopGraphVoList(busStopGraphVoList);
		assertEquals(changeGraphList.size(), 0);
	}

	/**
	 * 경유정류장 목록중 그래프 정보가 변경된 항목을 찾는다
	 * @throws Exception
	 */
	@Test
	public void changeBusRouteGraph() throws Exception {

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputInfo(TaskType.MODIFY.getCode());
		List<BusStopGraphVo> busStopGraphVoList = busRouteService.makeBusStopGraphVoList(busRouteTaskInputVo);

		BusStopGraphVo busStopGraphVo1 = busStopGraphVoList.get(busStopGraphVoList.size()-2);
		BusStopGraphVo busStopGraphVo2 = busStopGraphVoList.get(busStopGraphVoList.size()-3);

		// 임의 그래프 조작
		busStopGraphVo1.setGraphInfo("LINESTRING(127.051581 37.626283,127.050537 37.627248,127.050331 37.627444,127.050195 37.627434,127.050160 37.627353,127.050357 37.627174)");
		busStopGraphVo2.setGraphInfo("LINESTRING(127.050265 37.623695,127.049880 37.62408,127.04993 37.624206,127.050627 37.624644,127.051986 37.625601,127.052019 37.625647,127.052053 37.625701,127.05204 37.625782,127.051993 37.625935,127.051581 37.626283)");

		busStopGraphVoList.set(busStopGraphVoList.size()-2, busStopGraphVo1);
		busStopGraphVoList.set(busStopGraphVoList.size()-3, busStopGraphVo2);

		List<BusStopGraphVo> changeGraphList = busRouteService.extractChangedBusStopGraphVoList(busStopGraphVoList);
		assertEquals(changeGraphList.size(), 2);
	}


	/**
	 * 버스노선 테스트 입력정보 공통
	 * 경유정류장은 DB에 저장된 노선정보를 이용하여 생성한다.
	 * @return
	 * @throws Exception
	 */
	public BusRouteTaskInputVo getBusRouteTaskInputInfo(String type) throws Exception {


		// 정류장 상세정보 가져오기
		String busRouteDetailJson = apiUtils.getBusRouteDetailInfo(11000000);


		JsonNode rootNode = objectMapper.readTree(busRouteDetailJson);
		JsonNode dataNode = rootNode.get("result").get("data");
		BusRouteDetailOutputVo busRouteDetailOutputVo = objectMapper.readValue(dataNode.toString(), BusRouteDetailOutputVo.class);
		GeoJsonOutputVo geoJsonOutputVo = busRouteDetailOutputVo.getBusStopGraphInfo();
		String geojsonText = objectMapper.writeValueAsString(geoJsonOutputVo);
		GeoJsonInputVo geoJsonInputVo = objectMapper.readValue(geojsonText, GeoJsonInputVo.class);


		BusRouteTaskInputVo busRouteTaskInputVo = new BusRouteTaskInputVo();

		if(type.equals(TaskType.REGISTER.getCode())) {
			busRouteTaskInputVo.setRouteName("서울01");
			busRouteTaskInputVo.setCityCode(1000);
			busRouteTaskInputVo.setBusClass(11);
			busRouteTaskInputVo.setLocalRouteId("T111111111");
			busRouteTaskInputVo.setProviderId(4);
			busRouteTaskInputVo.setTurningPointSequence(33);
			busRouteTaskInputVo.setMondayYn("Y");
			busRouteTaskInputVo.setTuesdayYn("Y");
			busRouteTaskInputVo.setWednesdayYn("Y");
			busRouteTaskInputVo.setThursdayYn("Y");
			busRouteTaskInputVo.setFridayYn("Y");
			busRouteTaskInputVo.setSaturdayYn("Y");
			busRouteTaskInputVo.setSundayYn("Y");

			BusRouteCompanyTaskInputVo busRoutecompanyTaskInputVo = new BusRouteCompanyTaskInputVo();
			busRoutecompanyTaskInputVo.setCompanyId(42);
			List<BusRouteCompanyTaskInputVo> companyList = new ArrayList<>();
			companyList.add(busRoutecompanyTaskInputVo);

			// 운수회사 정보
			busRouteTaskInputVo.setCompanyList(companyList);

			// 경유 정류장 정보
			busRouteTaskInputVo.setBusStopGraphInfo(geoJsonInputVo);

		}else if(type.equals(TaskType.MODIFY.getCode())) {
			BusRouteTaskInputVo busRouteTaskVo = objectMapper.readValue(dataNode.toString(), BusRouteTaskInputVo.class);
			BeanUtils.copyProperties(busRouteTaskVo, busRouteTaskInputVo);

			busRouteTaskInputVo.setWeekdayStartPointFirstTime("0430");
		}



		busRouteTaskInputVo.setTaskComment("노선등록 Junit 테스트");
		busRouteTaskInputVo.setCheckUserId(this.tokenMap.get("userId"));



		return busRouteTaskInputVo;
	}
}
