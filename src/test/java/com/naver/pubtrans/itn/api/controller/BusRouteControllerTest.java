//package com.naver.pubtrans.itn.api.controller;
//
//import static org.hamcrest.Matchers.*;
//import static org.junit.Assert.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.util.LinkedHashMap;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import com.naver.pubtrans.itn.api.auth.JwtAdapter;
//import com.naver.pubtrans.itn.api.common.ApiUtils;
//import com.naver.pubtrans.itn.api.consts.CommonConstant;
//import com.naver.pubtrans.itn.api.consts.ResultCode;
//import com.naver.pubtrans.itn.api.service.BusRouteService;
//import com.naver.pubtrans.itn.api.vo.bus.graph.output.GeoJsonOutputVo;
//import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;
//
///**
// * 버스 노선관리 테스트
// * @author adtec10
// *
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class BusRouteControllerTest {
//
//	@Autowired
//    private MockMvc mockMvc;
//
//	@Autowired
//    private ObjectMapper objectMapper;
//	
//	@Autowired
//	private BusRouteService busRouteService;
//
//	private ApiUtils apiUtils;
//	
//	// 테스트 header에 전달 될 Token Map
//	private LinkedHashMap<String, String> tokenMap;
//
//	@Before
//	public void setup() throws Exception {
//		//Api Test Utils 초기화
//		apiUtils = new ApiUtils(mockMvc, objectMapper);
//		
//		tokenMap = apiUtils.getTokenMap();
//	}
//
//	/**
//	 * 버스 노선목록 - 데이터가 존재할때
//	 * @throws Exception
//	 */
//	@Test
//	public void caseExistsBusRouteList() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/list/busRoute")
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.param("routeName", "")
//            	.param("cityCode", "1000")
//            	.param("pageNo", "1")
//            	.param("listSize", "20")
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(200)))
//				.andExpect(jsonPath("$.result.data", is(not(hasSize(0)))));
//	}
//
//	/**
//	 * 버스 노선목록 - 검색 데이터가 없을때
//	 * @throws Exception
//	 */
//	@Test
//	public void caseNotExistsBusRouteList() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/list/busRoute")
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.param("routeName", "11")
//            	.param("cityCode", "3000")
//            	.param("pageNo", "1")
//            	.param("listSize", "20")
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(200)))
//				.andExpect(jsonPath("$.result.data", is(hasSize(0))));
//	}
//
//	/**
//	 * 버스 노선 작업목록 - 데이터가 존재할때
//	 * @throws Exception
//	 */
//	@Test
//	public void caseExistsBusRouteTaskList() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/list/busRouteTask/{busRouteId}", 11000000)
//			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//        	.param("pageNo", "1")
//        	.param("listSize", "20")
//        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .characterEncoding("UTF-8"))
//			.andDo(print())
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.code", is(200)))
//			.andExpect(jsonPath("$.result.data", is(not(hasSize(0)))));
//
//	}
//
//	/**
//	 * 버스 노선 작업목록 - 데이터가 없을때
//	 * @throws Exception
//	 */
//	@Test
//	public void caseNotExistsBusRouteTaskList() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/list/busRouteTask/{busRouteId}", 11000005)
//			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//        	.param("pageNo", "1")
//        	.param("listSize", "20")
//        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .characterEncoding("UTF-8"))
//			.andDo(print())
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.code", is(200)))
//			.andExpect(jsonPath("$.result.data", is(hasSize(0))));
//
//	}
//
//	/**
//	 * 연속되는 정류장간의 그래프 정보 목록 - 그래프 데이터 존재시
//	 * @throws Exception
//	 */
//	@Test
//	public void caseExistsGraphBetweenBusStop() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/list/busStopsGraph")
//			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//        	.param("busStopIds", "55000837,55000520,55000529,55000845,55000524")
//        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .characterEncoding("UTF-8"))
//			.andDo(print())
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.code", is(200)))
//			.andExpect(jsonPath("$.result.data.features", is(hasSize(4))));
//
//	}
//
//	/**
//	 * 연속되는 정류장간의 그래프 정보 목록 - 데이터 미 존재시
//	 * @throws Exception
//	 */
//	@Test
//	public void caseNotExistsGraphBetweenBusStop() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/list/busStopsGraph")
//			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//        	.param("busStopIds", "55000005,55000009")
//        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .characterEncoding("UTF-8"))
//			.andDo(print())
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.code", is(200)))
//			.andExpect(jsonPath("$.result.data.features[0].properties.matchGraphInfoYn", is("N")));
//
//
//	}
//
//	/**
//	 * 연속되는 정류장간의 그래프 정보 목록 - 파라미터 오류
//	 * @throws Exception
//	 */
//	@Test
//	public void caseExistsGraphBetweenBusStopParameterError() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/list/busStopsGraph")
//			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//        	.param("busStopIds", "55000837")
//        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .characterEncoding("UTF-8"))
//			.andDo(print())
//			.andExpect(status().is5xxServerError())
//			.andExpect(jsonPath("$.code", is(ResultCode.PARAMETER_RULE_ERROR.getApiErrorCode())))
//			.andExpect(jsonPath("$.message", is(ResultCode.PARAMETER_RULE_ERROR.getDisplayMessage())));
//	}
//
//	/**
//	 * 버스노선 상세정보 테이블 스키마 조회
//	 * @throws Exception
//	 */
//	@Test
//	public void busRouteSchema() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/schema/busRoute")
//			.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .characterEncoding("UTF-8"))
//            .andDo(print())
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//			.andExpect(jsonPath("$.result.schema", is(not(hasSize(0)))));
//	}
//
//	/**
//	 * 버스노선 상세정보 - 일치하는 정보가 있을경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseMatchBusRouteInfo() throws Exception {
//		// TODO 버스노선 생성 및 배포부분 개발 완료후 TC 연계하여 노선ID를 가져와 테스트 할 수 있도록 수정 필요
//		mockMvc.perform(get("/v1/ntool/api/info/busRoute/{routeId}", 11000000)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//				.andExpect(jsonPath("$.result.data", is(notNullValue())));
//	}
//
//	/**
//	 * 버스노선 상세정보 - 일치하는 노선 정보가 없을경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseNonMatchBusRouteInfo() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/info/busRoute/{routeId}", 12000000)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().is5xxServerError())
//				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())))
//				.andExpect(jsonPath("$.message", is(ResultCode.NOT_MATCH.getDisplayMessage())));
//	}
//
//	/**
//	 * 버스노선 상세정보 - 우회노선 목록을 가지고 있는경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseMatchBusRouteInfoWithBypassChildren() throws Exception {
//		// TODO 버스노선 생성 및 배포부분 개발 완료후 TC 연계하여 노선ID를 가져와 테스트 할 수 있도록 수정 필요
//		mockMvc.perform(get("/v1/ntool/api/info/busRoute/{routeId}", 11000009)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//				.andExpect(jsonPath("$.result.data.bypassChildList", is(not(hasSize(0)))));
//	}
//
//	/**
//	 * 버스노선 상세정보 - 우회노선 목록이 없는경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseNotMatchBusRouteInfoWithBypassChildren() throws Exception {
//		// TODO 버스노선 생성 및 배포부분 개발 완료후 TC 연계하여 노선ID를 가져와 테스트 할 수 있도록 수정 필요
//		mockMvc.perform(get("/v1/ntool/api/info/busRoute/{routeId}", 11000000)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//				.andExpect(jsonPath("$.result.data.bypassChildList", is(nullValue())));
//	}
//
//	/**
//	 * 버스노선 작업 상세정보 - 일치하는 정보가 있을경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseMatchBusRouteTaskInfo() throws Exception {
//
//		// TODO 버스노선 Task 등록 개발 완료시, 해당 태스트를 통해 등록된 taskId를 이용하여 호출할수 있도록 변경 필요.
//		int taskId = 122;
//
//		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", taskId)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//				.andExpect(jsonPath("$.result.data", is(notNullValue())));
//	}
//
//	/**
//	 * 버스노선 작업 상세정보 - 일치하는 정보가 없을경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseNotMatchBusRouteTaskInfo() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 0)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().is5xxServerError())
//				.andExpect(jsonPath("$.code", is(ResultCode.NOT_MATCH.getApiErrorCode())))
//				.andExpect(jsonPath("$.message", is(ResultCode.NOT_MATCH.getDisplayMessage())));
//	}
//
//	/**
//	 * 버스노선 작업 상세정보 - 우회노선인 경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseMatchBusRouteTaskInfoWithBypassChildren() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 130)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//				.andExpect(jsonPath("$.result.data.bypassYn", is(CommonConstant.Y)));
//	}
//
//	/**
//	 * 버스노선 작업 상세정보 - 우회노선이 아닌경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseNotMatchBusRouteTaskInfoWithBypassChildren() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 129)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//				.andExpect(jsonPath("$.result.data.bypassYn", is(CommonConstant.N)));
//	}
//
//	/**
//	 * 버스노선 작업 상세정보 - 우회노선 목록을 가지고 있는경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseMatchBusRouteInfoTaskWithBypassChildren() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 129)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//				.andExpect(jsonPath("$.result.data.bypassChildrenList", is(not(hasSize(0)))));
//	}
//
//	/**
//	 * 버스노선 작업 상세정보 - 우회노선 목록이 없는경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseNotMatchBusRouteInfoTaskWithBypassChildren() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 130)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//				.andExpect(jsonPath("$.result.data.bypassChildrenList", is(nullValue())));
//	}
//
//	/**
//	 * 버스노선 작업정보에 그래프 정보가 포함되어 있는지 확인한다
//	 * @throws Exception
//	 */
//	@Test
//	public void busGraphTask() throws Exception {
//		TaskOutputVo taskOutputVo = new TaskOutputVo();
//		taskOutputVo.setTaskId(122);	// 경유 정류장 정보가 수정된 Task ID
//		taskOutputVo.setPubTransId(11000000);
//
//		GeoJsonOutputVo geoJsonOutputVo = busRouteService.getBusRouteGraphTaskInfo(taskOutputVo);
//
//		int graphRouteCnt = geoJsonOutputVo.getFeatures().size();
//		int graphRouteCheckCnt = 69;		// Task에 저장된 경유장류장 수
//
//		assertEquals(graphRouteCnt, graphRouteCheckCnt);
//	}
//
//	/**
//	 * 버스노선 작업정보에 그래프 정보가 미포함인지 확인한다
//	 * @throws Exception
//	 */
//	@Test
//	public void busGraphTaskWithoutTask() throws Exception {
//		TaskOutputVo taskOutputVo = new TaskOutputVo();
//		taskOutputVo.setTaskId(131);	// 경유 정류장 정보가 수정되지 않은 Task ID
//		taskOutputVo.setPubTransId(11000000);
//
//		GeoJsonOutputVo geoJsonOutputVo = busRouteService.getBusRouteGraphTaskInfo(taskOutputVo);
//
//		int graphRouteCnt = geoJsonOutputVo.getFeatures().size();
//		int graphRouteCheckCnt = 71;		// 해당 노선이 서비스중인 경유 정류장 수
//
//		assertEquals(graphRouteCnt, graphRouteCheckCnt);
//	}
//
//
//	/**
//	 * 버스노선 작업정보 - BIS 변경사항으로 경유정류장이 변경되었을경우 모든 정류장 ID가 내부 정류장 ID와 일치할 경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseMatchBusRouteInfoTaskWithBisAutoGraph() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 129)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//				.andExpect(jsonPath("$.result.data.taskInfo.bisChangeDataInfo.busStopGraphInfo.features", is(hasSize(2))));
//	}
//
//	/**
//	 * 버스노선 작업정보 - BIS 변경사항으로 경유정류장이 변경되었을때 BIS 정류장ID가 내부 ID와 일치하지 않은경우
//	 * @throws Exception
//	 */
//	@Test
//	public void caseNonMatchBusRouteInfoTaskWithBisAutoGraph() throws Exception {
//		mockMvc.perform(get("/v1/ntool/api/info/busRouteTask/{taskId}", 130)
//				.header(JwtAdapter.HEADER_NAME, this.tokenMap.get(CommonConstant.ACCESS_TOKEN_KEY))
//            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .characterEncoding("UTF-8"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.code", is(ResultCode.OK.getApiErrorCode())))
//				.andExpect(jsonPath("$.result.data.taskInfo.bisChangeDataInfo", is(not(hasKey("busStopGraphInfo")))));
//	}
//}
