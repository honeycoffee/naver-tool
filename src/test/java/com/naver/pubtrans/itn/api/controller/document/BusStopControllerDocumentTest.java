package com.naver.pubtrans.itn.api.controller.document;

import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.controller.BusStopController;
import com.naver.pubtrans.itn.api.handler.MemberAccessDeniedHandler;
import com.naver.pubtrans.itn.api.service.BusStopService;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusRouteVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopListOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopOutputVoWithRoute;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopTaskOutputVoWithRoute;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusRouteOutputVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskSummaryOutputVo;

@RunWith(SpringRunner.class)
@WebMvcTest(BusStopController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class BusStopControllerDocumentTest {

	private static final List<JsonFieldType> STRING_OR_NULL = Arrays.asList(JsonFieldType.STRING, JsonFieldType.NULL) ;
	private static final List<JsonFieldType> NUMBER_OR_NULL = Arrays.asList(JsonFieldType.NUMBER, JsonFieldType.NULL) ;
	private static final List<JsonFieldType> OBJECT_OR_NULL = Arrays.asList(JsonFieldType.OBJECT, JsonFieldType.NULL) ;


	@Autowired
	MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@MockBean
	private BusStopService busStopService ;

	@MockBean
	private OutputFmtUtil outputFmtUtil ;

	@MockBean
	private JwtAdapter jwtAdapter;

	@MockBean
	private MemberAccessDeniedHandler memberAccessDeniedHandler;



	/**
	 * 버스 정류장 목록 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void listBusStop() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;



		// 리스트 페이징 정보
		PagingVo pagingVo = new PagingVo(100, 1, 20) ;



		// 스키마
		SchemaVo schemaVo = new SchemaVo();
		schemaVo.setColumnName("stop_id");
		schemaVo.setColumnComment("정류장ID");
		schemaVo.setIsNullable("NO");
		schemaVo.setColumnKey("PRI");
		schemaVo.setColumnType("int(11)");


		SchemaVo schemaCityVo = new SchemaVo();
		schemaCityVo.setColumnName("city_code");
		schemaCityVo.setColumnComment("도시코드");
		schemaCityVo.setIsNullable("NO");
		schemaCityVo.setColumnKey("");
		schemaCityVo.setColumnType("string");


		List<SchemaVo> schemaVoList = new ArrayList<>();
		schemaVoList.add(schemaVo) ;
		schemaVoList.add(schemaCityVo) ;



		// 코드 속성 정의
		FieldValue fieldValue = new FieldValue("1000", "서울") ;

		List<FieldValue> FieldValueList = new ArrayList<>();
		FieldValueList.add(fieldValue) ;

		HashMap<String, List<FieldValue>> valuesMap = new HashMap<>() ;
		valuesMap.put("city_code", FieldValueList) ;


		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, valuesMap) ;



		// 목록
		BusStopListOutputVo busStopListOutputVo = new BusStopListOutputVo();
		busStopListOutputVo.setStopId(88);
		busStopListOutputVo.setStopName("금강제화");
		busStopListOutputVo.setLongitude(126.98757);
		busStopListOutputVo.setLatitude(37.570551);
		busStopListOutputVo.setCityCode(1000);
		busStopListOutputVo.setCityName("서울");


		List<BusStopListOutputVo> busStopListOutputVoList = new ArrayList<>();
		busStopListOutputVoList.add(busStopListOutputVo) ;




		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, busStopListOutputVoList) ;


		//given
		given(busStopService.getBusStopList(any(BusStopSearchVo.class)))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/busStop")
                	.param("stopName", "")
                	.param("cityCode", "1000")
                	.param("pageNo", "1")
                	.param("listSize", "20")
                	.param("sort", "stopId,asc")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busStop/busStopList",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("stopName").description("[선택]정류장명").optional(),
	            		parameterWithName("stopId").description("[선택]정류장ID").optional(),
	            		parameterWithName("cityCode").description("[선택]도시코드").optional(),
	            		parameterWithName("pageNo").description("[선택]페이지 번호(기본:1)").optional(),
	            		parameterWithName("listSize").description("[선택]페이지당 목록 수(기본:20)").optional(),
	            		parameterWithName("sort").description("[선택]정렬(기본:목록 첫번째 Key 내림차순) - 사용 예:stopName,asc").optional()
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.meta").type(JsonFieldType.OBJECT).description("페이징 정보 - link:#_데이터_목록_페이징_정보[공통사항 참고]"),
	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"),

	             		fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("정류장 목록"),
	             		fieldWithPath("result.data[].stopId").type(JsonFieldType.NUMBER).description("정류장ID"),
	             		fieldWithPath("result.data[].stopName").type(JsonFieldType.STRING).description("정류장명"),
	             		fieldWithPath("result.data[].longitude").type(JsonFieldType.NUMBER).description("경도"),
	             		fieldWithPath("result.data[].latitude").type(JsonFieldType.NUMBER).description("위도"),
	             		fieldWithPath("result.data[].cityCode").type(JsonFieldType.NUMBER).description("도시코드"),
	             		fieldWithPath("result.data[].cityName").type(JsonFieldType.STRING).description("도시코드명")
	             )
 		));

	}


	/**
	 * 버스정류장 상세정보 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoBusStop() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;


		BusStopOutputVoWithRoute busStopOutputVo = new BusStopOutputVoWithRoute() ;
		busStopOutputVo.setStopId(1);
		busStopOutputVo.setStopName("세종문화회관");
		busStopOutputVo.setLongitude(126.976712);
		busStopOutputVo.setLatitude(37.57266);
		busStopOutputVo.setCityCode(1000);
		busStopOutputVo.setCityName("서울");
		busStopOutputVo.setLevel(0);
		busStopOutputVo.setNonstopYn("N");
		busStopOutputVo.setVirtualStopYn("N");
		busStopOutputVo.setCenterStopYn("N");
		busStopOutputVo.setLocalStopId("100000031");
		busStopOutputVo.setProviderId(4);
		busStopOutputVo.setDisplayId("01126");



		BusRouteVo busStopRouteVo1 = new BusRouteVo() ;
		busStopRouteVo1.setBusClass(1);
		busStopRouteVo1.setBusClassName("간선");
		busStopRouteVo1.setRouteId(1);
		busStopRouteVo1.setRouteName("100");


		BusRouteVo busStopRouteVo2 = new BusRouteVo() ;
		busStopRouteVo2.setBusClass(1);
		busStopRouteVo2.setBusClassName("간선");
		busStopRouteVo2.setRouteId(2);
		busStopRouteVo2.setRouteName("101");

		BusRouteVo busStopRouteVo3 = new BusRouteVo() ;
		busStopRouteVo3.setBusClass(2);
		busStopRouteVo3.setBusClassName("지선");
		busStopRouteVo3.setRouteId(3);
		busStopRouteVo3.setRouteName("7001");

		List<BusRouteVo> busStopRouteVoList = new ArrayList<>();
		busStopRouteVoList.add(busStopRouteVo1) ;
		busStopRouteVoList.add(busStopRouteVo2) ;
		busStopRouteVoList.add(busStopRouteVo3) ;


		BusRouteOutputVo busStopRouteOutputVo = new BusRouteOutputVo(busStopRouteVoList.size(), busStopRouteVoList) ;


		busStopOutputVo.setBusRouteInfo(busStopRouteOutputVo);




		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(busStopOutputVo) ;


		//given
		given(busStopService.getBusStopInfo(1))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/info/busStop/{stopId}", 1)
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busStop/busStopInfo",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            pathParameters(
                        parameterWithName("stopId").description("정류장 ID")
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]").optional(),

	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("정류장 정보"),
	             		fieldWithPath("result.data.stopId").type(JsonFieldType.NUMBER).description("정류장ID"),
	             		fieldWithPath("result.data.stopName").type(JsonFieldType.STRING).description("정류장명"),
	             		fieldWithPath("result.data.longitude").type(JsonFieldType.NUMBER).description("경도"),
	             		fieldWithPath("result.data.latitude").type(JsonFieldType.NUMBER).description("위도"),
	             		fieldWithPath("result.data.cityCode").type(JsonFieldType.NUMBER).description("도시코드"),
	             		fieldWithPath("result.data.cityName").type(JsonFieldType.STRING).description("도시코드명"),
	             		fieldWithPath("result.data.level").type(JsonFieldType.NUMBER).description("정류장 위치구분"),
	             		fieldWithPath("result.data.nonstopYn").type(STRING_OR_NULL).description("미정차정류장 구분(Y/N)"),
	             		fieldWithPath("result.data.virtualStopYn").type(STRING_OR_NULL).description("가성정류장 구분(Y/N)"),
	             		fieldWithPath("result.data.centerStopYn").type(STRING_OR_NULL).description("중앙차로 구분(Y/N)"),
	             		fieldWithPath("result.data.sido").type(STRING_OR_NULL).description("광역시/도명"),
	             		fieldWithPath("result.data.gu").type(STRING_OR_NULL).description("구명"),
	             		fieldWithPath("result.data.dong").type(STRING_OR_NULL).description("동명"),
	             		fieldWithPath("result.data.roadAddress").type(STRING_OR_NULL).description("도로명주소"),
	             		fieldWithPath("result.data.roadName").type(STRING_OR_NULL).description("도로명"),
	             		fieldWithPath("result.data.bonbun").type(STRING_OR_NULL).description("본번"),
	             		fieldWithPath("result.data.boobun").type(STRING_OR_NULL).description("부번"),
	             		fieldWithPath("result.data.localStopId").type(STRING_OR_NULL).description("지자체 정류장ID"),
	             		fieldWithPath("result.data.providerId").type(NUMBER_OR_NULL).description("지자체 코드"),
	             		fieldWithPath("result.data.displayId").type(STRING_OR_NULL).description("버스정류장 부착 ID"),


	             		fieldWithPath("result.data.busRouteInfo").type(OBJECT_OR_NULL).description("경유 노선정보"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteCnt").type(JsonFieldType.NUMBER).description("노선수"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteList[]").type(JsonFieldType.ARRAY).description("노선정보"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteList[].busClass").type(JsonFieldType.NUMBER).description("노선타입"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteList[].busClassName").type(JsonFieldType.STRING).description("노선타입명"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteList[].routeId").type(JsonFieldType.NUMBER).description("노선ID"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteList[].routeName").type(JsonFieldType.STRING).description("노선명")

	             )
 		));

	}


	/**
	 * 버스정류장 상세정보 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoBusStopTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;


		BusStopTaskOutputVoWithRoute busStopTaskOutputVo = new BusStopTaskOutputVoWithRoute() ;
		busStopTaskOutputVo.setStopId(1);
		busStopTaskOutputVo.setStopName("세종문화회관");
		busStopTaskOutputVo.setLongitude(126.976712);
		busStopTaskOutputVo.setLatitude(37.57266);
		busStopTaskOutputVo.setCityCode(1000);
		busStopTaskOutputVo.setCityName("서울");
		busStopTaskOutputVo.setLevel(0);
		busStopTaskOutputVo.setNonstopYn("N");
		busStopTaskOutputVo.setVirtualStopYn("N");
		busStopTaskOutputVo.setCenterStopYn("N");
		busStopTaskOutputVo.setLocalStopId("100000031");
		busStopTaskOutputVo.setProviderId(4);
		busStopTaskOutputVo.setDisplayId("01126");



		TaskOutputVo taskOutputVo = new TaskOutputVo() ;
		taskOutputVo.setTaskId(1);
		taskOutputVo.setProviderId(4);
		taskOutputVo.setProviderName("서울");
		taskOutputVo.setTaskType("modify");
		taskOutputVo.setTaskStatus("01");
		taskOutputVo.setPubTransId(1);
		taskOutputVo.setTaskDataType("stop");
		taskOutputVo.setTaskDataName("세종문화회관");
		taskOutputVo.setTaskComment("정류장 이름변경");
		taskOutputVo.setTaskRegisterType("M");
		taskOutputVo.setRegUserName("안경현");
		taskOutputVo.setRegUserId("kr94666");
		taskOutputVo.setRegDate("2020-03-06 13:45:49");
		taskOutputVo.setWorkUserName("안경현");
		taskOutputVo.setWorkUserId("kr94666");
		taskOutputVo.setWorkAssignDate("2020-03-06 13:45:14");
		taskOutputVo.setWorkCompleteDate("2020-03-06 13:45:29");



		BusRouteVo busStopRouteVo1 = new BusRouteVo() ;
		busStopRouteVo1.setBusClass(1);
		busStopRouteVo1.setBusClassName("간선");
		busStopRouteVo1.setRouteId(1);
		busStopRouteVo1.setRouteName("100");


		BusRouteVo busStopRouteVo2 = new BusRouteVo() ;
		busStopRouteVo2.setBusClass(1);
		busStopRouteVo2.setBusClassName("간선");
		busStopRouteVo2.setRouteId(2);
		busStopRouteVo2.setRouteName("101");

		BusRouteVo busStopRouteVo3 = new BusRouteVo() ;
		busStopRouteVo3.setBusClass(2);
		busStopRouteVo3.setBusClassName("지선");
		busStopRouteVo3.setRouteId(3);
		busStopRouteVo3.setRouteName("7001");

		List<BusRouteVo> busStopRouteVoList = new ArrayList<>();
		busStopRouteVoList.add(busStopRouteVo1) ;
		busStopRouteVoList.add(busStopRouteVo2) ;
		busStopRouteVoList.add(busStopRouteVo3) ;


		BusRouteOutputVo busStopRouteOutputVo = new BusRouteOutputVo(busStopRouteVoList.size(), busStopRouteVoList) ;

		busStopTaskOutputVo.setTaskInfo(taskOutputVo);
		busStopTaskOutputVo.setBusRouteInfo(busStopRouteOutputVo);




		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(busStopTaskOutputVo) ;


		//given
		given(busStopService.getBusStopTaskInfo(1))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/info/busStopTask/{taskId}", 1L)
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busStop/busStopTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            pathParameters(
                        parameterWithName("taskId").description("작업ID")
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]").optional(),

	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("정류장 정보"),
	             		fieldWithPath("result.data.stopId").type(JsonFieldType.NUMBER).description("정류장ID"),
	             		fieldWithPath("result.data.stopName").type(JsonFieldType.STRING).description("정류장명"),
	             		fieldWithPath("result.data.longitude").type(JsonFieldType.NUMBER).description("경도"),
	             		fieldWithPath("result.data.latitude").type(JsonFieldType.NUMBER).description("위도"),
	             		fieldWithPath("result.data.cityCode").type(JsonFieldType.NUMBER).description("도시코드"),
	             		fieldWithPath("result.data.cityName").type(JsonFieldType.STRING).description("도시코드명"),
	             		fieldWithPath("result.data.level").type(JsonFieldType.NUMBER).description("정류장 위치구분"),
	             		fieldWithPath("result.data.nonstopYn").type(STRING_OR_NULL).description("미정차정류장 구분(Y/N)"),
	             		fieldWithPath("result.data.virtualStopYn").type(STRING_OR_NULL).description("가성정류장 구분(Y/N)"),
	             		fieldWithPath("result.data.centerStopYn").type(STRING_OR_NULL).description("중앙차로 구분(Y/N)"),
	             		fieldWithPath("result.data.sido").type(STRING_OR_NULL).description("광역시/도명"),
	             		fieldWithPath("result.data.gu").type(STRING_OR_NULL).description("구명"),
	             		fieldWithPath("result.data.dong").type(STRING_OR_NULL).description("동명"),
	             		fieldWithPath("result.data.roadAddress").type(STRING_OR_NULL).description("도로명주소"),
	             		fieldWithPath("result.data.roadName").type(STRING_OR_NULL).description("도로명"),
	             		fieldWithPath("result.data.bonbun").type(STRING_OR_NULL).description("본번"),
	             		fieldWithPath("result.data.boobun").type(STRING_OR_NULL).description("부번"),
	             		fieldWithPath("result.data.localStopId").type(STRING_OR_NULL).description("지자체 정류장ID"),
	             		fieldWithPath("result.data.providerId").type(NUMBER_OR_NULL).description("지자체 코드"),
	             		fieldWithPath("result.data.displayId").type(STRING_OR_NULL).description("버스정류장 부착 ID"),

	             		fieldWithPath("result.data.taskInfo").type(JsonFieldType.OBJECT).description("작업정보"),
	             		fieldWithPath("result.data.taskInfo.taskId").type(NUMBER_OR_NULL).description("작업ID (작업정보 존재시)"),
	             		fieldWithPath("result.data.taskInfo.providerId").type(JsonFieldType.NUMBER).description("BIS 지역코드"),
	             		fieldWithPath("result.data.taskInfo.providerName").type(STRING_OR_NULL).description("BIS 지역명"),
	             		fieldWithPath("result.data.taskInfo.taskType").type(STRING_OR_NULL).description("작업구분(register:등록, modify:수정, remove:삭제)"),
	             		fieldWithPath("result.data.taskInfo.taskStatus").type(STRING_OR_NULL).description("작업 진행상태(00:대기, 01:진행, 02:완료, 03:예외)"),
	             		fieldWithPath("result.data.taskInfo.pubTransId").type(JsonFieldType.NUMBER).description("대중교통 ID"),
	             		fieldWithPath("result.data.taskInfo.taskDataType").type(STRING_OR_NULL).description("데이터 종류(stop:정류장, route:노선, company:운수사)"),
	             		fieldWithPath("result.data.taskInfo.taskDataName").type(STRING_OR_NULL).description("데이터 이름"),
	             		fieldWithPath("result.data.taskInfo.taskComment").type(STRING_OR_NULL).description("작업내용"),
	             		fieldWithPath("result.data.taskInfo.bisChangeDataInfo").type(OBJECT_OR_NULL).description("BIS 자동등록 변경내용 - Null이 아닌경우 변경된 데이터 컬럼 정보만 하위 요소로 표출"),
	             		fieldWithPath("result.data.taskInfo.taskRegisterType").type(STRING_OR_NULL).description("작업 등록구분(A:자동, M:수동)"),
	             		fieldWithPath("result.data.taskInfo.regUserName").type(STRING_OR_NULL).description("등록자명"),
	             		fieldWithPath("result.data.taskInfo.regUserId").type(STRING_OR_NULL).description("등록자ID"),
	             		fieldWithPath("result.data.taskInfo.regDate").type(STRING_OR_NULL).description("등록일"),
	             		fieldWithPath("result.data.taskInfo.workUserName").type(STRING_OR_NULL).description("작업자명"),
	             		fieldWithPath("result.data.taskInfo.workUserId").type(STRING_OR_NULL).description("작업자ID"),
	             		fieldWithPath("result.data.taskInfo.workAssignDate").type(STRING_OR_NULL).description("작업자 할당일"),
	             		fieldWithPath("result.data.taskInfo.workCompleteDate").type(STRING_OR_NULL).description("작업 완료일"),
	             		fieldWithPath("result.data.taskInfo.checkUserName").type(STRING_OR_NULL).description("검수자명"),
	             		fieldWithPath("result.data.taskInfo.checkUserId").type(STRING_OR_NULL).description("검수자ID"),
	             		fieldWithPath("result.data.taskInfo.checkAssignDate").type(STRING_OR_NULL).description("검수자 할당일"),
	             		fieldWithPath("result.data.taskInfo.checkCompleteDate").type(STRING_OR_NULL).description("검수일"),

	             		fieldWithPath("result.data.busRouteInfo").type(OBJECT_OR_NULL).description("경유 노선정보"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteCnt").type(JsonFieldType.NUMBER).description("노선수"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteList[]").type(JsonFieldType.ARRAY).description("노선정보"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteList[].busClass").type(JsonFieldType.NUMBER).description("노선타입"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteList[].busClassName").type(JsonFieldType.STRING).description("노선타입명"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteList[].routeId").type(JsonFieldType.NUMBER).description("노선ID"),
	             		fieldWithPath("result.data.busRouteInfo.busRouteList[].routeName").type(JsonFieldType.STRING).description("노선명")

	             )
 		));

	}

	/**
	 * 정류장 작업 요약 목록
	 * @throws Exception
	 */
	@Test
	public void listBusStopTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;



		TaskSummaryOutputVo taskSummaryOuputVo1 = new TaskSummaryOutputVo();
		taskSummaryOuputVo1.setTaskId(1);
		taskSummaryOuputVo1.setTaskType("modify");
		taskSummaryOuputVo1.setTaskStatus("01");
		taskSummaryOuputVo1.setTaskDataType("stop");
		taskSummaryOuputVo1.setTaskComment("정류장 이름변경");
		taskSummaryOuputVo1.setRegDate("2020-03-06");
		taskSummaryOuputVo1.setWorkUserName("안경현");

		TaskSummaryOutputVo taskSummaryOuputVo2 = new TaskSummaryOutputVo();
		taskSummaryOuputVo2.setTaskId(2);
		taskSummaryOuputVo2.setTaskType("modify");
		taskSummaryOuputVo2.setTaskStatus("00");
		taskSummaryOuputVo2.setTaskDataType("stop");
		taskSummaryOuputVo2.setTaskComment("DCC 정류장 이름변경");
		taskSummaryOuputVo2.setRegDate("2020-03-09");
		taskSummaryOuputVo2.setWorkUserName("안경현");


		List<TaskSummaryOutputVo> taskSummaryOutputVoList = new ArrayList<>() ;
		taskSummaryOutputVoList.add(taskSummaryOuputVo1) ;
		taskSummaryOutputVoList.add(taskSummaryOuputVo2) ;

		// 페이징 정보
		PagingVo pagingVo = new PagingVo(2, 1, 20) ;

		CommonResult commonResult = outputFmtUtil.setCommonListFmt(pagingVo, taskSummaryOutputVoList);

		//given
		given(busStopService.getBusStopTaskSummaryList(ArgumentMatchers.anyInt(), any()))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/busStopTask/summary/{busStopId}", 55000389)
	                .param("pageNo", "1")
	            	.param("listSize", "20")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busStop/busStopTaskList",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            pathParameters(
                        parameterWithName("busStopId").description("정류장 ID")
	            ),
	            requestParameters(
	            		parameterWithName("pageNo").description("[선택]페이지 번호(기본:1)").optional(),
	            		parameterWithName("listSize").description("[선택]페이지당 목록 수(기본:20)").optional()
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.meta").type(JsonFieldType.OBJECT).description("페이징 정보 - link:#_데이터_목록_페이징_정보[공통사항 참고]"),

	             		fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("작업 정보"),
	             		fieldWithPath("result.data[].taskId").type(JsonFieldType.NUMBER).description("작업ID"),
	             		fieldWithPath("result.data[].taskType").type(JsonFieldType.STRING).description("작업구분(register:등록, modify:수정, remove:삭제)"),
	             		fieldWithPath("result.data[].taskStatus").type(JsonFieldType.STRING).description("작업 진행상태(00:대기, 01:진행, 02:완료, 03:예외)"),
	             		fieldWithPath("result.data[].taskDataType").type(JsonFieldType.STRING).description("데이터 종류(stop:정류장, route:노선, company:운수사)"),
	             		fieldWithPath("result.data[].taskComment").type(JsonFieldType.STRING).description("작업내용"),
	             		fieldWithPath("result.data[].regDate").type(JsonFieldType.STRING).description("등록일"),
	             		fieldWithPath("result.data[].workUserName").type(JsonFieldType.STRING).description("작업자")


	             )
 		));

	}


	/**
	 * 버스정류장 기본 스키마를 조회한다
	 * @throws Exception
	 */
	@Test
	public void schemaBusStop() throws Exception {


		List<CommonSchema> commonSchema = new ArrayList<>() ;




		//given
		given(busStopService.selectBusStopSchemaAll())
				.willReturn(commonSchema) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/schema/busStop")
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busStop/busStopSchema",
	 			getDocumentRequest(),
	            getDocumentResponse(),

	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		subsectionWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보").optional(),

	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]")
	             )
 		));

	}


	/**
	 * 정류장 등록 Task
	 * @throws Exception
	 */
	@Test
	public void registerBusStopAddTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;

		BusStopTaskInputVo busStopInputVo = new BusStopTaskInputVo() ;
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
		busStopInputVo.setCheckUserId("kr94666");


		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, 255);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		//given
		given(busStopService.registerBusStopTask(ArgumentMatchers.anyString(), any()))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/busStopTask/addTask")
	                .content(objectMapper.writeValueAsString(busStopInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busStop/busStopAddTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
                        fieldWithPath("stopName").type(JsonFieldType.STRING).description("[필수]정류장 명칭"),
                        fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("[필수]경도"),
                        fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("[필수]위도"),
                        fieldWithPath("cityCode").type(JsonFieldType.NUMBER).description("[필수]도시코드"),
                        fieldWithPath("level").type(JsonFieldType.NUMBER).description("정류장 위치구분"),
                        fieldWithPath("nonstopYn").type(JsonFieldType.STRING).description("미정차 정류장(Y/N)"),
                        fieldWithPath("virtualStopYn").type(JsonFieldType.STRING).description("가상 정류장(Y/N)"),
                        fieldWithPath("centerStopYn").type(JsonFieldType.STRING).description("중앙차로(Y/N)"),
                        fieldWithPath("sido").type(JsonFieldType.STRING).description("광역시,도명"),
                        fieldWithPath("gu").type(JsonFieldType.STRING).description("구"),
                        fieldWithPath("dong").type(JsonFieldType.STRING).description("동"),
                        fieldWithPath("roadAddress").type(JsonFieldType.STRING).description("도로명주소"),
                        fieldWithPath("roadName").type(JsonFieldType.STRING).description("도로명"),
                        fieldWithPath("bonbun").type(JsonFieldType.STRING).description("본번"),
                        fieldWithPath("boobun").type(JsonFieldType.STRING).description("부번"),
                        fieldWithPath("localStopId").type(JsonFieldType.STRING).description("지자체 정류장ID"),
                        fieldWithPath("providerId").type(JsonFieldType.NUMBER).description("지자체 BIS ID"),
                        fieldWithPath("displayId").type(JsonFieldType.STRING).description("정류장 표기 ID"),
                        fieldWithPath("taskComment").type(JsonFieldType.STRING).description("[필수]변경내용"),
                        fieldWithPath("checkUserId").type(JsonFieldType.STRING).description("[필수]검수자ID")
                ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("상세 정보"),
	             		fieldWithPath("result.data.taskId").type(JsonFieldType.NUMBER).description("등록 성공한 작업 ID")
	             )
 		));

	}


	/**
	 * 정류장 수정 Task
	 * @throws Exception
	 */
	@Test
	public void registerBusStopEditTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;

		BusStopTaskInputVo busStopInputVo = new BusStopTaskInputVo() ;
		busStopInputVo.setStopId(1);
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
		busStopInputVo.setCheckUserId("kr94666");


		// 성공시 작업ID 리턴
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, 255);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		//given
		given(busStopService.registerBusStopTask(ArgumentMatchers.anyString(), any()))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/busStopTask/editTask")
	                .content(objectMapper.writeValueAsString(busStopInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busStop/busStopEditTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
	            		fieldWithPath("stopId").type(JsonFieldType.NUMBER).description("[필수]정류장 ID"),
                        fieldWithPath("stopName").type(JsonFieldType.STRING).description("[필수]정류장 명칭"),
                        fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("[필수]경도"),
                        fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("[필수]위도"),
                        fieldWithPath("cityCode").type(JsonFieldType.NUMBER).description("[필수]도시코드"),
                        fieldWithPath("level").type(JsonFieldType.NUMBER).description("정류장 위치구분"),
                        fieldWithPath("nonstopYn").type(JsonFieldType.STRING).description("미정차 정류장(Y/N)"),
                        fieldWithPath("virtualStopYn").type(JsonFieldType.STRING).description("가상 정류장(Y/N)"),
                        fieldWithPath("centerStopYn").type(JsonFieldType.STRING).description("중앙차로(Y/N)"),
                        fieldWithPath("sido").type(JsonFieldType.STRING).description("광역시,도명"),
                        fieldWithPath("gu").type(JsonFieldType.STRING).description("구"),
                        fieldWithPath("dong").type(JsonFieldType.STRING).description("동"),
                        fieldWithPath("roadAddress").type(JsonFieldType.STRING).description("도로명주소"),
                        fieldWithPath("roadName").type(JsonFieldType.STRING).description("도로명"),
                        fieldWithPath("bonbun").type(JsonFieldType.STRING).description("본번"),
                        fieldWithPath("boobun").type(JsonFieldType.STRING).description("부번"),
                        fieldWithPath("localStopId").type(JsonFieldType.STRING).description("지자체 정류장ID"),
                        fieldWithPath("providerId").type(JsonFieldType.NUMBER).description("지자체 BIS ID"),
                        fieldWithPath("displayId").type(JsonFieldType.STRING).description("정류장 표기 ID"),
                        fieldWithPath("taskComment").type(JsonFieldType.STRING).description("[필수]변경내용"),
                        fieldWithPath("checkUserId").type(JsonFieldType.STRING).description("[필수]검수자ID")
                ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("상세 정보"),
	             		fieldWithPath("result.data.taskId").type(JsonFieldType.NUMBER).description("등록 성공한 작업 ID")
	             )
 		));

	}

	/**
	 * 정류장 Task 수정
	 * @throws Exception
	 */
	@Test
	public void modifyBusStopTask() throws Exception {

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;

		BusStopTaskInputVo busStopInputVo = new BusStopTaskInputVo() ;
		busStopInputVo.setTaskId(1);
		busStopInputVo.setStopId(3);
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
		busStopInputVo.setCheckUserId("kr94666");



		//when
		ResultActions result = this.mockMvc.perform(
                put("/v1/ntool/api/modify/busStopTask")
	                .content(objectMapper.writeValueAsString(busStopInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busStop/busStopModifyTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
	            		fieldWithPath("taskId").type(JsonFieldType.NUMBER).description("[필수]작업ID"),
	            		fieldWithPath("stopId").type(JsonFieldType.NUMBER).description("[필수]정류장ID"),
                        fieldWithPath("stopName").type(JsonFieldType.STRING).description("[필수]정류장 명칭"),
                        fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("[필수]경도"),
                        fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("[필수]위도"),
                        fieldWithPath("cityCode").type(JsonFieldType.NUMBER).description("[필수]도시코드"),
                        fieldWithPath("level").type(JsonFieldType.NUMBER).description("정류장 위치구분"),
                        fieldWithPath("nonstopYn").type(JsonFieldType.STRING).description("미정차 정류장(Y/N)"),
                        fieldWithPath("virtualStopYn").type(JsonFieldType.STRING).description("가상 정류장(Y/N)"),
                        fieldWithPath("centerStopYn").type(JsonFieldType.STRING).description("중앙차로(Y/N)"),
                        fieldWithPath("sido").type(JsonFieldType.STRING).description("광역시,도명"),
                        fieldWithPath("gu").type(JsonFieldType.STRING).description("구"),
                        fieldWithPath("dong").type(JsonFieldType.STRING).description("동"),
                        fieldWithPath("roadAddress").type(JsonFieldType.STRING).description("도로명주소"),
                        fieldWithPath("roadName").type(JsonFieldType.STRING).description("도로명"),
                        fieldWithPath("bonbun").type(JsonFieldType.STRING).description("본번"),
                        fieldWithPath("boobun").type(JsonFieldType.STRING).description("부번"),
                        fieldWithPath("localStopId").type(JsonFieldType.STRING).description("지자체 정류장ID"),
                        fieldWithPath("providerId").type(JsonFieldType.NUMBER).description("지자체 BIS ID"),
                        fieldWithPath("displayId").type(JsonFieldType.STRING).description("정류장 표기 ID"),
                        fieldWithPath("taskComment").type(JsonFieldType.STRING).description("[필수]변경내용"),
                        fieldWithPath("checkUserId").type(JsonFieldType.STRING).description("[필수]검수자ID")

                ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지")
	             )
 		));

	}


	/**
	 * 정류장 삭제요청 Task 등록
	 * @throws Exception
	 */
	@Test
	public void busStopRemoveTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		BusStopRemoveTaskInputVo busStopRemoveInputVo = new BusStopRemoveTaskInputVo() ;
		busStopRemoveInputVo.setStopId(3);
		busStopRemoveInputVo.setTaskComment("미존재 정류장 - 삭제 처리");
		busStopRemoveInputVo.setCheckUserId("kr94666");

		// 성공시 작업ID 리턴
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, 255);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		//given
		given(busStopService.registerBusStopRemoveTask(any()))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/busStopTask/removeTask")
	                .content(objectMapper.writeValueAsString(busStopRemoveInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busStop/busStopRemoveTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
	            		fieldWithPath("stopId").type(JsonFieldType.NUMBER).description("[필수]정류장ID"),
                        fieldWithPath("taskComment").type(JsonFieldType.STRING).description("[필수]변경내용"),
                        fieldWithPath("checkUserId").type(JsonFieldType.STRING).description("[필수]검수자ID")
                ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("상세 정보"),
	             		fieldWithPath("result.data.taskId").type(JsonFieldType.NUMBER).description("등록 성공한 작업 ID")
	             )
 		));

	}

}
