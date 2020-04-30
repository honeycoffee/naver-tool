package com.naver.pubtrans.itn.api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedHashMap;
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
import org.springframework.dao.DataAccessException;
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
import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.repository.BusCompanyRepository;
import com.naver.pubtrans.itn.api.service.TaskService;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanyRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanyTaskInputVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.task.input.TaskInputVo;


/**
 * 버수 운수사 관리 Test
 * @author westwind
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class BusCompanyControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@Autowired
	private TaskService taskService;

	@Autowired
	private BusCompanyRepository busCompanyRepository;

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
	 * 버스 운수사 목록 - 데이터가 존재할때
	 * @throws Exception
	 */
	@Test
	public void caseExistsBusCompanyList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busCompany")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.param("companyName", "")
            	.param("cityCode", "")
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
	 * 버스 운수사 목록 - 데이터가 존재하지 않을때
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsBusCompanyList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busCompany")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.param("companyName", "버스 운수사 명칭")
            	.param("cityCode", "2000")
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
	 * 버스 운수사 상세정보 - 일치하는 버스 운수사가 있을경우
	 * @throws Exception
	 */
	@Test
	public void caseMatchBusCompanyInfo() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busCompany/{companyId}", 1)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data", is(notNullValue())));
	}

	/**
	 * 버스 운수사 상세정보 - 일치하는 버스 운수사가 없을경우
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchBusCompanyInfo() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busCompany/{companyId}", 0)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}

	/**
	 * 버스 운수사 Task 정보 - 일치하는 Task 정보가 존재하는 경우
	 * @throws Exception
	 */
	@Test
	@Order(2)
	public void caseMatchBusCompanyTask() throws Exception {
		if(TASK_ID == 0) {
			this.caseBusCompanyAddTask();
		}

		mockMvc.perform(get("/v1/ntool/api/info/busCompanyTask/{taskId}", 880)
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
				.andExpect(jsonPath("$.result.data.taskInfo", is(notNullValue())));
	}

	/**
	 * 버스 운수사 Task 정보 - 일치하는 Task 정보가 없는 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchBusCompanyTask() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/info/busCompanyTask/{taskId}", 2000)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}

	/**
	 * 버스 운수사 Task 요약 목록 - 목록이 존재하는 경우
	 * @throws Exception
	 */
	@Test
	public void caseExistsBusCompanyTaskList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busCompanyTask/summary/{companyId}", 1)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
            .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(not(hasSize(0)))));
	}

	/**
	 * 버스 운수사 Task 요약 목록 - 목록이 존재하지 않는 경우
	 * @throws Exception
	 */
	@Test
	public void caseNotExistsBusCompanyTaskList() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/list/busCompanyTask/summary/{companyId}", -1)
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
            .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.data", is(hasSize(0))));
	}

	/**
	 * 버스 운수사 테이블 스키마 조회
	 * @throws Exception
	 */
	@Test
	public void caseBusCompanySchema() throws Exception {
		mockMvc.perform(get("/v1/ntool/api/schema/busCompany")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .characterEncoding("UTF-8"))
            .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
			.andExpect(jsonPath("$.result.schema", is(not(hasSize(0)))));
	}

	/**
	 * 버스 운수사 생성을 위한 Task 등록 - 정상인 경우
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	public void caseBusCompanyAddTask() throws Exception {

		BusCompanyTaskInputVo busCompanyTaskInputVo = new BusCompanyTaskInputVo() ;
		busCompanyTaskInputVo.setCompanyName("jUnit 운수사 등록 Test");
		busCompanyTaskInputVo.setTel("010-1234-5678");
		busCompanyTaskInputVo.setAddressName("서울시 영등포구 당산로41길 11");
		busCompanyTaskInputVo.setCityCode(1000);
		busCompanyTaskInputVo.setSourceName("네이버");
		busCompanyTaskInputVo.setSourceUrl("www.naver.com");
		busCompanyTaskInputVo.setComment("테스트 등록");
		busCompanyTaskInputVo.setTaskComment("운수사 등록");
		busCompanyTaskInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busCompanyTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busCompanyTaskInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);


		MvcResult mvcResult = mockMvc.perform(post("/v1/ntool/api/busCompanyTask/addTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busCompanyTaskInputVo))
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
	 * 버스 운수사 생성을 위한 Task 등록 - 필수값 오류인 경우
	 * @throws Exception
	 */
	@Test
	public void caseValidErrorBusCompanyAddTask() throws Exception {

		BusCompanyTaskInputVo busCompanyTaskInputVo = new BusCompanyTaskInputVo() ;
		busCompanyTaskInputVo.setCompanyName("jUnit 운수사 등록 Test");
		busCompanyTaskInputVo.setTel("010-1234-5678");

		mockMvc.perform(post("/v1/ntool/api/busCompanyTask/addTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busCompanyTaskInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_ERROR.getApiErrorCode())));
	}

	/**
	 * 버스 운수사 수정을 위한 Task 등록 - 정상
	 * @throws Exception
	 */
	@Test
	public void caseBusCompanyEditTask() throws Exception {

		BusCompanyTaskInputVo busCompanyTaskInputVo = new BusCompanyTaskInputVo() ;
		busCompanyTaskInputVo.setCompanyId(1);
		busCompanyTaskInputVo.setCompanyName("jUnit 운수사 등록 Test");
		busCompanyTaskInputVo.setTel("010-1234-5678");
		busCompanyTaskInputVo.setAddressName("서울시 영등포구 당산로41길 11");
		busCompanyTaskInputVo.setCityCode(1000);
		busCompanyTaskInputVo.setSourceName("네이버");
		busCompanyTaskInputVo.setSourceUrl("www.naver.com");
		busCompanyTaskInputVo.setComment("테스트 등록");
		busCompanyTaskInputVo.setTaskComment("운수사 등록");
		busCompanyTaskInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busCompanyTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busCompanyTaskInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);

		mockMvc.perform(post("/v1/ntool/api/busCompanyTask/editTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busCompanyTaskInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));

	}

	/**
	 * 버스 운수사 작업 Task 정보 수정 - 성공
	 * @throws Exception
	 */
	@Test
	public void caseBusCompanyTaskModify() throws Exception {

		BusCompanyTaskInputVo busCompanyTaskInputVo = new BusCompanyTaskInputVo() ;
		busCompanyTaskInputVo.setTaskId(951);
		busCompanyTaskInputVo.setCompanyId(1);
		busCompanyTaskInputVo.setCompanyName("jUnit 운수사 등록 Test");
		busCompanyTaskInputVo.setTel("010-1234-5678");
		busCompanyTaskInputVo.setAddressName("서울시 영등포구 당산로41길 11");
		busCompanyTaskInputVo.setCityCode(1000);
		busCompanyTaskInputVo.setSourceName("네이버");
		busCompanyTaskInputVo.setSourceUrl("www.naver.com");
		busCompanyTaskInputVo.setComment("테스트 등록");
		busCompanyTaskInputVo.setTaskComment("운수사 등록");
		busCompanyTaskInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busCompanyTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busCompanyTaskInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);


		mockMvc.perform(put("/v1/ntool/api/modify/busCompanyTask")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            .content(objectMapper.writeValueAsString(busCompanyTaskInputVo))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 버스 운수사 작업 Task 정보 수정 - 일치하는 작업 정보가 없을때
	 * @throws Exception
	 */
	@Test
	public void caseNotMatchBusCompanyTaskModifyError() throws Exception {

		BusCompanyTaskInputVo busCompanyTaskInputVo = new BusCompanyTaskInputVo() ;
		busCompanyTaskInputVo.setTaskId(99999999999999999L);
		busCompanyTaskInputVo.setCompanyId(1);
		busCompanyTaskInputVo.setCompanyName("jUnit 운수사 등록 Test");
		busCompanyTaskInputVo.setTel("010-1234-5678");
		busCompanyTaskInputVo.setAddressName("서울시 영등포구 당산로41길 11");
		busCompanyTaskInputVo.setCityCode(1000);
		busCompanyTaskInputVo.setSourceName("네이버");
		busCompanyTaskInputVo.setSourceUrl("www.naver.com");
		busCompanyTaskInputVo.setComment("테스트 등록");
		busCompanyTaskInputVo.setTaskComment("운수사 등록");
		busCompanyTaskInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busCompanyTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busCompanyTaskInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);

		mockMvc.perform(put("/v1/ntool/api/modify/busCompanyTask")
			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
            .content(objectMapper.writeValueAsString(busCompanyTaskInputVo))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}
	/**
	 * 버스 운수사 삭제요청 Task 등록 - 성공
	 * @throws Exception
	 */
	@Test
	public void caseBusCompanyRemoveTask() throws Exception {
		BusCompanyRemoveTaskInputVo busCompanyRemoveInputVo = new BusCompanyRemoveTaskInputVo() ;
		busCompanyRemoveInputVo.setCompanyId(1);
		busCompanyRemoveInputVo.setTaskComment("테스트 삭제 처리");
		busCompanyRemoveInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busCompanyRemoveInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busCompanyRemoveInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);

		mockMvc.perform(post("/v1/ntool/api/busCompanyTask/removeTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busCompanyRemoveInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())));
	}

	/**
	 * 버스 운수사 삭제요청 Task 등록 - 실패(해당 운수사가 없는경우)
	 * @throws Exception
	 */
	@Test
	public void caseBusCompanyRemoveTaskError() throws Exception {
		BusCompanyRemoveTaskInputVo busCompanyRemoveInputVo = new BusCompanyRemoveTaskInputVo() ;
		busCompanyRemoveInputVo.setCompanyId(-1);
		busCompanyRemoveInputVo.setTaskComment("테스트 삭제 처리");
		busCompanyRemoveInputVo.setCheckUserId(this.tokenMap.get("userId"));
		busCompanyRemoveInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		busCompanyRemoveInputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);

		mockMvc.perform(post("/v1/ntool/api/busCompanyTask/removeTask")
				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
                .content(objectMapper.writeValueAsString(busCompanyRemoveInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())));
	}

}
