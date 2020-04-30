package com.naver.pubtrans.itn.api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.ApiUtils;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.repository.FareRuleRepository;
import com.naver.pubtrans.itn.api.service.TaskService;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleTaskInputVo;


/**
 * 요금 관리 Test
 * @author westwind
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class FareRuleControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FareRuleRepository fareRuleRepository;

	// 작업ID
	private static long TASK_ID;

	private ApiUtils apiUtils;

	// 테스트 header에 전달 될 Token Map
	private LinkedHashMap<String, String> tokenMap;

	@Before
	public void setup() throws Exception {
		//Api Test Utils 초기화
		apiUtils = new ApiUtils(mockMvc, objectMapper);

		tokenMap = apiUtils.getTokenMap();
	}


	/**
	 * 기본 요금 룰 조회 - 일치하는 기본 요금 룰이 있을 경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchBaseFareRule() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/baseFareRule")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.param("cityCode", "1000")
            	.param("BusClass", "11")
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(notNullValue())));
	}

	/**
	 * 기본 요금 룰 조회 - 일치하는 기본 요금 룰이 없을 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchBaseFareRule() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/baseFareRule")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	        	.param("cityCode", "0")
	        	.param("BusClass", "0")
	        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}
	/**
	 * 예외 요금 룰 조회 - 일치하는 예외 요금 룰이 있을 경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchExceptionalFareRule() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/exceptionalFareRule/{fareId}", 3)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(notNullValue())));
	}

	/**
	 * 예외 요금 룰 조회 - 일치하는 예외 요금 룰이 없을 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchExceptionalFareRule() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/exceptionalFareRule/{fareId}", 0)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}

	/**
	 * 예외 요금 룰 목록- 데이터가 존재할때
	 * @throws Exception
	 */
	@Test
	public void caseExistsExceptionalFareRule() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/exceptionalFareRule")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.param("cityCode", "1000")
            	.param("BusClass", "11")
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(not(hasSize(0)))));
	}

	/**
	 * 예외 요금 룰 목록- 데이터가 존재하지 않을때
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsExceptionalFareRule() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/exceptionalFareRule")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	        	.param("cityCode", "0")
	        	.param("BusClass", "0")
	        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(hasSize(0))));
	}

	/**
	 * 요금 룰 Task 정보 - 일치하는 Task 정보가 존재하는 경우
	 * @throws Exception
	 */
	@Test
	@Order(2)
	public void caseMatchFareRuleTask() throws Exception {
		if(TASK_ID == 0) {
			this.caseFareRuleAddTask();
		}

		mockMvc.perform(get("/v1/ntool/api/info/fareRuleTask/{taskId}", TASK_ID)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.taskInfo", is(notNullValue())));
	}

	/**
	 * 요금 룰 Task 정보 - 일치하는 Task 정보가 없는 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchFareRuleTask() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/fareRuleTask/{taskId}", 999999999)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}

	/**
	 * 요금 룰 Task 요약 목록 - 목록이 존재하는 경우
	 * @throws Exception
	 */
	@Test
	public void caseExistsFareRuleTaskList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/fareRuleTask/summary/{fareId}", 3)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
            .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(not(hasSize(0)))));
	}

	/**
	 * 요금 룰 Task 요약 목록 - 목록이 존재하지 않는 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsFareRuleTaskList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/fareRuleTask/summary/{fareId}", -1)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
            .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(hasSize(0))));
	}

	/**
	 * 요금 룰 테이블 스키마 조회
	 * @throws Exception
	 */
	@Test
	public void caseFareRuleSchema() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/schema/fareRule")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
            .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.schema", is(not(hasSize(0)))));
	}

	/**
	 * 요금 룰 생성을 위한 Task 등록 - 정상인 경우
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	public void caseFareRuleAddTask() throws Exception {

		Integer[] routeIds = {11000006};
		Integer[] startStopIds = {55000691};
		Integer[] endStopIds = {55000126};

		FareRuleTaskInputVo fareRuleTaskInputVo = new FareRuleTaskInputVo() ;
		fareRuleTaskInputVo.setBaseYn("N");
		fareRuleTaskInputVo.setBaseFare(1000);
		fareRuleTaskInputVo.setBaseDist(10);
		fareRuleTaskInputVo.setUnitFare(1200);
		fareRuleTaskInputVo.setUnitDist(3);
		fareRuleTaskInputVo.setMaxFare(1200);
		fareRuleTaskInputVo.setCityCode(1000);
		fareRuleTaskInputVo.setBusClass(11);
		fareRuleTaskInputVo.setRouteIds(routeIds);
		fareRuleTaskInputVo.setStartStopIds(startStopIds);
		fareRuleTaskInputVo.setEndStopIds(endStopIds);
		fareRuleTaskInputVo.setSourceName("네이버");
		fareRuleTaskInputVo.setSourceUrl("www.naver.com");
		fareRuleTaskInputVo.setComment("테스트 등록");
		fareRuleTaskInputVo.setTaskComment("요금 등록");
		fareRuleTaskInputVo.setCheckUserId("test");
		fareRuleTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);

		MvcResult mvcResult = mockMvc.perform(post("/v1/ntool/api/fareRuleTask/addTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(fareRuleTaskInputVo))
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
		List<Number> taskIdList = (List<Number>)map.get(CommonConstant.KEY_TASK_LIST);
		
		TASK_ID = taskIdList.get(0).longValue();

	}

	/**
	 * 요금 룰 생성을 위한 Task 등록 - 필수값 오류인 경우
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorFareRuleAddTask() throws Exception {

		FareRuleTaskInputVo fareRuleTaskInputVo = new FareRuleTaskInputVo() ;
		fareRuleTaskInputVo.setBaseYn("N");
		fareRuleTaskInputVo.setBaseFare(1000);
		fareRuleTaskInputVo.setBaseDist(10);

		mockMvc.perform(post("/v1/ntool/api/fareRuleTask/addTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(fareRuleTaskInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())));
	}

	/**
	 * 요금 룰 수정을 위한 Task 등록 - 정상
	 * @throws Exception
	 */
	@Test
	public void caseFareRuleEditTask() throws Exception {
		
		Integer[] routeIds = {11000006};
		Integer[] startStopIds = {55000691};
		Integer[] endStopIds = {55000102};

		FareRuleTaskInputVo fareRuleTaskInputVo = new FareRuleTaskInputVo() ;
		fareRuleTaskInputVo.setFareId(10);
		fareRuleTaskInputVo.setBaseYn("N");
		fareRuleTaskInputVo.setBaseFare(1000);
		fareRuleTaskInputVo.setBaseDist(10);
		fareRuleTaskInputVo.setUnitFare(1200);
		fareRuleTaskInputVo.setUnitDist(3);
		fareRuleTaskInputVo.setMaxFare(1200);
		fareRuleTaskInputVo.setCityCode(1000);
		fareRuleTaskInputVo.setBusClass(11);
		fareRuleTaskInputVo.setRouteIds(routeIds);
		fareRuleTaskInputVo.setStartStopIds(startStopIds);
		fareRuleTaskInputVo.setEndStopIds(endStopIds);
		fareRuleTaskInputVo.setSourceName("네이버");
		fareRuleTaskInputVo.setSourceUrl("www.naver.com");
		fareRuleTaskInputVo.setComment("테스트 수정");
		fareRuleTaskInputVo.setTaskComment("요금 수정");
		fareRuleTaskInputVo.setCheckUserId("test");
		fareRuleTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);

		mockMvc.perform(post("/v1/ntool/api/fareRuleTask/editTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(fareRuleTaskInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));

	}

	/**
	 * 요금 룰 수정을 위한 Task 등록 - 필수값 오류인 경우
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorFareRuleEditTask() throws Exception {

		FareRuleTaskInputVo fareRuleTaskInputVo = new FareRuleTaskInputVo() ;
		fareRuleTaskInputVo.setBaseYn("N");
		fareRuleTaskInputVo.setBaseFare(1000);
		fareRuleTaskInputVo.setBaseDist(10);

		mockMvc.perform(post("/v1/ntool/api/fareRuleTask/editTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(fareRuleTaskInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())));
	}

	/**
	 * 요금 룰 작업 Task 정보 수정 - 성공
	 * @throws Exception
	 */
	@Test
	public void caseFareRuleTaskModify() throws Exception {

		Integer[] routeIds = {11000006};
		Integer[] startStopIds = {55000691};
		Integer[] endStopIds = {55000102};

		FareRuleTaskInputVo fareRuleTaskInputVo = new FareRuleTaskInputVo() ;
		fareRuleTaskInputVo.setTaskId(1412);
		fareRuleTaskInputVo.setFareId(10);
		fareRuleTaskInputVo.setBaseYn("N");
		fareRuleTaskInputVo.setBaseFare(1000);
		fareRuleTaskInputVo.setBaseDist(10);
		fareRuleTaskInputVo.setUnitFare(1200);
		fareRuleTaskInputVo.setUnitDist(3);
		fareRuleTaskInputVo.setMaxFare(1200);
		fareRuleTaskInputVo.setCityCode(1000);
		fareRuleTaskInputVo.setBusClass(11);
		fareRuleTaskInputVo.setRouteIds(routeIds);
		fareRuleTaskInputVo.setStartStopIds(startStopIds);
		fareRuleTaskInputVo.setEndStopIds(endStopIds);
		fareRuleTaskInputVo.setSourceName("네이버");
		fareRuleTaskInputVo.setSourceUrl("www.naver.com");
		fareRuleTaskInputVo.setComment("테스트 수정");
		fareRuleTaskInputVo.setTaskComment("요금 수정");
		fareRuleTaskInputVo.setCheckUserId("test");
		fareRuleTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);

		mockMvc.perform(put("/v1/ntool/api/modify/fareRuleTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	            .content(objectMapper.writeValueAsString(fareRuleTaskInputVo))
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 요금 룰 작업 Task 정보 수정 - 일치하는 작업 정보가 없을때
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchFareRuleTaskModifyError() throws Exception {

		Integer[] routeIds = {11000006};
		Integer[] startStopIds = {55000691};
		Integer[] endStopIds = {55000102};

		FareRuleTaskInputVo fareRuleTaskInputVo = new FareRuleTaskInputVo() ;
		fareRuleTaskInputVo.setTaskId(999999999999L);
		fareRuleTaskInputVo.setFareId(10);
		fareRuleTaskInputVo.setBaseYn("N");
		fareRuleTaskInputVo.setBaseFare(1000);
		fareRuleTaskInputVo.setBaseDist(10);
		fareRuleTaskInputVo.setUnitFare(1200);
		fareRuleTaskInputVo.setUnitDist(3);
		fareRuleTaskInputVo.setMaxFare(1200);
		fareRuleTaskInputVo.setCityCode(1000);
		fareRuleTaskInputVo.setBusClass(11);
		fareRuleTaskInputVo.setRouteIds(routeIds);
		fareRuleTaskInputVo.setStartStopIds(startStopIds);
		fareRuleTaskInputVo.setEndStopIds(endStopIds);
		fareRuleTaskInputVo.setSourceName("네이버");
		fareRuleTaskInputVo.setSourceUrl("www.naver.com");
		fareRuleTaskInputVo.setComment("테스트 수정");
		fareRuleTaskInputVo.setTaskComment("요금 수정");
		fareRuleTaskInputVo.setCheckUserId("test");
		fareRuleTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);

		mockMvc.perform(put("/v1/ntool/api/modify/fareRuleTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
	            .content(objectMapper.writeValueAsString(fareRuleTaskInputVo))
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}
	
	/**
	 * 요금 룰 삭제요청 Task 등록 - 성공
	 * @throws Exception
	 */
	@Test
	public void caseFareRuleRemoveTask() throws Exception {

		FareRuleRemoveTaskInputVo fareRuleRemoveTaskInputVo = new FareRuleRemoveTaskInputVo() ;
		fareRuleRemoveTaskInputVo.setFareId(3);
		fareRuleRemoveTaskInputVo.setTaskComment("미존재 요금 룰 - 삭제 처리");
		fareRuleRemoveTaskInputVo.setCheckUserId("test");
		fareRuleRemoveTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);

		mockMvc.perform(post("/v1/ntool/api/fareRuleTask/removeTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(fareRuleRemoveTaskInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 요금 룰 삭제요청 Task 등록 - 실패(해당 요금 룰이 없는경우)
	 * @throws Exception
	 */
	@Test
	public void caseFareRuleRemoveTaskError() throws Exception {

		FareRuleRemoveTaskInputVo fareRuleRemoveTaskInputVo = new FareRuleRemoveTaskInputVo() ;
		fareRuleRemoveTaskInputVo.setFareId(-1);
		fareRuleRemoveTaskInputVo.setTaskComment("미존재 요금 룰 - 삭제 처리");
		fareRuleRemoveTaskInputVo.setCheckUserId("test");
		fareRuleRemoveTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);

		mockMvc.perform(post("/v1/ntool/api/fareRuleTask/removeTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(fareRuleRemoveTaskInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}

}
