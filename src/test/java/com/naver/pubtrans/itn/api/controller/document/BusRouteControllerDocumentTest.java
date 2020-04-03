package com.naver.pubtrans.itn.api.controller.document;

import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.BeanUtils;
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

import com.naver.pubtrans.itn.api.common.ApiUtils;
import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.controller.BusRouteController;
import com.naver.pubtrans.itn.api.controller.BusRouteControllerTest;
import com.naver.pubtrans.itn.api.handler.MemberAccessDeniedHandler;
import com.naver.pubtrans.itn.api.service.BusRouteService;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureGeometryVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphInputVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.GeoJsonFeatureInputVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.GeoJsonInputVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.output.BusStopGraphOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.output.GeoJsonOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteCompanyTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteCompanyOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteDetailOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteTaskDetailOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskSummaryOutputVo;

@RunWith(SpringRunner.class)
@WebMvcTest(BusRouteController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class BusRouteControllerDocumentTest {
	private static final JsonFieldType STRING = JsonFieldType.STRING;
	private static final JsonFieldType NUMBER = JsonFieldType.NUMBER;
	private static final JsonFieldType OBJECT = JsonFieldType.OBJECT;
	private static final JsonFieldType ARRAY = JsonFieldType.ARRAY;
	private static final JsonFieldType NULL = JsonFieldType.NULL;
	private static final List<JsonFieldType> STRING_OR_NULL = Arrays.asList(JsonFieldType.STRING, JsonFieldType.NULL) ;
	private static final List<JsonFieldType> NUMBER_OR_NULL = Arrays.asList(JsonFieldType.NUMBER, JsonFieldType.NULL) ;
	private static final List<JsonFieldType> OBJECT_OR_NULL = Arrays.asList(JsonFieldType.OBJECT, JsonFieldType.NULL) ;
	private static final List<JsonFieldType> ARRAY_OR_NULL = Arrays.asList(JsonFieldType.ARRAY, JsonFieldType.NULL) ;


	@Autowired
	MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@MockBean
	private BusRouteService busRouteService ;

	@MockBean
	private OutputFmtUtil outputFmtUtil ;

	@MockBean
	private JwtAdapter jwtAdapter;

	@MockBean
	private MemberAccessDeniedHandler memberAccessDeniedHandler;

	/**
	 * 버스 노선 목록 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void listBusStop() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;



		// 리스트 페이징 정보
		PagingVo pagingVo = new PagingVo(100, 1, 20) ;



		// 스키마
		SchemaVo schemaVo = new SchemaVo();
		schemaVo.setColumnName("route_id");
		schemaVo.setColumnComment("노선ID");
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
		BusRouteListOutputVo busRouteListOutputVo = new BusRouteListOutputVo();
		busRouteListOutputVo.setRouteId(11000000);
		busRouteListOutputVo.setRouteName("103");
		busRouteListOutputVo.setBusClass(11);
		busRouteListOutputVo.setBusClassName("간선");
		busRouteListOutputVo.setStartPointName("월계동");
		busRouteListOutputVo.setEndPointName("서울역");
		busRouteListOutputVo.setCityCode(1000);
		busRouteListOutputVo.setCityName("서울");
		busRouteListOutputVo.setBypassYn("N");
		busRouteListOutputVo.setBypassRouteCnt(0);


		List<BusRouteListOutputVo> busRouteListOutputVoList = new ArrayList<>();
		busRouteListOutputVoList.add(busRouteListOutputVo) ;


		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, busRouteListOutputVoList) ;


		//given
		given(busRouteService.getBusRouteList(any(BusRouteSearchVo.class)))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/busRoute")
                	.param("busClass", "11")
	                .param("routeName", "")
	            	.param("cityCode", "1000")
	            	.param("pageNo", "1")
	            	.param("listSize", "20")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busRoute/busRouteList",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("routeName").description("[선택]노선명").optional(),
	            		parameterWithName("routeId").description("[선택]노선ID").optional(),
	            		parameterWithName("busClass").description("[선택]노선 타입").optional(),
	            		parameterWithName("cityCode").description("[선택]도시코드").optional(),
	            		parameterWithName("pageNo").description("[선택]페이지 번호(기본:1)").optional(),
	            		parameterWithName("listSize").description("[선택]페이지당 목록 수(기본:20)").optional(),
	            		parameterWithName("sort").description("[선택]정렬(기본:목록 첫번째 Key 내림차순) - 사용 예:routeName,asc").optional()
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.meta").type(JsonFieldType.OBJECT).description("페이징 정보 - link:#_데이터_목록_페이징_정보[공통사항 참고]"),
	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"),

	             		fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("노선 목록"),
	             		fieldWithPath("result.data[].routeId").type(JsonFieldType.NUMBER).description("노선ID"),
	             		fieldWithPath("result.data[].routeName").type(JsonFieldType.STRING).description("노선명"),
	             		fieldWithPath("result.data[].busClass").type(JsonFieldType.NUMBER).description("노선 타입"),
	             		fieldWithPath("result.data[].busClassName").type(JsonFieldType.STRING).description("노선 타입명"),
	             		fieldWithPath("result.data[].startPointName").type(JsonFieldType.STRING).description("기점 정류장 명"),
	             		fieldWithPath("result.data[].endPointName").type(JsonFieldType.STRING).description("종점 정류장 명"),
	             		fieldWithPath("result.data[].cityCode").type(JsonFieldType.NUMBER).description("도시코드"),
	             		fieldWithPath("result.data[].cityName").type(JsonFieldType.STRING).description("도시코드명"),
	             		fieldWithPath("result.data[].bypassYn").type(JsonFieldType.STRING).description("우회노선 여부(Y/N)"),
	             		fieldWithPath("result.data[].bypassRouteCnt").type(JsonFieldType.NUMBER).description("우회노선수")
	             )
 		));

	}


	/**
	 * 노선 작업 요약 목록
	 * @throws Exception
	 */
	@Test
	public void listBusRouteTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;



		TaskSummaryOutputVo taskSummaryOuputVo1 = new TaskSummaryOutputVo();
		taskSummaryOuputVo1.setTaskId(1);
		taskSummaryOuputVo1.setTaskType("modify");
		taskSummaryOuputVo1.setTaskStatus("01");
		taskSummaryOuputVo1.setTaskDataType("route");
		taskSummaryOuputVo1.setTaskComment("노선 이름변경");
		taskSummaryOuputVo1.setRegDate("2020-03-06");
		taskSummaryOuputVo1.setWorkUserName("안경현");

		TaskSummaryOutputVo taskSummaryOuputVo2 = new TaskSummaryOutputVo();
		taskSummaryOuputVo2.setTaskId(2);
		taskSummaryOuputVo2.setTaskType("modify");
		taskSummaryOuputVo2.setTaskStatus("00");
		taskSummaryOuputVo2.setTaskDataType("route");
		taskSummaryOuputVo2.setTaskComment("DCC 노선 이름변경");
		taskSummaryOuputVo2.setRegDate("2020-03-09");
		taskSummaryOuputVo2.setWorkUserName("안경현");


		List<TaskSummaryOutputVo> taskSummaryOutputVoList = new ArrayList<>() ;
		taskSummaryOutputVoList.add(taskSummaryOuputVo1) ;
		taskSummaryOutputVoList.add(taskSummaryOuputVo2) ;

		// 페이징 정보
		PagingVo pagingVo = new PagingVo(2, 1, 20) ;

		CommonResult commonResult = outputFmtUtil.setCommonListFmt(pagingVo, taskSummaryOutputVoList);

		//given
		given(busRouteService.getBusRouteTaskSummaryList(ArgumentMatchers.anyInt(), any()))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/busRouteTask/{busRouteId}", 11000000)
	                .param("pageNo", "1")
	            	.param("listSize", "20")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busRoute/busRouteTaskList",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            pathParameters(
                        parameterWithName("busRouteId").description("노선 ID")
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
	 * 버스 정류장사이의 구간 그래프 정보 목록을 가져온다
	 * @throws Exception
	 */
	@Test
	public void listGraphInfoBetweenBusStops() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;


		// 구간 1
		GeoJsonFeatureGeometryVo geometry1 = new GeoJsonFeatureGeometryVo();
		geometry1.setType(CommonConstant.GEOJSON_GEOMETRY_TYPE_LINE_STRING);
		geometry1.setCoordinates(Arrays.asList(Arrays.asList(127.052801,37.620283), Arrays.asList(127.05331,37.619422), Arrays.asList(127.052389,37.618847)));

		BusStopGraphOutputVo properties1 = new BusStopGraphOutputVo();
		properties1.setGraphId(549);
		properties1.setStartStopId(55000529);
		properties1.setEndStopId(55000845);
		properties1.setStartStopName("광운초교앞");
		properties1.setEndStopName("장위래미안아파트");
		properties1.setMatchGraphInfoYn("Y");


		// 구간 2
		GeoJsonFeatureGeometryVo geometry2 = new GeoJsonFeatureGeometryVo();
		geometry2.setType(CommonConstant.GEOJSON_GEOMETRY_TYPE_LINE_STRING);
		geometry2.setCoordinates(Arrays.asList(Arrays.asList(127.052389,37.618847), Arrays.asList(127.051265,37.618108)));

		BusStopGraphOutputVo properties2 = new BusStopGraphOutputVo();
		properties2.setGraphId(875);
		properties2.setStartStopId(55000845);
		properties2.setEndStopId(55000524);
		properties2.setStartStopName("장위래미안아파트");
		properties2.setEndStopName("꿈의숲아이파크아파트");
		properties2.setMatchGraphInfoYn("Y");



		// feature 1
		GeoJsonFeatureVo geoJsonFeatureVo1 = new GeoJsonFeatureVo() ;
		geoJsonFeatureVo1.setType(CommonConstant.GEOJSON_TYPE_FEATURE_TYPE);
		geoJsonFeatureVo1.setProperties(properties1);
		geoJsonFeatureVo1.setGeometry(geometry1);

		// feature
		GeoJsonFeatureVo geoJsonFeatureVo2 = new GeoJsonFeatureVo() ;
		geoJsonFeatureVo2.setType(CommonConstant.GEOJSON_TYPE_FEATURE_TYPE);
		geoJsonFeatureVo2.setProperties(properties2);
		geoJsonFeatureVo2.setGeometry(geometry2);

		// feature List
		List<GeoJsonFeatureVo> geoJsonFeatureVoList = new ArrayList<>();
		geoJsonFeatureVoList.add(geoJsonFeatureVo1);
		geoJsonFeatureVoList.add(geoJsonFeatureVo2);



		GeoJsonOutputVo geoJsonOutputVo = new GeoJsonOutputVo();
		geoJsonOutputVo.setFeatures(geoJsonFeatureVoList);





		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(geoJsonOutputVo);


		//given
		given(busRouteService.getGraphInfoBetweenBusStops(any()))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/busStopsGraph")
                	.param("busStopIds", "55000529,55000845,55000524")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busRoute/graphBetweenBusStop",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("busStopIds").description("연속된 정류장 ID. 정류장사이는 쉼표(,)로 구분.(예:busStopIds=55000529,55000845,55000524)")
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("그래프 정보(GeoJson)"),
	             		fieldWithPath("result.data.type").type(JsonFieldType.STRING).description("geojson 타입 - FeatureCollection 고정 "),
	             		fieldWithPath("result.data.features[]").type(JsonFieldType.ARRAY).description("구간 정보 "),
	             		fieldWithPath("result.data.features[].type").type(JsonFieldType.STRING).description("타입 - Feature 고정 "),
	             		fieldWithPath("result.data.features[].geometry").type(JsonFieldType.OBJECT).description("지오메트리 정보"),
	             		fieldWithPath("result.data.features[].geometry.type").type(JsonFieldType.STRING).description("지오메트리 타입 - LineString 고정"),
	             		fieldWithPath("result.data.features[].geometry.coordinates[]").type(JsonFieldType.ARRAY).description("좌표정보"),
	             		fieldWithPath("result.data.features[].geometry.coordinates[].[]").type(JsonFieldType.ARRAY).description("LineString 목록"),
	             		fieldWithPath("result.data.features[].properties").type(JsonFieldType.OBJECT).description("속성 정보"),
	             		fieldWithPath("result.data.features[].properties.graphId").type(NUMBER_OR_NULL).description("그래프 ID - 신규그래프인 경우 NULL"),
	             		fieldWithPath("result.data.features[].properties.startStopId").type(JsonFieldType.NUMBER).description("출발 정류장 ID"),
	             		fieldWithPath("result.data.features[].properties.endStopId").type(JsonFieldType.NUMBER).description("도착 정류장 ID"),
	             		fieldWithPath("result.data.features[].properties.startStopName").type(JsonFieldType.STRING).description("출발 정류장명"),
	             		fieldWithPath("result.data.features[].properties.endStopName").type(JsonFieldType.STRING).description("도착 정류장명"),
	             		fieldWithPath("result.data.features[].properties.matchGraphInfoYn").type(JsonFieldType.STRING).description("그래프 정보 존재여부(Y/N)")
	             )
 		));

	}


	/**
	 * 버스정류장 기본 스키마를 조회한다
	 * @throws Exception
	 */
	@Test
	public void schemaBusRoute() throws Exception {

		List<CommonSchema> commonSchema = new ArrayList<>() ;

		//given
		given(busRouteService.selectBusRouteSchemaAll())
				.willReturn(commonSchema) ;

		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/schema/busRoute")
	                .characterEncoding("UTF-8")
        );

		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busRoute/busRouteSchema",
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
	 * 버스노선 상세정보
	 * @throws Exception
	 */
	@Test
	public void infoBusRoute() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;


		BusRouteDetailOutputVo busRouteDetailOutputVo = this.getCommonBusRouteInfoVo();
		List<CommonSchema> commonSchemaList = this.getCommonSchemaList();
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, busRouteDetailOutputVo);


		//given
		given(busRouteService.getBusRouteInfo(11000000))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/info/busRoute/{routeId}", 11000000)
	                .characterEncoding("UTF-8")
        );

		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busRoute/busRouteInfo",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            pathParameters(
	                    parameterWithName("routeId").description("노선 ID")
	            ),
	            responseFields(
	            		fieldWithPath("code").type(NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(STRING).description("API 응답 메세지"),
	             		subsectionWithPath("result").type(OBJECT).description("결과 정보").optional(),

	             		subsectionWithPath("result.schema[]").type(ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"),

	             		fieldWithPath("result.data").type(OBJECT).description("상세 정보"),
	             		fieldWithPath("result.data.routeId").type(NUMBER).description("노선 ID"),
	             		fieldWithPath("result.data.routeName").type(STRING).description("노선 명칭"),
	             		fieldWithPath("result.data.cityCode").type(NUMBER).description("도시코드"),
	             		fieldWithPath("result.data.busClass").type(NUMBER).description("버스 노선 종류"),
	             		fieldWithPath("result.data.busAdditionalName").type(STRING_OR_NULL).description("버스 노선 부가명칭"),
	             		fieldWithPath("result.data.startPointName").type(STRING_OR_NULL).description("기점명"),
	             		fieldWithPath("result.data.endPointName").type(STRING_OR_NULL).description("종점명"),
	             		fieldWithPath("result.data.turningPointSequence").type(NUMBER).description("회자 정류장 순번"),
	             		fieldWithPath("result.data.weekdayStartPointFirstTime").type(STRING_OR_NULL).description("평일 기점 기준 첫차 출발 시각(HHmm)"),
	             		fieldWithPath("result.data.weekdayStartPointLastTime").type(STRING_OR_NULL).description("평일 기점 기준 첫차 도착 시각(HHmm)"),
	             		fieldWithPath("result.data.weekdayEndPointFirstTime").type(STRING_OR_NULL).description("평일 종점 기준 첫차 출발 시각(HHmm)"),
	             		fieldWithPath("result.data.weekdayEndPointLastTime").type(STRING_OR_NULL).description("평일 종점 기준 첫차 도착 시각(HHmm)"),
	             		fieldWithPath("result.data.weekdayIntervalMin").type(NUMBER_OR_NULL).description("평일 최소 배차간격(분)"),
	             		fieldWithPath("result.data.weekdayIntervalMax").type(NUMBER_OR_NULL).description("평일 최대 배차간격(분)"),
	             		fieldWithPath("result.data.weekdayIntervalCount").type(NUMBER_OR_NULL).description("평일 배차간격(횟수)"),
	             		fieldWithPath("result.data.saturdayStartPointFirstTime").type(STRING_OR_NULL).description("토요일 기점 기준 첫차 출발 시각"),
	             		fieldWithPath("result.data.saturdayStartPointLastTime").type(STRING_OR_NULL).description("토요일 기점 기준 첫차 도착 시각"),
	             		fieldWithPath("result.data.saturdayEndPointFirstTime").type(STRING_OR_NULL).description("토요일 종점 기준 첫차 출발 시각"),
	             		fieldWithPath("result.data.saturdayEndPointLastTime").type(STRING_OR_NULL).description("토요일 종점 기준 첫차 도착 시각"),
	             		fieldWithPath("result.data.saturdayIntervalMin").type(NUMBER_OR_NULL).description("토요일 최소 배차간격(분)"),
	             		fieldWithPath("result.data.saturdayIntervalMax").type(NUMBER_OR_NULL).description("토요일 최대 배차간격(분)"),
	             		fieldWithPath("result.data.saturdayIntervalCount").type(NUMBER_OR_NULL).description("토요일 배차간격(횟수)"),
	             		fieldWithPath("result.data.sundayStartPointFirstTime").type(STRING_OR_NULL).description("일요일 기점 기준 첫차 출발 시각"),
	             		fieldWithPath("result.data.sundayStartPointLastTime").type(STRING_OR_NULL).description("일요일 기점 기준 첫차 도착 시각"),
	             		fieldWithPath("result.data.sundayEndPointFirstTime").type(STRING_OR_NULL).description("일요일 종점 기준 첫차 출발 시각"),
	             		fieldWithPath("result.data.sundayEndPointLastTime").type(STRING_OR_NULL).description("일요일 종점 기준 첫차 도착 시각"),
	             		fieldWithPath("result.data.sundayIntervalMin").type(NUMBER_OR_NULL).description("일요일 최소 배차간격(분)"),
	             		fieldWithPath("result.data.sundayIntervalMax").type(NUMBER_OR_NULL).description("일요일 최대 배차간격(분)"),
	             		fieldWithPath("result.data.sundayIntervalCount").type(NUMBER_OR_NULL).description("일요일 배차간격(횟수)"),
	             		fieldWithPath("result.data.nonstepBusYn").type(STRING_OR_NULL).description("저상버스 운행여부(Y/N)"),
	             		fieldWithPath("result.data.telReservation").type(STRING_OR_NULL).description("예약전화번호"),
	             		fieldWithPath("result.data.providerId").type(NUMBER_OR_NULL).description("BIS 지역구분ID"),
	             		fieldWithPath("result.data.bypassYn").type(STRING_OR_NULL).description("우회노선 여부(Y/N)"),
	             		fieldWithPath("result.data.localRouteId").type(STRING_OR_NULL).description("상세 정보"),
	             		fieldWithPath("result.data.mondayYn").type(STRING).description("월요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.tuesdayYn").type(STRING).description("화요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.wednesdayYn").type(STRING).description("수요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.thursdayYn").type(STRING).description("목요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.fridayYn").type(STRING).description("금요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.saturdayYn").type(STRING).description("토요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.sundayYn").type(STRING).description("일요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.parentRouteId").type(STRING_OR_NULL).description("우회노선인경우 본노선 ID"),
	             		fieldWithPath("result.data.bypassStartDateTime").type(STRING_OR_NULL).description("우회노선인경우 우회 시작일시"),
	             		fieldWithPath("result.data.bypassEndDateTime").type(STRING_OR_NULL).description("우회노선인경우 우회 종료일시"),

	             		fieldWithPath("result.data.busStopGraphInfo").type(OBJECT).description("버스 경유정류장 및 그래프 정보(GeoJson 형식)"),
	             		fieldWithPath("result.data.busStopGraphInfo.type").type(STRING).description("geojson type"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].type").type(STRING).description("타입 - Feature 고정 "),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].geometry").type(OBJECT).description("지오메트리 정보"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].geometry.type").type(STRING).description("지오메트리 타입 - LineString 고정"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].geometry.coordinates[]").type(ARRAY).description("좌표정보"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].geometry.coordinates[].[]").type(ARRAY).description("LineString 목록"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties").type(OBJECT).description("속성 정보"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.graphId").type(NUMBER_OR_NULL).description("그래프 ID - 신규그래프인 경우 NULL"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.startStopId").type(NUMBER).description("출발 정류장 ID"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.endStopId").type(NUMBER).description("도착 정류장 ID"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.startStopName").type(STRING).description("출발 정류장명"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.endStopName").type(STRING).description("도착 정류장명"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.matchGraphInfoYn").type(STRING).description("그래프 정보 존재여부(Y/N)"),

	             		fieldWithPath("result.data.companyList[]").type(ARRAY).description("운수회사 목록"),
	             		fieldWithPath("result.data.companyList[].routeId").type(NUMBER).description("노선ID"),
	             		fieldWithPath("result.data.companyList[].companyId").type(NUMBER).description("운수회사 ID"),
	             		fieldWithPath("result.data.companyList[].companyName").type(STRING).description("운수회사 명"),
	             		fieldWithPath("result.data.companyList[].tel").type(STRING_OR_NULL).description("연락처"),
	             		fieldWithPath("result.data.companyList[].cityCode").type(NUMBER).description("운수회사 도시코드"),
	             		fieldWithPath("result.data.companyList[].cityName").type(STRING).description("운수회사 도시명"),

	             		fieldWithPath("result.data.bypassChildList[]").type(ARRAY_OR_NULL).description("본노선일때 하위 우회노선 목록").optional(),
	             		fieldWithPath("result.data.bypassChildList[].bypassRouteId").type(NUMBER).description("우회노선 ID"),
	             		fieldWithPath("result.data.bypassChildList[].parentRouteId").type(NUMBER).description("본 노선 ID"),
	             		fieldWithPath("result.data.bypassChildList[].routeName").type(STRING).description("우회노선 명칭"),
	             		fieldWithPath("result.data.bypassChildList[].bypassStartDateTime").type(STRING).description("우회 시작일시"),
	             		fieldWithPath("result.data.bypassChildList[].bypassEndDateTime").type(STRING).description("우회 종료일시")



	             )
 		));
	}

	/**
	 * 버스노선 작업정보 rest doc 생성
	 * @throws Exception
	 */
	@Test
	public void infoBusRouteTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		TaskOutputVo taskOutputVo = this.getCommonBusTaskInfoVo();

		BusRouteDetailOutputVo busRouteDetailOutputVo = this.getCommonBusRouteInfoVo();
		BusRouteTaskDetailOutputVo busRouteTaskDetailOutputVo = new BusRouteTaskDetailOutputVo();
		BeanUtils.copyProperties(busRouteDetailOutputVo, busRouteTaskDetailOutputVo);

		busRouteTaskDetailOutputVo.setTaskId(122);
		busRouteTaskDetailOutputVo.setTaskInfo(taskOutputVo);

		List<CommonSchema> commonSchema = this.getCommonSchemaList();
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(busRouteTaskDetailOutputVo);


		//given
		given(busRouteService.getBusRouteTaskInfo(122))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/info/busRouteTask/{taskId}", 122)
	                .characterEncoding("UTF-8")
        );

		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busRoute/busRouteTaskInfo",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            pathParameters(
	                    parameterWithName("taskId").description("작업 ID")
	            ),
	            responseFields(
	            		fieldWithPath("code").type(NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(STRING).description("API 응답 메세지"),
	             		subsectionWithPath("result").type(OBJECT).description("결과 정보").optional(),

	             		subsectionWithPath("result.schema[]").type(ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]").optional(),

	             		fieldWithPath("result.data").type(OBJECT).description("상세 정보"),
	             		fieldWithPath("result.data.taskId").type(NUMBER).description("작업 ID"),
	             		fieldWithPath("result.data.routeId").type(NUMBER).description("노선 ID"),
	             		fieldWithPath("result.data.routeName").type(STRING).description("노선 명칭"),
	             		fieldWithPath("result.data.cityCode").type(NUMBER).description("도시코드"),
	             		fieldWithPath("result.data.busClass").type(NUMBER).description("버스 노선 종류"),
	             		fieldWithPath("result.data.busAdditionalName").type(STRING_OR_NULL).description("버스 노선 부가명칭"),
	             		fieldWithPath("result.data.startPointName").type(STRING_OR_NULL).description("기점명"),
	             		fieldWithPath("result.data.endPointName").type(STRING_OR_NULL).description("종점명"),
	             		fieldWithPath("result.data.turningPointSequence").type(NUMBER).description("회자 정류장 순번"),
	             		fieldWithPath("result.data.weekdayStartPointFirstTime").type(STRING_OR_NULL).description("평일 기점 기준 첫차 출발 시각(HHmm)"),
	             		fieldWithPath("result.data.weekdayStartPointLastTime").type(STRING_OR_NULL).description("평일 기점 기준 첫차 도착 시각(HHmm)"),
	             		fieldWithPath("result.data.weekdayEndPointFirstTime").type(STRING_OR_NULL).description("평일 종점 기준 첫차 출발 시각(HHmm)"),
	             		fieldWithPath("result.data.weekdayEndPointLastTime").type(STRING_OR_NULL).description("평일 종점 기준 첫차 도착 시각(HHmm)"),
	             		fieldWithPath("result.data.weekdayIntervalMin").type(NUMBER_OR_NULL).description("평일 최소 배차간격(분)"),
	             		fieldWithPath("result.data.weekdayIntervalMax").type(NUMBER_OR_NULL).description("평일 최대 배차간격(분)"),
	             		fieldWithPath("result.data.weekdayIntervalCount").type(NUMBER_OR_NULL).description("평일 배차간격(횟수)"),
	             		fieldWithPath("result.data.saturdayStartPointFirstTime").type(STRING_OR_NULL).description("토요일 기점 기준 첫차 출발 시각"),
	             		fieldWithPath("result.data.saturdayStartPointLastTime").type(STRING_OR_NULL).description("토요일 기점 기준 첫차 도착 시각"),
	             		fieldWithPath("result.data.saturdayEndPointFirstTime").type(STRING_OR_NULL).description("토요일 종점 기준 첫차 출발 시각"),
	             		fieldWithPath("result.data.saturdayEndPointLastTime").type(STRING_OR_NULL).description("토요일 종점 기준 첫차 도착 시각"),
	             		fieldWithPath("result.data.saturdayIntervalMin").type(NUMBER_OR_NULL).description("토요일 최소 배차간격(분)"),
	             		fieldWithPath("result.data.saturdayIntervalMax").type(NUMBER_OR_NULL).description("토요일 최대 배차간격(분)"),
	             		fieldWithPath("result.data.saturdayIntervalCount").type(NUMBER_OR_NULL).description("토요일 배차간격(횟수)"),
	             		fieldWithPath("result.data.sundayStartPointFirstTime").type(STRING_OR_NULL).description("일요일 기점 기준 첫차 출발 시각"),
	             		fieldWithPath("result.data.sundayStartPointLastTime").type(STRING_OR_NULL).description("일요일 기점 기준 첫차 도착 시각"),
	             		fieldWithPath("result.data.sundayEndPointFirstTime").type(STRING_OR_NULL).description("일요일 종점 기준 첫차 출발 시각"),
	             		fieldWithPath("result.data.sundayEndPointLastTime").type(STRING_OR_NULL).description("일요일 종점 기준 첫차 도착 시각"),
	             		fieldWithPath("result.data.sundayIntervalMin").type(NUMBER_OR_NULL).description("일요일 최소 배차간격(분)"),
	             		fieldWithPath("result.data.sundayIntervalMax").type(NUMBER_OR_NULL).description("일요일 최대 배차간격(분)"),
	             		fieldWithPath("result.data.sundayIntervalCount").type(NUMBER_OR_NULL).description("일요일 배차간격(횟수)"),
	             		fieldWithPath("result.data.nonstepBusYn").type(STRING_OR_NULL).description("저상버스 운행여부(Y/N)"),
	             		fieldWithPath("result.data.telReservation").type(STRING_OR_NULL).description("예약전화번호"),
	             		fieldWithPath("result.data.providerId").type(NUMBER_OR_NULL).description("BIS 지역구분ID"),
	             		fieldWithPath("result.data.bypassYn").type(STRING_OR_NULL).description("우회노선 여부(Y/N)"),
	             		fieldWithPath("result.data.localRouteId").type(STRING_OR_NULL).description("상세 정보"),
	             		fieldWithPath("result.data.mondayYn").type(STRING).description("월요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.tuesdayYn").type(STRING).description("화요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.wednesdayYn").type(STRING).description("수요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.thursdayYn").type(STRING).description("목요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.fridayYn").type(STRING).description("금요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.saturdayYn").type(STRING).description("토요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.sundayYn").type(STRING).description("일요일 운행여부(Y/N)"),
	             		fieldWithPath("result.data.parentRouteId").type(STRING_OR_NULL).description("우회노선인경우 본노선 ID"),
	             		fieldWithPath("result.data.bypassStartDateTime").type(STRING_OR_NULL).description("우회노선인경우 우회 시작일시"),
	             		fieldWithPath("result.data.bypassEndDateTime").type(STRING_OR_NULL).description("우회노선인경우 우회 종료일시"),

	             		fieldWithPath("result.data.busStopGraphInfo").type(OBJECT).description("버스 경유정류장 및 그래프 정보(GeoJson 형식)"),
	             		fieldWithPath("result.data.busStopGraphInfo.type").type(STRING).description("geojson type"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].type").type(STRING).description("타입 - Feature 고정 "),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].geometry").type(OBJECT).description("지오메트리 정보"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].geometry.type").type(STRING).description("지오메트리 타입 - LineString 고정"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].geometry.coordinates[]").type(ARRAY).description("좌표정보"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].geometry.coordinates[].[]").type(ARRAY).description("LineString 목록"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties").type(OBJECT).description("속성 정보"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.graphId").type(NUMBER_OR_NULL).description("그래프 ID - 신규그래프인 경우 NULL"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.startStopId").type(NUMBER).description("출발 정류장 ID"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.endStopId").type(NUMBER).description("도착 정류장 ID"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.startStopName").type(STRING).description("출발 정류장명"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.endStopName").type(STRING).description("도착 정류장명"),
	             		fieldWithPath("result.data.busStopGraphInfo.features[].properties.matchGraphInfoYn").type(STRING).description("그래프 정보 존재여부(Y/N)"),

	             		fieldWithPath("result.data.companyList[]").type(ARRAY).description("운수회사 목록"),
	             		fieldWithPath("result.data.companyList[].routeId").type(NUMBER).description("노선ID"),
	             		fieldWithPath("result.data.companyList[].companyId").type(NUMBER).description("운수회사 ID"),
	             		fieldWithPath("result.data.companyList[].companyName").type(STRING).description("운수회사 명"),
	             		fieldWithPath("result.data.companyList[].tel").type(STRING_OR_NULL).description("연락처"),
	             		fieldWithPath("result.data.companyList[].cityCode").type(NUMBER).description("운수회사 도시코드"),
	             		fieldWithPath("result.data.companyList[].cityName").type(STRING).description("운수회사 도시명"),
	             		fieldWithPath("result.data.bypassChildList[]").type(ARRAY_OR_NULL).description("본노선일때 하위 우회노선 목록").optional(),
	             		fieldWithPath("result.data.bypassChildList[].bypassRouteId").type(NUMBER).description("우회노선 ID"),
	             		fieldWithPath("result.data.bypassChildList[].parentRouteId").type(NUMBER).description("본 노선 ID"),
	             		fieldWithPath("result.data.bypassChildList[].routeName").type(STRING).description("우회노선 명칭"),
	             		fieldWithPath("result.data.bypassChildList[].bypassStartDateTime").type(STRING).description("우회 시작일시"),
	             		fieldWithPath("result.data.bypassChildList[].bypassEndDateTime").type(STRING).description("우회 종료일시"),

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
	             		fieldWithPath("result.data.taskInfo.checkCompleteDate").type(STRING_OR_NULL).description("검수일")

	             )
 		));
	}


	/**
	 * 버스노선 생성 작업 Task 등록
	 * @throws Exception
	 */
	@Test
	public void busRouteAddTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputVo();


		// 성공시 작업ID 리턴
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, 255);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);



		//given
		given(busRouteService.registerBusRouteTask(ArgumentMatchers.anyString(), any()))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/busRouteTask/addTask")
                .content(objectMapper.writeValueAsString(busRouteTaskInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8")
        );

		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busRoute/busRouteAddTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
                    fieldWithPath("routeName").type(STRING).description("[필수]노선명칭"),
                    fieldWithPath("cityCode").type(NUMBER).description("[필수]도시코드"),
                    fieldWithPath("busClass").type(NUMBER).description("[필수]버스 노선타입"),
                    fieldWithPath("busAdditionalName").type(STRING).description("노선 부가명칭").optional(),
                    fieldWithPath("startPointName").type(STRING).description("[필수]기점 정류장 명"),
                    fieldWithPath("endPointName").type(STRING).description("[필수]종점 정류장 명"),
                    fieldWithPath("turningPointSequence").type(NUMBER).description("회차 정류장 순번").optional(),
                    fieldWithPath("weekdayStartPointFirstTime").type(STRING).description("평일 기점 기준 첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("weekdayStartPointLastTime").type(STRING).description("평일 기점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("weekdayEndPointFirstTime").type(STRING).description("평일 종점 기준  첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("weekdayEndPointLastTime").type(STRING).description("평일 종점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("weekdayIntervalMin").type(NUMBER).description("평일 최소 배차간격(분)").optional(),
                    fieldWithPath("weekdayIntervalMax").type(NUMBER).description("평일 최대 배차간격(분)").optional(),
                    fieldWithPath("weekdayIntervalCount").type(NUMBER).description("평일 배차간격(횟수)").optional(),
                    fieldWithPath("saturdayStartPointFirstTime").type(STRING).description("토요일 기점 기준 첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("saturdayStartPointLastTime").type(STRING).description("토요일 기점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("saturdayEndPointFirstTime").type(STRING).description("토요일 종점 기준 첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("saturdayEndPointLastTime").type(STRING).description("토요일 종점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("saturdayIntervalMin").type(NUMBER).description("토요일 최소 배차간격(분)").optional(),
                    fieldWithPath("saturdayIntervalMax").type(NUMBER).description("토요일 최대 배차간격(분)").optional(),
                    fieldWithPath("saturdayIntervalCount").type(NUMBER).description("토요일 배차간격(횟수)").optional(),
                    fieldWithPath("sundayStartPointFirstTime").type(STRING).description("일요일 기점 기준 첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("sundayStartPointLastTime").type(STRING).description("일요일 기점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("sundayEndPointFirstTime").type(STRING).description("일요일 종점 기준 첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("sundayEndPointLastTime").type(STRING).description("일요일 종점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("sundayIntervalMin").type(NUMBER).description("일요일 최소 배차간격(분)").optional(),
                    fieldWithPath("sundayIntervalMax").type(NUMBER).description("일요일 최대 배차간격(분)").optional(),
                    fieldWithPath("sundayIntervalCount").type(NUMBER).description("일요일 배차간격(횟수)").optional(),
                    fieldWithPath("nonstepBusYn").type(STRING).description("저상버스 운행여부(Y/N)").optional(),
                    fieldWithPath("telReservation").type(STRING).description("예약 전화번호").optional(),
                    fieldWithPath("providerId").type(NUMBER).description("BIS 지역 ID").optional(),
                    fieldWithPath("bypassYn").type(STRING).description("우회노선 여부(Y/N)").optional(),
                    fieldWithPath("localRouteId").type(STRING).description("BIS 노선 ID").optional(),
                    fieldWithPath("mondayYn").type(STRING).description("[필수]월요일 운행여부(Y/N)"),
                    fieldWithPath("tuesdayYn").type(STRING).description("[필수]화요일 운행여부(Y/N)"),
                    fieldWithPath("wednesdayYn").type(STRING).description("[필수]수요일 운행여부(Y/N)"),
                    fieldWithPath("thursdayYn").type(STRING).description("[필수]목요일 운행여부(Y/N)"),
                    fieldWithPath("fridayYn").type(STRING).description("[필수]금요일 운행여부(Y/N)"),
                    fieldWithPath("saturdayYn").type(STRING).description("[필수]토요일 운행여부(Y/N)"),
                    fieldWithPath("sundayYn").type(STRING).description("[필수]일요일 운행여부(Y/N)"),
                    fieldWithPath("parentRouteId").type(NUMBER).description("본 노선 ID").optional(),
                    fieldWithPath("bypassStartDateTime").type(STRING).description("우회 시작일시").optional(),
                    fieldWithPath("bypassEndDateTime").type(STRING).description("우회 종료일시").optional(),
                    fieldWithPath("taskComment").type(STRING).description("[필수]작업내용"),
                    fieldWithPath("checkUserId").type(STRING).description("[필수]검수자 ID"),
                    fieldWithPath("companyList[]").type(ARRAY).description("[필수]운수회사 정보"),
                    fieldWithPath("companyList[].companyId").type(NUMBER).description("[필수]운수회사 ID"),
                    fieldWithPath("busStopGraphInfo").type(OBJECT).description("[필수]노선 경유정류장 및 그래프 정보"),
                    fieldWithPath("busStopGraphInfo.type").type(STRING).description("FeatureCollection").optional(),
                    fieldWithPath("busStopGraphInfo.features[]").type(ARRAY).description("[필수] feature 목록"),
                    fieldWithPath("busStopGraphInfo.features[].type").type(STRING).description("Feature").optional(),
                    fieldWithPath("busStopGraphInfo.features[].geometry").type(OBJECT).description("[필수]지오메트리 정보"),
                    fieldWithPath("busStopGraphInfo.features[].geometry.type").type(STRING).description("LineString").optional(),
                    fieldWithPath("busStopGraphInfo.features[].geometry.coordinates[]").type(ARRAY).description("[필수]좌표정보"),
                    fieldWithPath("busStopGraphInfo.features[].geometry.coordinates[].[]").type(ARRAY).description("[필수]좌표목록"),
                    fieldWithPath("busStopGraphInfo.features[].properties").type(OBJECT).description("[필수]속성 정보"),
                    fieldWithPath("busStopGraphInfo.features[].properties.graphId").type(NUMBER).description("그래프 ID").optional(),
                    fieldWithPath("busStopGraphInfo.features[].properties.startStopId").type(NUMBER).description("[필수]출발 정류장ID"),
                    fieldWithPath("busStopGraphInfo.features[].properties.endStopId").type(NUMBER).description("[필수]도착 정류장ID")
	            ),
	            responseFields(
	            		fieldWithPath("code").type(NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(OBJECT).description("결과 정보"),
	             		fieldWithPath("result.data").type(OBJECT).description("상세 정보"),
	             		fieldWithPath("result.data.taskId").type(NUMBER).description("등록 성공한 작업 ID")
	             )
 		));
	}


	/**
	 * 버스노선 생성 수정 Task 등록
	 * @throws Exception
	 */
	@Test
	public void busRouteEditTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;

		// 입력정보 설정
		BusRouteTaskInputVo busRouteTaskInputVo = this.getBusRouteTaskInputVo();
		busRouteTaskInputVo.setRouteId(11000000);


		// 성공시 작업ID 리턴
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, 255);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);


		//given
		given(busRouteService.registerBusRouteTask(ArgumentMatchers.anyString(), any()))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/busRouteTask/editTask")
                .content(objectMapper.writeValueAsString(busRouteTaskInputVo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8")
        );

		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busRoute/busRouteEditTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
	            	fieldWithPath("routeId").type(NUMBER).description("[필수]노선ID"),
                    fieldWithPath("routeName").type(STRING).description("[필수]노선명칭"),
                    fieldWithPath("cityCode").type(NUMBER).description("[필수]도시코드"),
                    fieldWithPath("busClass").type(NUMBER).description("[필수]버스 노선타입"),
                    fieldWithPath("busAdditionalName").type(STRING).description("노선 부가명칭").optional(),
                    fieldWithPath("startPointName").type(STRING).description("[필수]기점 정류장 명"),
                    fieldWithPath("endPointName").type(STRING).description("[필수]종점 정류장 명"),
                    fieldWithPath("turningPointSequence").type(NUMBER).description("회차 정류장 순번").optional(),
                    fieldWithPath("weekdayStartPointFirstTime").type(STRING).description("평일 기점 기준 첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("weekdayStartPointLastTime").type(STRING).description("평일 기점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("weekdayEndPointFirstTime").type(STRING).description("평일 종점 기준  첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("weekdayEndPointLastTime").type(STRING).description("평일 종점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("weekdayIntervalMin").type(NUMBER).description("평일 최소 배차간격(분)").optional(),
                    fieldWithPath("weekdayIntervalMax").type(NUMBER).description("평일 최대 배차간격(분)").optional(),
                    fieldWithPath("weekdayIntervalCount").type(NUMBER).description("평일 배차간격(횟수)").optional(),
                    fieldWithPath("saturdayStartPointFirstTime").type(STRING).description("토요일 기점 기준 첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("saturdayStartPointLastTime").type(STRING).description("토요일 기점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("saturdayEndPointFirstTime").type(STRING).description("토요일 종점 기준 첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("saturdayEndPointLastTime").type(STRING).description("토요일 종점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("saturdayIntervalMin").type(NUMBER).description("토요일 최소 배차간격(분)").optional(),
                    fieldWithPath("saturdayIntervalMax").type(NUMBER).description("토요일 최대 배차간격(분)").optional(),
                    fieldWithPath("saturdayIntervalCount").type(NUMBER).description("토요일 배차간격(횟수)").optional(),
                    fieldWithPath("sundayStartPointFirstTime").type(STRING).description("일요일 기점 기준 첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("sundayStartPointLastTime").type(STRING).description("일요일 기점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("sundayEndPointFirstTime").type(STRING).description("일요일 종점 기준 첫차 출발시간(HHmm)").optional(),
                    fieldWithPath("sundayEndPointLastTime").type(STRING).description("일요일 종점 기준 첫차 도착시간(HHmm)").optional(),
                    fieldWithPath("sundayIntervalMin").type(NUMBER).description("일요일 최소 배차간격(분)").optional(),
                    fieldWithPath("sundayIntervalMax").type(NUMBER).description("일요일 최대 배차간격(분)").optional(),
                    fieldWithPath("sundayIntervalCount").type(NUMBER).description("일요일 배차간격(횟수)").optional(),
                    fieldWithPath("nonstepBusYn").type(STRING).description("저상버스 운행여부(Y/N)").optional(),
                    fieldWithPath("telReservation").type(STRING).description("예약 전화번호").optional(),
                    fieldWithPath("providerId").type(NUMBER).description("BIS 지역 ID").optional(),
                    fieldWithPath("localRouteId").type(STRING).description("BIS 노선 ID").optional(),
                    fieldWithPath("mondayYn").type(STRING).description("[필수]월요일 운행여부(Y/N)"),
                    fieldWithPath("tuesdayYn").type(STRING).description("[필수]화요일 운행여부(Y/N)"),
                    fieldWithPath("wednesdayYn").type(STRING).description("[필수]수요일 운행여부(Y/N)"),
                    fieldWithPath("thursdayYn").type(STRING).description("[필수]목요일 운행여부(Y/N)"),
                    fieldWithPath("fridayYn").type(STRING).description("[필수]금요일 운행여부(Y/N)"),
                    fieldWithPath("saturdayYn").type(STRING).description("[필수]토요일 운행여부(Y/N)"),
                    fieldWithPath("sundayYn").type(STRING).description("[필수]일요일 운행여부(Y/N)"),
                    fieldWithPath("bypassYn").type(STRING).description("우회노선 여부(Y/N)").optional(),
                    fieldWithPath("parentRouteId").type(NUMBER).description("본 노선 ID").optional(),
                    fieldWithPath("bypassStartDateTime").type(STRING).description("우회 시작일시(YYYYmmddHHmm)").optional(),
                    fieldWithPath("bypassEndDateTime").type(STRING).description("우회 종료일시(YYYYmmddHHmm)").optional(),
                    fieldWithPath("taskComment").type(STRING).description("[필수]작업내용"),
                    fieldWithPath("checkUserId").type(STRING).description("[필수]검수자 ID"),
                    fieldWithPath("companyList[]").type(ARRAY).description("[필수]운수회사 정보"),
                    fieldWithPath("companyList[].companyId").type(NUMBER).description("[필수]운수회사 ID"),
                    fieldWithPath("busStopGraphInfo").type(OBJECT).description("[필수]노선 경유정류장 및 그래프 정보"),
                    fieldWithPath("busStopGraphInfo.type").type(STRING).description("FeatureCollection").optional(),
                    fieldWithPath("busStopGraphInfo.features[]").type(ARRAY).description("[필수] feature 목록"),
                    fieldWithPath("busStopGraphInfo.features[].type").type(STRING).description("Feature").optional(),
                    fieldWithPath("busStopGraphInfo.features[].geometry").type(OBJECT).description("[필수]지오메트리 정보"),
                    fieldWithPath("busStopGraphInfo.features[].geometry.type").type(STRING).description("LineString").optional(),
                    fieldWithPath("busStopGraphInfo.features[].geometry.coordinates[]").type(ARRAY).description("[필수]좌표정보"),
                    fieldWithPath("busStopGraphInfo.features[].geometry.coordinates[].[]").type(ARRAY).description("[필수]좌표목록"),
                    fieldWithPath("busStopGraphInfo.features[].properties").type(OBJECT).description("[필수]속성 정보"),
                    fieldWithPath("busStopGraphInfo.features[].properties.graphId").type(NUMBER).description("그래프 ID").optional(),
                    fieldWithPath("busStopGraphInfo.features[].properties.startStopId").type(NUMBER).description("[필수]출발 정류장ID"),
                    fieldWithPath("busStopGraphInfo.features[].properties.endStopId").type(NUMBER).description("[필수]도착 정류장ID")
	            ),
	            responseFields(
	            		fieldWithPath("code").type(NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(OBJECT).description("결과 정보"),
	             		fieldWithPath("result.data").type(OBJECT).description("상세 정보"),
	             		fieldWithPath("result.data.taskId").type(NUMBER).description("등록 성공한 작업 ID")
	             )
 		));
	}


	/**
	 * 공통 스키마 정보
	 * @return
	 */
	public List<CommonSchema> getCommonSchemaList() {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		// 스키마
		SchemaVo schemaVo = new SchemaVo();
		schemaVo.setColumnName("route_id");
		schemaVo.setColumnComment("노선ID");
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

		return outputFmtUtil.makeCommonSchema(schemaVoList, null, valuesMap) ;
	}

	/**
	 * 공통 버스노선 정보
	 * @return
	 */
	public BusRouteDetailOutputVo getCommonBusRouteInfoVo() {


		BusRouteDetailOutputVo busRouteDetailOutputVo = new BusRouteDetailOutputVo();
		busRouteDetailOutputVo.setRouteId(11000000);
		busRouteDetailOutputVo.setRouteName("103");
		busRouteDetailOutputVo.setCityCode(1000);
		busRouteDetailOutputVo.setBusClass(11);
		busRouteDetailOutputVo.setStartPointName("월계동");
		busRouteDetailOutputVo.setEndPointName("서울역");
		busRouteDetailOutputVo.setTurningPointSequence(36);
		busRouteDetailOutputVo.setProviderId(4);
		busRouteDetailOutputVo.setBypassYn("N");
		busRouteDetailOutputVo.setLocalRouteId("100100008");
		busRouteDetailOutputVo.setMondayYn("Y");
		busRouteDetailOutputVo.setTuesdayYn("Y");
		busRouteDetailOutputVo.setWednesdayYn("Y");
		busRouteDetailOutputVo.setThursdayYn("Y");
		busRouteDetailOutputVo.setFridayYn("Y");
		busRouteDetailOutputVo.setSaturdayYn("Y");
		busRouteDetailOutputVo.setSundayYn("Y");


		List<BusRouteCompanyOutputVo> busRouteCompanyOutputVoList = new ArrayList<>();
		BusRouteCompanyOutputVo busRouteCompanyOutputVo = new BusRouteCompanyOutputVo();
		busRouteCompanyOutputVo.setRouteId(11000000);
		busRouteCompanyOutputVo.setCompanyId(42);
		busRouteCompanyOutputVo.setCompanyName("삼화상운");
		busRouteCompanyOutputVo.setTel("02-918-6051");
		busRouteCompanyOutputVo.setCityCode(1000);
		busRouteCompanyOutputVo.setCityName("서울");

		busRouteCompanyOutputVoList.add(busRouteCompanyOutputVo);


		GeoJsonOutputVo geoJsonOutputVo = new GeoJsonOutputVo();

		// 구간 1
		GeoJsonFeatureGeometryVo geometry1 = new GeoJsonFeatureGeometryVo();
		geometry1.setType(CommonConstant.GEOJSON_GEOMETRY_TYPE_LINE_STRING);
		geometry1.setCoordinates(Arrays.asList(Arrays.asList(127.052801,37.620283), Arrays.asList(127.05331,37.619422), Arrays.asList(127.052389,37.618847)));

		BusStopGraphOutputVo properties1 = new BusStopGraphOutputVo();
		properties1.setGraphId(549);
		properties1.setStartStopId(55000529);
		properties1.setEndStopId(55000845);
		properties1.setStartStopName("광운초교앞");
		properties1.setEndStopName("장위래미안아파트");
		properties1.setMatchGraphInfoYn("Y");


		// 구간 2
		GeoJsonFeatureGeometryVo geometry2 = new GeoJsonFeatureGeometryVo();
		geometry2.setType(CommonConstant.GEOJSON_GEOMETRY_TYPE_LINE_STRING);
		geometry2.setCoordinates(Arrays.asList(Arrays.asList(127.052389,37.618847), Arrays.asList(127.051265,37.618108)));

		BusStopGraphOutputVo properties2 = new BusStopGraphOutputVo();
		properties2.setGraphId(875);
		properties2.setStartStopId(55000845);
		properties2.setEndStopId(55000524);
		properties2.setStartStopName("장위래미안아파트");
		properties2.setEndStopName("꿈의숲아이파크아파트");
		properties2.setMatchGraphInfoYn("Y");



		// feature 1
		GeoJsonFeatureVo geoJsonFeatureVo1 = new GeoJsonFeatureVo() ;
		geoJsonFeatureVo1.setType(CommonConstant.GEOJSON_TYPE_FEATURE_TYPE);
		geoJsonFeatureVo1.setProperties(properties1);
		geoJsonFeatureVo1.setGeometry(geometry1);

		// feature
		GeoJsonFeatureVo geoJsonFeatureVo2 = new GeoJsonFeatureVo() ;
		geoJsonFeatureVo2.setType(CommonConstant.GEOJSON_TYPE_FEATURE_TYPE);
		geoJsonFeatureVo2.setProperties(properties2);
		geoJsonFeatureVo2.setGeometry(geometry2);

		// feature List
		List<GeoJsonFeatureVo> geoJsonFeatureVoList = new ArrayList<>();
		geoJsonFeatureVoList.add(geoJsonFeatureVo1);
		geoJsonFeatureVoList.add(geoJsonFeatureVo2);


		geoJsonOutputVo.setFeatures(geoJsonFeatureVoList);

		busRouteDetailOutputVo.setCompanyList(busRouteCompanyOutputVoList);
		busRouteDetailOutputVo.setBusStopGraphInfo(geoJsonOutputVo);

		return busRouteDetailOutputVo;
	}

	/**
	 * 공통 작업정보
	 * @return
	 */
	public TaskOutputVo getCommonBusTaskInfoVo() {
		TaskOutputVo taskOutputVo = new TaskOutputVo() ;
		taskOutputVo.setTaskId(122);
		taskOutputVo.setProviderId(4);
		taskOutputVo.setProviderName("서울");
		taskOutputVo.setTaskType("modify");
		taskOutputVo.setTaskStatus("01");
		taskOutputVo.setPubTransId(11000000);
		taskOutputVo.setTaskDataType("route");
		taskOutputVo.setTaskDataName("103");
		taskOutputVo.setTaskComment("노선명칭 변경");
		taskOutputVo.setTaskRegisterType("M");
		taskOutputVo.setRegUserName("안경현");
		taskOutputVo.setRegUserId("kr94666");
		taskOutputVo.setRegDate("2020-03-06 13:45:49");
		taskOutputVo.setWorkUserName("안경현");
		taskOutputVo.setWorkUserId("kr94666");
		taskOutputVo.setWorkAssignDate("2020-03-06 13:45:14");
		taskOutputVo.setWorkCompleteDate("2020-03-06 13:45:29");

		return taskOutputVo;
	}


	/**
	 * 버스노선 작업정보
	 * @return
	 */
	public BusRouteTaskInputVo getBusRouteTaskInputVo() {
		BusRouteTaskInputVo busRouteTaskInputVo = new BusRouteTaskInputVo();
		busRouteTaskInputVo.setRouteName("103");
		busRouteTaskInputVo.setCityCode(1000);
		busRouteTaskInputVo.setBusClass(11);
		busRouteTaskInputVo.setStartPointName("월계동");
		busRouteTaskInputVo.setEndPointName("서울역");
		busRouteTaskInputVo.setTurningPointSequence(36);
		busRouteTaskInputVo.setWeekdayStartPointFirstTime("0430");
		busRouteTaskInputVo.setWeekdayStartPointLastTime("2300");
		busRouteTaskInputVo.setWeekdayEndPointFirstTime("0500");
		busRouteTaskInputVo.setWeekdayEndPointLastTime("2330");
		busRouteTaskInputVo.setWeekdayIntervalMin(7);
		busRouteTaskInputVo.setWeekdayIntervalMax(8);
		busRouteTaskInputVo.setWeekdayIntervalCount(35);
		busRouteTaskInputVo.setSaturdayStartPointFirstTime("0430");
		busRouteTaskInputVo.setSaturdayStartPointLastTime("2300");
		busRouteTaskInputVo.setSaturdayEndPointFirstTime("0530");
		busRouteTaskInputVo.setSaturdayEndPointLastTime("2330");
		busRouteTaskInputVo.setSaturdayIntervalMin(7);
		busRouteTaskInputVo.setSaturdayIntervalMax(10);
		busRouteTaskInputVo.setSaturdayIntervalCount(30);
		busRouteTaskInputVo.setSundayStartPointFirstTime("0430");
		busRouteTaskInputVo.setSundayStartPointLastTime("2300");
		busRouteTaskInputVo.setSundayEndPointFirstTime("0530");
		busRouteTaskInputVo.setSundayEndPointLastTime("2330");
		busRouteTaskInputVo.setSundayIntervalMin(7);
		busRouteTaskInputVo.setSundayIntervalMax(11);
		busRouteTaskInputVo.setSundayIntervalCount(30);
		busRouteTaskInputVo.setNonstepBusYn("N");
		busRouteTaskInputVo.setTelReservation("02-1234-5678");
		busRouteTaskInputVo.setProviderId(4);
		busRouteTaskInputVo.setBypassYn("Y");
		busRouteTaskInputVo.setParentRouteId(11000009);
		busRouteTaskInputVo.setBypassStartDateTime("202003251200");
		busRouteTaskInputVo.setBypassEndDateTime("202003312400");
		busRouteTaskInputVo.setLocalRouteId("100100008");
		busRouteTaskInputVo.setMondayYn("Y");
		busRouteTaskInputVo.setTuesdayYn("Y");
		busRouteTaskInputVo.setWednesdayYn("Y");
		busRouteTaskInputVo.setThursdayYn("Y");
		busRouteTaskInputVo.setFridayYn("Y");
		busRouteTaskInputVo.setSaturdayYn("Y");
		busRouteTaskInputVo.setSundayYn("Y");
		busRouteTaskInputVo.setTaskComment("신규노선 추가");
		busRouteTaskInputVo.setCheckUserId("kr94666");


		BusRouteCompanyTaskInputVo busRoutecompanyTaskInputVo = new BusRouteCompanyTaskInputVo();
		busRoutecompanyTaskInputVo.setCompanyId(42);
		List<BusRouteCompanyTaskInputVo> companyList = new ArrayList<>();
		companyList.add(busRoutecompanyTaskInputVo);

		// 운수회사
		busRouteTaskInputVo.setCompanyList(companyList);



		GeoJsonInputVo geoJsonInputVo = new GeoJsonInputVo();

		// 구간 1
		GeoJsonFeatureGeometryVo geometry1 = new GeoJsonFeatureGeometryVo();
		geometry1.setType(CommonConstant.GEOJSON_GEOMETRY_TYPE_LINE_STRING);
		geometry1.setCoordinates(Arrays.asList(Arrays.asList(127.052801,37.620283), Arrays.asList(127.05331,37.619422), Arrays.asList(127.052389,37.618847)));

		BusStopGraphInputVo properties1 = new BusStopGraphInputVo();
		properties1.setGraphId(549);
		properties1.setStartStopId(55000529);
		properties1.setEndStopId(55000845);


		// 구간 2
		GeoJsonFeatureGeometryVo geometry2 = new GeoJsonFeatureGeometryVo();
		geometry2.setType(CommonConstant.GEOJSON_GEOMETRY_TYPE_LINE_STRING);
		geometry2.setCoordinates(Arrays.asList(Arrays.asList(127.052389,37.618847), Arrays.asList(127.051265,37.618108)));

		BusStopGraphInputVo properties2 = new BusStopGraphInputVo();
		properties2.setGraphId(875);
		properties2.setStartStopId(55000845);
		properties2.setEndStopId(55000524);



		// feature 1
		GeoJsonFeatureInputVo geoJsonFeatureVo1 = new GeoJsonFeatureInputVo() ;
		geoJsonFeatureVo1.setType(CommonConstant.GEOJSON_TYPE_FEATURE_TYPE);
		geoJsonFeatureVo1.setProperties(properties1);
		geoJsonFeatureVo1.setGeometry(geometry1);

		// feature
		GeoJsonFeatureInputVo geoJsonFeatureVo2 = new GeoJsonFeatureInputVo() ;
		geoJsonFeatureVo2.setType(CommonConstant.GEOJSON_TYPE_FEATURE_TYPE);
		geoJsonFeatureVo2.setProperties(properties2);
		geoJsonFeatureVo2.setGeometry(geometry2);

		// feature List
		List<GeoJsonFeatureInputVo> geoJsonFeatureVoList = new ArrayList<>();
		geoJsonFeatureVoList.add(geoJsonFeatureVo1);
		geoJsonFeatureVoList.add(geoJsonFeatureVo2);

		geoJsonInputVo.setType(CommonConstant.GEOJSON_TYPE_FEATURE_COLLECTION);
		geoJsonInputVo.setFeatures(geoJsonFeatureVoList);



		busRouteTaskInputVo.setBusStopGraphInfo(geoJsonInputVo);

		return busRouteTaskInputVo;
	}

}
