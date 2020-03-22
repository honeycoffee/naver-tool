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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.controller.BusRouteController;
import com.naver.pubtrans.itn.api.service.BusRouteService;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureGeometryVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.output.BusStopGraphOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.output.GeoJsonOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.task.output.TaskSummaryOutputVo;

@RunWith(SpringRunner.class)
@WebMvcTest(BusRouteController.class)
@AutoConfigureRestDocs
public class BusRouteControllerDocumentTest {
	private static final List<JsonFieldType> STRING_OR_NULL = Arrays.asList(JsonFieldType.STRING, JsonFieldType.NULL) ;
	private static final List<JsonFieldType> NUMBER_OR_NULL = Arrays.asList(JsonFieldType.NUMBER, JsonFieldType.NULL) ;
	private static final List<JsonFieldType> OBJECT_OR_NULL = Arrays.asList(JsonFieldType.OBJECT, JsonFieldType.NULL) ;

	@Autowired
	MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@MockBean
	private BusRouteService busRouteService ;

	@MockBean
	private OutputFmtUtil outputFmtUtil ;

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
		busRouteListOutputVo.setCityCode("1000");
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
	            		parameterWithName("routeName").description("[선택]정류장명").optional(),
	            		parameterWithName("routeId").description("[선택]정류장ID").optional(),
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
	             		fieldWithPath("result.data[].cityCode").type(JsonFieldType.STRING).description("도시코드"),
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
}
