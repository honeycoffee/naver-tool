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
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.controller.FareController;
import com.naver.pubtrans.itn.api.handler.MemberAccessDeniedHandler;
import com.naver.pubtrans.itn.api.service.FareService;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.fare.FareInfoVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareSearchVo;
import com.naver.pubtrans.itn.api.vo.fare.output.FareOutputVoWithRouteList;
import com.naver.pubtrans.itn.api.vo.fare.output.FareTaskOutputVoWithRouteList;
import com.naver.pubtrans.itn.api.vo.fare.output.IgnoredFareListOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 요금 룰 Rest Docs 생성 Test Class
 * @author westwind
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(FareController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class FareControllerDocumentTest {

	private static final List<JsonFieldType> STRING_OR_NULL = Arrays.asList(JsonFieldType.STRING, JsonFieldType.NULL);
	private static final List<JsonFieldType> NUMBER_OR_NULL = Arrays.asList(JsonFieldType.NUMBER, JsonFieldType.NULL);
	private static final List<JsonFieldType> OBJECT_OR_NULL = Arrays.asList(JsonFieldType.OBJECT, JsonFieldType.NULL);

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OutputFmtUtil outputFmtUtil;

	@MockBean
	private FareService fareService;

	@MockBean
	private JwtAdapter jwtAdapter;

	@MockBean
	private MemberAccessDeniedHandler memberAccessDeniedHandler;

	/**
	 * 기본 요금 룰 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void getBaseFareRule() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();

		// 스키마
		SchemaVo schemaSeqVo = new SchemaVo();
		schemaSeqVo.setColumnName("fareId");
		schemaSeqVo.setColumnComment("요금 ID");
		schemaSeqVo.setIsNullable("NO");
		schemaSeqVo.setColumnKey("PRI");
		schemaSeqVo.setColumnType("int(11)");

		SchemaVo schemaTitleVo = new SchemaVo();
		schemaTitleVo.setColumnName("startStopId");
		schemaTitleVo.setColumnComment("시작정류장");
		schemaTitleVo.setIsNullable("YES");
		schemaTitleVo.setColumnKey("");
		schemaTitleVo.setColumnType("int(11)");

		List<SchemaVo> schemaVoList = new ArrayList<>();
		schemaVoList.add(schemaSeqVo);
		schemaVoList.add(schemaTitleVo);

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, null);

		FareSearchVo fareSearchVo = new FareSearchVo();
		fareSearchVo.setCityCode(1000);
		fareSearchVo.setBusClass(11);
		
		FareInfoVo fareInfoVo = new FareInfoVo();
		
		fareInfoVo.setFareId(1);
		fareInfoVo.setBaseFare(1200);
		fareInfoVo.setBaseDist(10);
		fareInfoVo.setUnitFare(100);
		fareInfoVo.setUnitDist(5);
		fareInfoVo.setMaxFare(2300);
		fareInfoVo.setBaseYn("Y");
		fareInfoVo.setAgeId(1);
		fareInfoVo.setPaymentId(1);
		
		
		List<BusRouteListOutputVo> busRouteListOutputVoList = new ArrayList<BusRouteListOutputVo>(); 
		
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
		
		busRouteListOutputVoList.add(busRouteListOutputVo);
		

		FareOutputVoWithRouteList fareOutputVoWithRouteList = new FareOutputVoWithRouteList();

		fareOutputVoWithRouteList.setFareId(1);
		fareOutputVoWithRouteList.setStartStopId(111);
		fareOutputVoWithRouteList.setEndStopId(222);
		fareOutputVoWithRouteList.setServiceId(5);
		fareOutputVoWithRouteList.setCityCode(1000);
		fareOutputVoWithRouteList.setBusClass(11);
		fareOutputVoWithRouteList.setSourceName("출처");
		fareOutputVoWithRouteList.setSourceUrl("arointech.co.kr");
		fareOutputVoWithRouteList.setComment("설명");
		fareOutputVoWithRouteList.setTotalRouteCount(3);
		fareOutputVoWithRouteList.setCreateDate("2020.04.17");
		fareOutputVoWithRouteList.setSourceName("출처");
		fareOutputVoWithRouteList.setGeneralCardFare(fareInfoVo);
		fareOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareOutputVoWithRouteList);

		//given
		given(fareService.getFareRule(any(FareSearchVo.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/info/fare")
				.param("cityCode", "1000")
				.param("busClass", "11")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("fare/getBaseFare",
				getDocumentRequest(),
				getDocumentResponse(),
				requestParameters(
            		parameterWithName("cityCode").description("[필수]도시코드"),
            		parameterWithName("busClass").description("[필수]노선 타입")),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
					fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

					subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY)
						.description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]").optional(),

					fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
					fieldWithPath("result.data.fareId").type(JsonFieldType.NUMBER).description("요금 룰 ID"),
					fieldWithPath("result.data.startStopId").type(JsonFieldType.NUMBER).description("시작 정류장"),
					fieldWithPath("result.data.endStopId").type(JsonFieldType.NUMBER).description("도착 정류장"),
					fieldWithPath("result.data.serviceId").type(JsonFieldType.NUMBER).description("운행 요일 ID"),
					fieldWithPath("result.data.cityCode").type(JsonFieldType.NUMBER).description("도시 코드"),
					fieldWithPath("result.data.busClass").type(JsonFieldType.NUMBER).description("버스 노선 종류"),
					fieldWithPath("result.data.sourceName").type(JsonFieldType.STRING).description("출처명"),
					fieldWithPath("result.data.sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
					fieldWithPath("result.data.comment").type(JsonFieldType.STRING).description("설명"),
					fieldWithPath("result.data.totalRouteCount").type(JsonFieldType.NUMBER).description("총 노선수"),
					fieldWithPath("result.data.createDate").type(JsonFieldType.STRING).description("생성일"),
					
					fieldWithPath("result.data.generalCardFare").type(JsonFieldType.OBJECT).description("카드일반요금"),
					fieldWithPath("result.data.generalCardFare.fareId").type(JsonFieldType.NUMBER).description("요금 ID"),
					fieldWithPath("result.data.generalCardFare.baseFare").type(JsonFieldType.NUMBER).description("기본요금"),
					fieldWithPath("result.data.generalCardFare.baseDist").type(JsonFieldType.NUMBER).description("기본거리"),
					fieldWithPath("result.data.generalCardFare.unitFare").type(JsonFieldType.NUMBER).description("단위요금"),
					fieldWithPath("result.data.generalCardFare.unitDist").type(JsonFieldType.NUMBER).description("단위거리"),
					fieldWithPath("result.data.generalCardFare.maxFare").type(JsonFieldType.NUMBER).description("최대 요금"),
					fieldWithPath("result.data.generalCardFare.baseYn").type(JsonFieldType.STRING).description("요금 룰의 기본 정보 여부"),
					fieldWithPath("result.data.generalCardFare.ageId").type(JsonFieldType.NUMBER).description("연령별 타입"),
					fieldWithPath("result.data.generalCardFare.paymentId").type(JsonFieldType.NUMBER).description("지불 방식 식별"),
					
					fieldWithPath("result.data.busRouteInfoList[]").type(JsonFieldType.ARRAY).description("예외노선 목록"),
					fieldWithPath("result.data.busRouteInfoList[].routeId").type(JsonFieldType.NUMBER).description("노선ID"),
					fieldWithPath("result.data.busRouteInfoList[].routeName").type(JsonFieldType.STRING).description("노선명"),
					fieldWithPath("result.data.busRouteInfoList[].busClass").type(JsonFieldType.NUMBER).description("노선 타입"),
					fieldWithPath("result.data.busRouteInfoList[].busClassName").type(JsonFieldType.STRING).description("노선 타입 명칭"),
					fieldWithPath("result.data.busRouteInfoList[].startPointName").type(JsonFieldType.STRING).description("기점 정류장 명"),
					fieldWithPath("result.data.busRouteInfoList[].endPointName").type(JsonFieldType.STRING).description("종점 정류장 명"),
					fieldWithPath("result.data.busRouteInfoList[].cityCode").type(JsonFieldType.NUMBER).description("도시코드"),
					fieldWithPath("result.data.busRouteInfoList[].cityName").type(JsonFieldType.STRING).description("도시코드 명칭"),
					fieldWithPath("result.data.busRouteInfoList[].bypassYn").type(JsonFieldType.STRING).description("우회노선 여부 - 해당 노선이 우회노선 인지"),
					fieldWithPath("result.data.busRouteInfoList[].bypassRouteCnt").type(JsonFieldType.NUMBER).description("우회노선 수"))));

	}

	/**
	 * 예외 요금 룰 목록 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void selectIgnoredFareRuleList() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();

		FareSearchVo fareSearchVo = new FareSearchVo();
		fareSearchVo.setCityCode(1000);
		fareSearchVo.setBusClass(11);
		
		List<IgnoredFareListOutputVo> ignoredFareListOutputVoList = new ArrayList<IgnoredFareListOutputVo>();
		IgnoredFareListOutputVo ignoredFareListOutputVo1 = new IgnoredFareListOutputVo();
		IgnoredFareListOutputVo ignoredFareListOutputVo2 = new IgnoredFareListOutputVo();
		
		ignoredFareListOutputVo1.setFareId(1);
		ignoredFareListOutputVo1.setOrder(1);
		ignoredFareListOutputVo1.setRoutes("501, 502");
		ignoredFareListOutputVo1.setIgnoredFareName();
		
		ignoredFareListOutputVoList.add(ignoredFareListOutputVo1);
		
		ignoredFareListOutputVo2.setFareId(2);
		ignoredFareListOutputVo2.setOrder(2);
		ignoredFareListOutputVo2.setRoutes("601, 602");
		ignoredFareListOutputVo2.setIgnoredFareName();
		
		ignoredFareListOutputVoList.add(ignoredFareListOutputVo2);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(ignoredFareListOutputVoList);

		//given
		given(fareService.selectIgnoredFareRuleList(any(FareSearchVo.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/list/ignoredFare")
				.param("cityCode", "1000")
				.param("busClass", "11")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("fare/selectIgnoredFareRuleList",
				getDocumentRequest(),
				getDocumentResponse(),
				requestParameters(
            		parameterWithName("cityCode").description("[필수]도시코드"),
            		parameterWithName("busClass").description("[필수]노선 타입")),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
					fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

					fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("예외 요금 룰 목록"),
					fieldWithPath("result.data[].fareId").type(JsonFieldType.NUMBER).description("요금 룰 ID"),
					fieldWithPath("result.data[].ignoredFareName").type(JsonFieldType.STRING).description("예외 요금 명"))));

	}

	/**
	 * 예외 요금 룰 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void getIgnoredFareRule() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();

		// 스키마
		SchemaVo schemaSeqVo = new SchemaVo();
		schemaSeqVo.setColumnName("fareId");
		schemaSeqVo.setColumnComment("요금 ID");
		schemaSeqVo.setIsNullable("NO");
		schemaSeqVo.setColumnKey("PRI");
		schemaSeqVo.setColumnType("int(11)");

		SchemaVo schemaTitleVo = new SchemaVo();
		schemaTitleVo.setColumnName("startStopId");
		schemaTitleVo.setColumnComment("시작정류장");
		schemaTitleVo.setIsNullable("YES");
		schemaTitleVo.setColumnKey("");
		schemaTitleVo.setColumnType("int(11)");

		List<SchemaVo> schemaVoList = new ArrayList<>();
		schemaVoList.add(schemaSeqVo);
		schemaVoList.add(schemaTitleVo);

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, null);

		FareInfoVo fareInfoVo = new FareInfoVo();
		
		fareInfoVo.setFareId(1);
		fareInfoVo.setBaseFare(1200);
		fareInfoVo.setBaseDist(10);
		fareInfoVo.setUnitFare(100);
		fareInfoVo.setUnitDist(5);
		fareInfoVo.setMaxFare(2300);
		fareInfoVo.setBaseYn("Y");
		fareInfoVo.setAgeId(1);
		fareInfoVo.setPaymentId(1);
		
		
		List<BusRouteListOutputVo> busRouteListOutputVoList = new ArrayList<BusRouteListOutputVo>(); 
		
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
		
		busRouteListOutputVoList.add(busRouteListOutputVo);
		

		FareOutputVoWithRouteList fareOutputVoWithRouteList = new FareOutputVoWithRouteList();

		fareOutputVoWithRouteList.setFareId(1);
		fareOutputVoWithRouteList.setStartStopId(111);
		fareOutputVoWithRouteList.setEndStopId(222);
		fareOutputVoWithRouteList.setServiceId(5);
		fareOutputVoWithRouteList.setCityCode(1000);
		fareOutputVoWithRouteList.setBusClass(11);
		fareOutputVoWithRouteList.setSourceName("출처");
		fareOutputVoWithRouteList.setSourceUrl("arointech.co.kr");
		fareOutputVoWithRouteList.setComment("설명");
		fareOutputVoWithRouteList.setTotalRouteCount(3);
		fareOutputVoWithRouteList.setCreateDate("2020.04.17");
		fareOutputVoWithRouteList.setSourceName("출처");
		fareOutputVoWithRouteList.setGeneralCardFare(fareInfoVo);
		fareOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareOutputVoWithRouteList);

		//given
		given(fareService.getFareRule(any(FareSearchVo.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/info/fare/{fareId}", 2)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("fare/getIgnoredFare",
				getDocumentRequest(),
				getDocumentResponse(),
	            pathParameters(
                    parameterWithName("fareId").description("요금 룰 ID")
            	),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
					fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

					subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY)
						.description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]").optional(),

					fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
					fieldWithPath("result.data.fareId").type(JsonFieldType.NUMBER).description("요금 룰 ID"),
					fieldWithPath("result.data.startStopId").type(JsonFieldType.NUMBER).description("시작 정류장"),
					fieldWithPath("result.data.endStopId").type(JsonFieldType.NUMBER).description("도착 정류장"),
					fieldWithPath("result.data.serviceId").type(JsonFieldType.NUMBER).description("운행 요일 ID"),
					fieldWithPath("result.data.cityCode").type(JsonFieldType.NUMBER).description("도시 코드"),
					fieldWithPath("result.data.busClass").type(JsonFieldType.NUMBER).description("버스 노선 종류"),
					fieldWithPath("result.data.sourceName").type(JsonFieldType.STRING).description("출처명"),
					fieldWithPath("result.data.sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
					fieldWithPath("result.data.comment").type(JsonFieldType.STRING).description("설명"),
					fieldWithPath("result.data.totalRouteCount").type(JsonFieldType.NUMBER).description("총 노선수"),
					fieldWithPath("result.data.createDate").type(JsonFieldType.STRING).description("생성일"),
					
					fieldWithPath("result.data.generalCardFare").type(JsonFieldType.OBJECT).description("카드일반요금"),
					fieldWithPath("result.data.generalCardFare.fareId").type(JsonFieldType.NUMBER).description("요금 ID"),
					fieldWithPath("result.data.generalCardFare.baseFare").type(JsonFieldType.NUMBER).description("기본요금"),
					fieldWithPath("result.data.generalCardFare.baseDist").type(JsonFieldType.NUMBER).description("기본거리"),
					fieldWithPath("result.data.generalCardFare.unitFare").type(JsonFieldType.NUMBER).description("단위요금"),
					fieldWithPath("result.data.generalCardFare.unitDist").type(JsonFieldType.NUMBER).description("단위거리"),
					fieldWithPath("result.data.generalCardFare.maxFare").type(JsonFieldType.NUMBER).description("최대 요금"),
					fieldWithPath("result.data.generalCardFare.baseYn").type(JsonFieldType.STRING).description("요금 룰의 기본 정보 여부"),
					fieldWithPath("result.data.generalCardFare.ageId").type(JsonFieldType.NUMBER).description("연령별 타입"),
					fieldWithPath("result.data.generalCardFare.paymentId").type(JsonFieldType.NUMBER).description("지불 방식 식별"),
					
					fieldWithPath("result.data.busRouteInfoList[]").type(JsonFieldType.ARRAY).description("예외노선 목록"),
					fieldWithPath("result.data.busRouteInfoList[].routeId").type(JsonFieldType.NUMBER).description("노선ID"),
					fieldWithPath("result.data.busRouteInfoList[].routeName").type(JsonFieldType.STRING).description("노선명"),
					fieldWithPath("result.data.busRouteInfoList[].busClass").type(JsonFieldType.NUMBER).description("노선 타입"),
					fieldWithPath("result.data.busRouteInfoList[].busClassName").type(JsonFieldType.STRING).description("노선 타입 명칭"),
					fieldWithPath("result.data.busRouteInfoList[].startPointName").type(JsonFieldType.STRING).description("기점 정류장 명"),
					fieldWithPath("result.data.busRouteInfoList[].endPointName").type(JsonFieldType.STRING).description("종점 정류장 명"),
					fieldWithPath("result.data.busRouteInfoList[].cityCode").type(JsonFieldType.NUMBER).description("도시코드"),
					fieldWithPath("result.data.busRouteInfoList[].cityName").type(JsonFieldType.STRING).description("도시코드 명칭"),
					fieldWithPath("result.data.busRouteInfoList[].bypassYn").type(JsonFieldType.STRING).description("우회노선 여부 - 해당 노선이 우회노선 인지"),
					fieldWithPath("result.data.busRouteInfoList[].bypassRouteCnt").type(JsonFieldType.NUMBER).description("우회노선 수"))));

	}


	/**
	 * 요금 룰 상세정보 작업정보 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoFareTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		// 스키마
		SchemaVo schemaSeqVo = new SchemaVo();
		schemaSeqVo.setColumnName("fareId");
		schemaSeqVo.setColumnComment("요금 ID");
		schemaSeqVo.setIsNullable("NO");
		schemaSeqVo.setColumnKey("PRI");
		schemaSeqVo.setColumnType("int(11)");

		SchemaVo schemaTitleVo = new SchemaVo();
		schemaTitleVo.setColumnName("startStopId");
		schemaTitleVo.setColumnComment("시작정류장");
		schemaTitleVo.setIsNullable("YES");
		schemaTitleVo.setColumnKey("");
		schemaTitleVo.setColumnType("int(11)");

		List<SchemaVo> schemaVoList = new ArrayList<>();
		schemaVoList.add(schemaSeqVo);
		schemaVoList.add(schemaTitleVo);

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, null);

		FareInfoVo fareInfoVo = new FareInfoVo();
		
		fareInfoVo.setFareId(1);
		fareInfoVo.setBaseFare(1200);
		fareInfoVo.setBaseDist(10);
		fareInfoVo.setUnitFare(100);
		fareInfoVo.setUnitDist(5);
		fareInfoVo.setMaxFare(2300);
		fareInfoVo.setBaseYn("Y");
		fareInfoVo.setAgeId(1);
		fareInfoVo.setPaymentId(1);
		
		
		List<BusRouteListOutputVo> busRouteListOutputVoList = new ArrayList<BusRouteListOutputVo>(); 
		
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
		
		busRouteListOutputVoList.add(busRouteListOutputVo);
		

		FareTaskOutputVoWithRouteList fareTaskOutputVoWithRouteList = new FareTaskOutputVoWithRouteList();

		fareTaskOutputVoWithRouteList.setFareId(1);
		fareTaskOutputVoWithRouteList.setStartStopId(111);
		fareTaskOutputVoWithRouteList.setEndStopId(222);
		fareTaskOutputVoWithRouteList.setServiceId(5);
		fareTaskOutputVoWithRouteList.setCityCode(1000);
		fareTaskOutputVoWithRouteList.setBusClass(11);
		fareTaskOutputVoWithRouteList.setSourceName("출처");
		fareTaskOutputVoWithRouteList.setSourceUrl("arointech.co.kr");
		fareTaskOutputVoWithRouteList.setComment("설명");
		fareTaskOutputVoWithRouteList.setTotalRouteCount(3);
		fareTaskOutputVoWithRouteList.setCreateDate("2020.04.17");
		fareTaskOutputVoWithRouteList.setSourceName("출처");
		fareTaskOutputVoWithRouteList.setGeneralCardFare(fareInfoVo);
		fareTaskOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);


		TaskOutputVo taskOutputVo = new TaskOutputVo() ;
		taskOutputVo.setTaskId(1);
		taskOutputVo.setProviderId(4);
		taskOutputVo.setProviderName("서울");
		taskOutputVo.setTaskType("register");
		taskOutputVo.setTaskStatus("01");
		taskOutputVo.setPubTransId(1);
		taskOutputVo.setTaskDataType("company");
		taskOutputVo.setTaskDataName("서울운수");
		taskOutputVo.setTaskComment("주소 변경");
		taskOutputVo.setTaskRegisterType("M");
		taskOutputVo.setRegUserName("test_name");
		taskOutputVo.setRegUserId("test");
		taskOutputVo.setRegDate("2020-04-10 10:00:00");
		taskOutputVo.setWorkUserName("test_name");
		taskOutputVo.setWorkUserId("test");
		taskOutputVo.setWorkAssignDate("2020-04-10 10:00:00");
		taskOutputVo.setWorkCompleteDate("2020-04-10 10:00:00");

		fareTaskOutputVoWithRouteList.setTaskInfo(taskOutputVo);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareTaskOutputVoWithRouteList);


		//given
		given(fareService.getFareTaskInfo(1))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/info/fareTask/{taskId}", 1L)
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("fare/fareTask",
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

	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("요금 룰 정보"),
						fieldWithPath("result.data.fareId").type(JsonFieldType.NUMBER).description("요금 룰 ID"),
						fieldWithPath("result.data.startStopId").type(JsonFieldType.NUMBER).description("시작 정류장"),
						fieldWithPath("result.data.endStopId").type(JsonFieldType.NUMBER).description("도착 정류장"),
						fieldWithPath("result.data.serviceId").type(JsonFieldType.NUMBER).description("운행 요일 ID"),
						fieldWithPath("result.data.cityCode").type(JsonFieldType.NUMBER).description("도시 코드"),
						fieldWithPath("result.data.busClass").type(JsonFieldType.NUMBER).description("버스 노선 종류"),
						fieldWithPath("result.data.sourceName").type(JsonFieldType.STRING).description("출처명"),
						fieldWithPath("result.data.sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
						fieldWithPath("result.data.comment").type(JsonFieldType.STRING).description("설명"),
						fieldWithPath("result.data.totalRouteCount").type(JsonFieldType.NUMBER).description("총 노선수"),
						fieldWithPath("result.data.createDate").type(JsonFieldType.STRING).description("생성일"),
						
						fieldWithPath("result.data.generalCardFare").type(JsonFieldType.OBJECT).description("카드일반요금"),
						fieldWithPath("result.data.generalCardFare.fareId").type(JsonFieldType.NUMBER).description("요금 ID"),
						fieldWithPath("result.data.generalCardFare.baseFare").type(JsonFieldType.NUMBER).description("기본요금"),
						fieldWithPath("result.data.generalCardFare.baseDist").type(JsonFieldType.NUMBER).description("기본거리"),
						fieldWithPath("result.data.generalCardFare.unitFare").type(JsonFieldType.NUMBER).description("단위요금"),
						fieldWithPath("result.data.generalCardFare.unitDist").type(JsonFieldType.NUMBER).description("단위거리"),
						fieldWithPath("result.data.generalCardFare.maxFare").type(JsonFieldType.NUMBER).description("최대 요금"),
						fieldWithPath("result.data.generalCardFare.baseYn").type(JsonFieldType.STRING).description("요금 룰의 기본 정보 여부"),
						fieldWithPath("result.data.generalCardFare.ageId").type(JsonFieldType.NUMBER).description("연령별 타입"),
						fieldWithPath("result.data.generalCardFare.paymentId").type(JsonFieldType.NUMBER).description("지불 방식 식별"),
						
						fieldWithPath("result.data.busRouteInfoList[]").type(JsonFieldType.ARRAY).description("예외노선 목록"),
						fieldWithPath("result.data.busRouteInfoList[].routeId").type(JsonFieldType.NUMBER).description("노선ID"),
						fieldWithPath("result.data.busRouteInfoList[].routeName").type(JsonFieldType.STRING).description("노선명"),
						fieldWithPath("result.data.busRouteInfoList[].busClass").type(JsonFieldType.NUMBER).description("노선 타입"),
						fieldWithPath("result.data.busRouteInfoList[].busClassName").type(JsonFieldType.STRING).description("노선 타입 명칭"),
						fieldWithPath("result.data.busRouteInfoList[].startPointName").type(JsonFieldType.STRING).description("기점 정류장 명"),
						fieldWithPath("result.data.busRouteInfoList[].endPointName").type(JsonFieldType.STRING).description("종점 정류장 명"),
						fieldWithPath("result.data.busRouteInfoList[].cityCode").type(JsonFieldType.NUMBER).description("도시코드"),
						fieldWithPath("result.data.busRouteInfoList[].cityName").type(JsonFieldType.STRING).description("도시코드 명칭"),
						fieldWithPath("result.data.busRouteInfoList[].bypassYn").type(JsonFieldType.STRING).description("우회노선 여부 - 해당 노선이 우회노선 인지"),
						fieldWithPath("result.data.busRouteInfoList[].bypassRouteCnt").type(JsonFieldType.NUMBER).description("우회노선 수"),

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
	 * 요금 룰 스키마 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void schemaFare() throws Exception {

		List<CommonSchema> commonSchema = new ArrayList<>();

		//given
		given(fareService.selectFareSchemaAll())
			.willReturn(commonSchema);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/schema/fare")
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("fare/fareSchema",
				getDocumentRequest(),
				getDocumentResponse(),

				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
					subsectionWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보").optional(),

					subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY)
						.description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"))));
	}

}
