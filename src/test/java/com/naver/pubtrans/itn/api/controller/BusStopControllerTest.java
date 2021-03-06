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

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.ApiUtils;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransId;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.repository.BusStopRepository;
import com.naver.pubtrans.itn.api.service.TaskService;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopTaskInputVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.task.input.TaskInputVo;


/**
 * 버스정류장 관리 Test
 * @author adtec10
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class BusStopControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@Autowired
	private TaskService taskService;

	@Autowired
	private BusStopRepository busStopRepository;

	// 작업ID
	private static long TASK_ID;

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
	 * 버스정류장 목록 - 데이터가 존재할때
	 * @throws Exception
	 */
	@Test
	public void caseExistsBusStopList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busStop")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.param("stopName", "")
            	.param("cityCode", "")
            	.param("pageNo", "1")
            	.param("listSize", "20")
            	.param("sort", "stopId,asc")
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(not(hasSize(0)))));
	}

	/**
	 * 버스정류장 목록 - 데이터가 존재하지 않을때
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsBusStopList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busStop")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.param("stopName", "정류장명칭")
            	.param("cityCode", "2000")
            	.param("pageNo", "1")
            	.param("listSize", "20")
            	.param("sort", "stopId,asc")
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
                .andDo(print())
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
    			.andExpect(jsonPath("$.result.data", is(hasSize(0))));
	}

	/**
	 * 버스정류장 상세정보 - 일치하는 정류장이 있을경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchBusStopInfo() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busStop/{stopId}", 55000000)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(notNullValue())));
	}

	/**
	 * 버스정류장 상세정보 - 일치하는 정류장이 없을경우
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchBusStopInfo() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busStop/{stopId}", 400000)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}

	/**
	 * 버스정류장 상세정보 - 일치하는 정류장이 존재하면서 경유노선이 존재하는 경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchBusStopInfoWithBusRoute() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busStop/{stopId}", 55000000)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.busRouteInfo", is(notNullValue())));
	}

	/**
	 * 버스정류장 상세정보 - 일치하는 정류장이 존재하면서 경유노선이 없는 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchBusStopInfoWithBusRoute() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busStop/{stopId}", 55000848)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.busRouteInfo", is(nullValue())));
	}

	/**
	 * 버스정류장 Task 정보 - 일치하는 Task 정보가 존재하는 경우
	 * @throws Exception
	 */
	@Test
	@Order(2)
	public void caseMatchBusStopTask() throws Exception {
		if(TASK_ID == 0) {
			this.caseBusStopAddTask();
		}

		mockMvc.perform(get("/v1/ntool/api/info/busStopTask/{taskId}", TASK_ID)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.taskInfo", is(notNullValue())));
	}

	/**
	 * 버스정류장 Task 정보 - 일치하는 Task 정보가 없는 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchBusStopTask() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busStopTask/{taskId}", 2000)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}

	/**
	 * 버스정류장 Task 정보 - 일치하는 Task 정보가 존재하면서 BIS 자동 변경 데이터가 존재하는 경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchBusStopTaskWithBisAutoChangeData() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busStopTask/{taskId}", 3)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data.taskInfo.bisChangeDataInfo", is(notNullValue())));
	}

	/**
	 * 정류장 Task 요약 목록 - 목록이 존재하는 경우
	 * @throws Exception
	 */
	@Test
	public void caseExistsBusStopTaskList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busStopTask/summary/{busStopId}", 55000389)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
            .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(not(hasSize(0)))));
	}

	/**
	 * 정류장 Task 요약 목록 - 목록이 존재하지 않는 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsBusStopTaskList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busStopTask/summary/{busStopId}", 800)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
            .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(hasSize(0))));
	}

	/**
	 * 정류장 테이블 스키마 조회
	 * @throws Exception
	 */
	@Test
	public void caseBusStopSchema() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/schema/busStop")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
            .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.schema", is(not(hasSize(0)))));
	}

	/**
	 * 버스정류장 생성을 위한 Task 등록 - 정상인 경우
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	public void caseBusStopAddTask() throws Exception {

		BusStopTaskInputVo busStopInputVo = new BusStopTaskInputVo();
		busStopInputVo.setStopName("jUnit Test");
		busStopInputVo.setLongitude(126.123456);
		busStopInputVo.setLatitude(34.5678);
		busStopInputVo.setCityCode(1000);
		busStopInputVo.setLevel(1);
		busStopInputVo.setNonstopYn("N");
		busStopInputVo.setVirtualStopYn("N");
		busStopInputVo.setCenterStopYn("N");
		busStopInputVo.setSido("서울시");
		busStopInputVo.setGu("영등포구");
		busStopInputVo.setDong("당산동");
		busStopInputVo.setRoadAddress("당산로41길 11");
		busStopInputVo.setRoadName("당산로41길");
		busStopInputVo.setBonbun("11");
		busStopInputVo.setBoobun("2");
		busStopInputVo.setLocalStopId("100000031");
		busStopInputVo.setProviderId(4);
		busStopInputVo.setDisplayId("01123");
		busStopInputVo.setTaskComment("정류장 등록");
		busStopInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busStopInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busStopInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);

		MvcResult mvcResult = mockMvc.perform(post("/v1/ntool/api/busStopTask/addTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busStopInputVo))
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
	 * 버스정류장 생성을 위한 Task 등록 - 필수값 오류인 경우
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorBusStopAddTask() throws Exception {
		BusStopTaskInputVo busStopInputVo = new BusStopTaskInputVo();
		busStopInputVo.setStopName("SK v1 정류장");
		busStopInputVo.setLongitude(126.123456);

		mockMvc.perform(post("/v1/ntool/api/busStopTask/addTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busStopInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())));
	}

	/**
	 * 버스정류장 수정을 위한 Task 등록 - 정상
	 * @throws Exception
	 */
	@Test
	public void caseBusStopEditTask() throws Exception {

		BusStopTaskInputVo busStopInputVo = new BusStopTaskInputVo();
		busStopInputVo.setStopId(55000000);
		busStopInputVo.setStopName("jUnit Test");
		busStopInputVo.setLongitude(126.123456);
		busStopInputVo.setLatitude(34.5678);
		busStopInputVo.setCityCode(1000);
		busStopInputVo.setLevel(1);
		busStopInputVo.setNonstopYn("N");
		busStopInputVo.setVirtualStopYn("N");
		busStopInputVo.setCenterStopYn("N");
		busStopInputVo.setSido("서울시");
		busStopInputVo.setGu("영등포구");
		busStopInputVo.setDong("당산동");
		busStopInputVo.setRoadAddress("당산로41길 11");
		busStopInputVo.setRoadName("당산로41길");
		busStopInputVo.setBonbun("11");
		busStopInputVo.setBoobun("2");
		busStopInputVo.setLocalStopId("100000031");
		busStopInputVo.setProviderId(4);
		busStopInputVo.setDisplayId("01123");
		busStopInputVo.setTaskComment("정류장 등록");
		busStopInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busStopInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busStopInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);

		mockMvc.perform(post("/v1/ntool/api/busStopTask/editTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busStopInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));

	}

	/**
	 * 버스정류장 수정을 위한 Task 등록 - 필수값 오류인 경우
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorBusStopEditTask() throws Exception {
		BusStopTaskInputVo busStopInputVo = new BusStopTaskInputVo();
		busStopInputVo.setStopName("SK v1 정류장");
		busStopInputVo.setLongitude(126.123456);

		mockMvc.perform(post("/v1/ntool/api/busStopTask/editTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busStopInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())));
	}


	/**
	 * 버스정류장 작업 Task 정보 수정 - 성공
	 * @throws Exception
	 */
	@Test
	public void caseBusStopTaskModify() throws Exception {

		BusStopTaskInputVo busStopInputVo = new BusStopTaskInputVo();
		busStopInputVo.setTaskId(1);
		busStopInputVo.setStopId(500849);
		busStopInputVo.setStopName("SK v1 정류장");
		busStopInputVo.setLongitude(126.123456);
		busStopInputVo.setLatitude(34.5678);
		busStopInputVo.setCityCode(1000);
		busStopInputVo.setLevel(1);
		busStopInputVo.setNonstopYn("N");
		busStopInputVo.setVirtualStopYn("N");
		busStopInputVo.setCenterStopYn("N");
		busStopInputVo.setSido("서울시");
		busStopInputVo.setGu("영등포구");
		busStopInputVo.setDong("당산동");
		busStopInputVo.setRoadAddress("당산로41길 11");
		busStopInputVo.setRoadName("당산로41길");
		busStopInputVo.setBonbun("11");
		busStopInputVo.setBoobun("2");
		busStopInputVo.setLocalStopId("100000031");
		busStopInputVo.setProviderId(4);
		busStopInputVo.setDisplayId("01123");
		busStopInputVo.setTaskComment("정류장 등록");
		busStopInputVo.setCheckUserId(this.tokenMap.get("userId"));
		//busStopInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		//busStopInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);

		mockMvc.perform(put("/v1/ntool/api/modify/busStopTask")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            .content(objectMapper.writeValueAsString(busStopInputVo))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 버스정류장 작업 Task 정보 수정 - 일치하는 작업 정보가 없을때
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchBusStopTaskModifyError() throws Exception {

		BusStopTaskInputVo busStopInputVo = new BusStopTaskInputVo();
		busStopInputVo.setTaskId(-1);
		busStopInputVo.setStopId(5);
		busStopInputVo.setStopName("SK v1 정류장");
		busStopInputVo.setLongitude(126.123456);
		busStopInputVo.setLatitude(34.5678);
		busStopInputVo.setCityCode(1000);
		busStopInputVo.setLevel(1);
		busStopInputVo.setNonstopYn("N");
		busStopInputVo.setVirtualStopYn("N");
		busStopInputVo.setCenterStopYn("N");
		busStopInputVo.setSido("서울시");
		busStopInputVo.setGu("영등포구");
		busStopInputVo.setDong("당산동");
		busStopInputVo.setRoadAddress("당산로41길 11");
		busStopInputVo.setRoadName("당산로41길");
		busStopInputVo.setBonbun("11");
		busStopInputVo.setBoobun("2");
		busStopInputVo.setLocalStopId("100000031");
		busStopInputVo.setProviderId(4);
		busStopInputVo.setDisplayId("01123");
		busStopInputVo.setTaskComment("정류장 등록");
		busStopInputVo.setCheckUserId(this.tokenMap.get("userId"));

		mockMvc.perform(put("/v1/ntool/api/modify/busStopTask")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            .content(objectMapper.writeValueAsString(busStopInputVo))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}

	/**
	 * 버스정류장 작업 Task 정보 수정 - 실패
	 * @throws Exception
	 */
	@Test
	public void caseBusStopTaskModifyError() throws Exception {

		BusStopTaskInputVo busStopInputVo = new BusStopTaskInputVo();
		busStopInputVo.setTaskId(1);
		busStopInputVo.setStopId(5);
		busStopInputVo.setStopName("SK v1 정류장");
		busStopInputVo.setLongitude(126.123456);
		busStopInputVo.setCityCode(1000);
		busStopInputVo.setLevel(1);
		busStopInputVo.setNonstopYn("N");
		busStopInputVo.setVirtualStopYn("N");
		busStopInputVo.setCenterStopYn("N");
		busStopInputVo.setSido("서울시");
		busStopInputVo.setGu("영등포구");
		busStopInputVo.setDong("당산동");
		busStopInputVo.setRoadAddress("당산로41길 11");
		busStopInputVo.setRoadName("당산로41길");
		busStopInputVo.setBonbun("11");
		busStopInputVo.setBoobun("2");
		busStopInputVo.setLocalStopId("100000031");
		busStopInputVo.setProviderId(4);
		busStopInputVo.setDisplayId("01123");
		busStopInputVo.setTaskComment("정류장 등록");
		busStopInputVo.setCheckUserId(this.tokenMap.get("userId"));

		mockMvc.perform(put("/v1/ntool/api/modify/busStopTask")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            .content(objectMapper.writeValueAsString(busStopInputVo))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.INNER_FAIL.getApiErrorCode())));
	}

	/**
	 * 버스정류장 삭제요청 Task 등록 - 성공
	 * @throws Exception
	 */
	@Test
	public void caseBusStopRemoveTask() throws Exception {
		BusStopRemoveTaskInputVo busStopRemoveInputVo = new BusStopRemoveTaskInputVo() ;
		busStopRemoveInputVo.setStopId(55000848);
		busStopRemoveInputVo.setTaskComment("미존재 정류장 - 삭제 처리");
		busStopRemoveInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busStopRemoveInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busStopRemoveInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);

		mockMvc.perform(post("/v1/ntool/api/busStopTask/removeTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busStopRemoveInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 버스정류장 삭제요청 Task 등록 - 실패(해당 정류장이 없는경우)
	 * @throws Exception
	 */
	@Test
	public void caseBusStopRemoveTaskError() throws Exception {
		BusStopRemoveTaskInputVo busStopRemoveInputVo = new BusStopRemoveTaskInputVo() ;
		busStopRemoveInputVo.setStopId(3);
		busStopRemoveInputVo.setTaskComment("미존재 정류장 - 삭제 처리");
		busStopRemoveInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busStopRemoveInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busStopRemoveInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);

		mockMvc.perform(post("/v1/ntool/api/busStopTask/removeTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busStopRemoveInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}

	/**
	 * 버스정류장 삭제요청 Task 등록 - 실패(경유 노선이 있는경우)
	 * @throws Exception
	 */
	@Test
	public void caseBusStopRemoveTaskWithRouteError() throws Exception {
		BusStopRemoveTaskInputVo busStopRemoveInputVo = new BusStopRemoveTaskInputVo() ;
		busStopRemoveInputVo.setStopId(55000000);
		busStopRemoveInputVo.setTaskComment("미존재 정류장 - 삭제 처리");
		busStopRemoveInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busStopRemoveInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busStopRemoveInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);


		mockMvc.perform(post("/v1/ntool/api/busStopTask/removeTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busStopRemoveInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.STOP_REMOVE_EXISTS_BUS_ROUTE.getApiErrorCode())));
	}

	/**
	 * Task 정보 Insert 실패 테스트
	 * @throws Exception
	 */
	@Test
	public void insertFailTask() throws Exception {

		TaskInputVo taskInputVo = new TaskInputVo();
		taskInputVo.setPubTransId(0);
		taskInputVo.setProviderId(0);
		taskInputVo.setTaskStatusType(TaskStatusType.PROGRESS);
		taskInputVo.setPubTransType(PubTransType.STOP);
		taskInputVo.setPubTransName("테스트 정류장");

		assertThrows(DataAccessException.class, () -> {
			taskService.registerTaskInfoAll(taskInputVo);
		});

	}

	/**
	 * 버스정류장 등록 Task Insert 실패 테스트
	 * @throws Exception
	 */
	@Test
	public void insertFailBusStopTask() throws Exception {

		BusStopTaskInputVo busStopTaskInputVo = new BusStopTaskInputVo();
		busStopTaskInputVo.setLongitude(126.123456);
		busStopTaskInputVo.setLatitude(34.5678);
		busStopTaskInputVo.setCityCode(1000);
		busStopTaskInputVo.setLevel(1);

		assertThrows(DataAccessException.class, () -> {
			busStopRepository.insertBusStopTask(busStopTaskInputVo);
		});
	}


	/**
	 * 지도영역의 버스정류장 목록 - 데이터가 존재할때
	 * @throws Exception
	 */
	@Test
	public void caseExistsBusStopListFromMapBounds() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busStopFromMapBounds")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.param("rightTopCoordinates", "126.982676,37.575861")
            	.param("leftBottomCoordinates", "126.983191,37.569500")
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(not(hasSize(0)))));
	}

	/**
	 * 지도영역의 버스정류장 목록 - 데이터가 존재하지 않을때
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsBusStopListFromMapBounds() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busStopFromMapBounds")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.param("rightTopCoordinates", "126.879366,37.608285")
            	.param("leftBottomCoordinates", "126.894129,37.601162")
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(hasSize(0))));
	}

	/**
	 * 지도영역의 버스정류장 목록 유효성 검사를 진행한다 - 좌표정보가 잘못되었을 경우
	 * @throws Exception
	 */
	@Test
	public void verifyBusStopListFromMapBounds() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busStopFromMapBounds")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.param("rightTopCoordinates", "126.879366,37.608285,37.608285")
        	.param("leftBottomCoordinates", "126.894129,37.601162")
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_RULE_ERROR.getApiErrorCode())))
			.andExpect(jsonPath("$.message", is(ResultCode.PARAMETER_RULE_ERROR.getDisplayMessage())));
	}
}
