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
import com.naver.pubtrans.itn.api.consts.CodeType;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.controller.FareRuleController;
import com.naver.pubtrans.itn.api.handler.MemberAccessDeniedHandler;
import com.naver.pubtrans.itn.api.service.FareRuleService;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.fare.FareRuleInfoVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleSearchVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.output.ExceptionalFareRuleOutputVo;
import com.naver.pubtrans.itn.api.vo.fare.output.FareRuleOutputVoWithRouteList;
import com.naver.pubtrans.itn.api.vo.fare.output.FareRuleTaskOutputVoWithRouteList;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskSummaryOutputVo;

/**
 * 요금 룰 Rest Docs 생성 Test Class
 * @author westwind
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(FareRuleController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class FareRuleControllerDocumentTest {

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
	private FareRuleService fareRuleService;

	@MockBean
	private JwtAdapter jwtAdapter;

	@MockBean
	private MemberAccessDeniedHandler memberAccessDeniedHandler;

	/**
	 * 기본 요금 룰 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoBaseFareRule() throws Exception {

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

		FareRuleSearchVo fareRuleSearchVo = new FareRuleSearchVo();
		fareRuleSearchVo.setCityCode(1000);
		fareRuleSearchVo.setBusClass(11);
		
		FareRuleInfoVo fareRuleInfoVo = new FareRuleInfoVo();
		
		fareRuleInfoVo.setFareId(1);
		fareRuleInfoVo.setBaseFare(1200);
		fareRuleInfoVo.setBaseDist(10);
		fareRuleInfoVo.setUnitFare(100);
		fareRuleInfoVo.setUnitDist(5);
		fareRuleInfoVo.setMaxFare(2300);
		fareRuleInfoVo.setBaseYn("Y");
		fareRuleInfoVo.setAgeId(1);
		fareRuleInfoVo.setPaymentId(1);
		
		
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
		

		FareRuleOutputVoWithRouteList fareRuleOutputVoWithRouteList = new FareRuleOutputVoWithRouteList();

		fareRuleOutputVoWithRouteList.setFareId(1);
		fareRuleOutputVoWithRouteList.setServiceId(5);
		fareRuleOutputVoWithRouteList.setCityCode(1000);
		fareRuleOutputVoWithRouteList.setBusClass(11);
		fareRuleOutputVoWithRouteList.setSourceName("출처");
		fareRuleOutputVoWithRouteList.setSourceUrl("arointech.co.kr");
		fareRuleOutputVoWithRouteList.setComment("설명");
		fareRuleOutputVoWithRouteList.setTotalRouteCount(3);
		fareRuleOutputVoWithRouteList.setCreateDate("2020.04.17");
		fareRuleOutputVoWithRouteList.setUpdateDate("2020.04.17");
		fareRuleOutputVoWithRouteList.setSourceName("출처");
		fareRuleOutputVoWithRouteList.setGeneralCardFareRuleInfo(fareRuleInfoVo);
		fareRuleOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareRuleOutputVoWithRouteList);

		//given
		given(fareRuleService.getBaseFareRule(any(FareRuleSearchVo.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/info/baseFareRule")
				.param("cityCode", "1000")
				.param("busClass", "11")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("fareRule/infoBaseFareRule",
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
					fieldWithPath("result.data.serviceId").type(JsonFieldType.NUMBER).description("운행 요일 ID"),
					fieldWithPath("result.data.cityCode").type(JsonFieldType.NUMBER).description("도시 코드"),
					fieldWithPath("result.data.busClass").type(JsonFieldType.NUMBER).description("버스 노선 종류"),
					fieldWithPath("result.data.sourceName").type(JsonFieldType.STRING).description("출처명"),
					fieldWithPath("result.data.sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
					fieldWithPath("result.data.comment").type(JsonFieldType.STRING).description("설명"),
					fieldWithPath("result.data.totalRouteCount").type(JsonFieldType.NUMBER).description("총 노선수"),
					fieldWithPath("result.data.createDate").type(JsonFieldType.STRING).description("생성일"),
					fieldWithPath("result.data.updateDate").type(JsonFieldType.STRING).description("수정일"),
					
					fieldWithPath("result.data.generalCardFareRuleInfo").type(JsonFieldType.OBJECT).description("카드일반요금"),
					fieldWithPath("result.data.generalCardFareRuleInfo.fareId").type(JsonFieldType.NUMBER).description("요금 ID"),
					fieldWithPath("result.data.generalCardFareRuleInfo.baseFare").type(JsonFieldType.NUMBER).description("기본요금"),
					fieldWithPath("result.data.generalCardFareRuleInfo.baseDist").type(JsonFieldType.NUMBER).description("기본거리"),
					fieldWithPath("result.data.generalCardFareRuleInfo.unitFare").type(JsonFieldType.NUMBER).description("단위요금"),
					fieldWithPath("result.data.generalCardFareRuleInfo.unitDist").type(JsonFieldType.NUMBER).description("단위거리"),
					fieldWithPath("result.data.generalCardFareRuleInfo.maxFare").type(JsonFieldType.NUMBER).description("최대 요금"),
					fieldWithPath("result.data.generalCardFareRuleInfo.baseYn").type(JsonFieldType.STRING).description("요금 룰의 기본 정보 여부"),
					fieldWithPath("result.data.generalCardFareRuleInfo.ageId").type(JsonFieldType.NUMBER).description("연령별 타입"),
					fieldWithPath("result.data.generalCardFareRuleInfo.paymentId").type(JsonFieldType.NUMBER).description("지불 방식 식별"),
					
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
	public void selectExceptionalFareRuleList() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();

		FareRuleSearchVo fareRuleSearchVo = new FareRuleSearchVo();
		fareRuleSearchVo.setCityCode(1000);
		fareRuleSearchVo.setBusClass(11);
		
		List<ExceptionalFareRuleOutputVo> exceptionalFareRuleOutputVo = new ArrayList<ExceptionalFareRuleOutputVo>();
		ExceptionalFareRuleOutputVo exceptionalFareRuleListOutputVo1 = new ExceptionalFareRuleOutputVo();
		ExceptionalFareRuleOutputVo exceptionalFareRuleListOutputVo2 = new ExceptionalFareRuleOutputVo();
		
		exceptionalFareRuleListOutputVo1.setFareId(1);
		exceptionalFareRuleListOutputVo1.setOrder(1);
		exceptionalFareRuleListOutputVo1.setRouteNames("501, 502");
		exceptionalFareRuleListOutputVo1.setExceptionalFareRuleName();
		
		exceptionalFareRuleOutputVo.add(exceptionalFareRuleListOutputVo1);
		
		exceptionalFareRuleListOutputVo2.setFareId(2);
		exceptionalFareRuleListOutputVo2.setOrder(2);
		exceptionalFareRuleListOutputVo2.setRouteNames("601, 602");
		exceptionalFareRuleListOutputVo2.setExceptionalFareRuleName();
		
		exceptionalFareRuleOutputVo.add(exceptionalFareRuleListOutputVo2);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(exceptionalFareRuleOutputVo);

		//given
		given(fareRuleService.selectExceptionalFareRuleList(any(FareRuleSearchVo.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/list/exceptionalFareRule")
				.param("cityCode", "1000")
				.param("busClass", "11")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("fareRule/selectExceptionalFareRuleList",
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
					fieldWithPath("result.data[].exceptionalFareRuleName").type(JsonFieldType.STRING).description("예외 요금 룰 명"))));

	}

	/**
	 * 예외 요금 룰 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoExceptionalFareRule() throws Exception {

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

		FareRuleInfoVo fareRuleInfoVo = new FareRuleInfoVo();
		
		fareRuleInfoVo.setFareId(1);
		fareRuleInfoVo.setBaseFare(1200);
		fareRuleInfoVo.setBaseDist(10);
		fareRuleInfoVo.setUnitFare(100);
		fareRuleInfoVo.setUnitDist(5);
		fareRuleInfoVo.setMaxFare(2300);
		fareRuleInfoVo.setBaseYn("Y");
		fareRuleInfoVo.setAgeId(1);
		fareRuleInfoVo.setPaymentId(1);
		
		
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
		

		FareRuleOutputVoWithRouteList fareRuleOutputVoWithRouteList = new FareRuleOutputVoWithRouteList();

		fareRuleOutputVoWithRouteList.setFareId(1);
		fareRuleOutputVoWithRouteList.setStartStopId(111);
		fareRuleOutputVoWithRouteList.setEndStopId(222);
		fareRuleOutputVoWithRouteList.setStartStopName("북부수도사업소");
		fareRuleOutputVoWithRouteList.setEndStopName("한성운수종점");
		fareRuleOutputVoWithRouteList.setServiceId(5);
		fareRuleOutputVoWithRouteList.setCityCode(1000);
		fareRuleOutputVoWithRouteList.setBusClass(11);
		fareRuleOutputVoWithRouteList.setSourceName("출처");
		fareRuleOutputVoWithRouteList.setSourceUrl("arointech.co.kr");
		fareRuleOutputVoWithRouteList.setComment("설명");
		fareRuleOutputVoWithRouteList.setExceptionalFareRuleName("예외 7");
		fareRuleOutputVoWithRouteList.setRouteNames("종로09, 경복궁1");
		fareRuleOutputVoWithRouteList.setTotalRouteCount(3);
		fareRuleOutputVoWithRouteList.setCreateDate("2020.04.17");
		fareRuleOutputVoWithRouteList.setUpdateDate("2020.04.17");
		fareRuleOutputVoWithRouteList.setSourceName("출처");
		fareRuleOutputVoWithRouteList.setGeneralCardFareRuleInfo(fareRuleInfoVo);
		fareRuleOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareRuleOutputVoWithRouteList);

		//given
		given(fareRuleService.getExceptionalFareRule(any(FareRuleSearchVo.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/info/exceptionalFareRule/{fareId}", 2)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("fareRule/infoExceptionalFareRule",
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
					fieldWithPath("result.data.startStopId").type(JsonFieldType.NUMBER).description("시작 정류장 ID"),
					fieldWithPath("result.data.endStopId").type(JsonFieldType.NUMBER).description("도착 정류장 ID"),
					fieldWithPath("result.data.startStopName").type(JsonFieldType.STRING).description("시작 정류장 명 "),
					fieldWithPath("result.data.endStopName").type(JsonFieldType.STRING).description("도착 정류장 명"),
					fieldWithPath("result.data.serviceId").type(JsonFieldType.NUMBER).description("운행 요일 ID"),
					fieldWithPath("result.data.cityCode").type(JsonFieldType.NUMBER).description("도시 코드"),
					fieldWithPath("result.data.busClass").type(JsonFieldType.NUMBER).description("버스 노선 종류"),
					fieldWithPath("result.data.sourceName").type(JsonFieldType.STRING).description("출처명"),
					fieldWithPath("result.data.sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
					fieldWithPath("result.data.comment").type(JsonFieldType.STRING).description("설명"),
					fieldWithPath("result.data.exceptionalFareRuleName").type(JsonFieldType.STRING).description("예외 요금 룰 이름"),
					fieldWithPath("result.data.routeNames").type(JsonFieldType.STRING).description("예외 노선 이름들 (최대 5개)"),
					fieldWithPath("result.data.totalRouteCount").type(JsonFieldType.NUMBER).description("총 노선수"),
					fieldWithPath("result.data.createDate").type(JsonFieldType.STRING).description("생성일"),
					fieldWithPath("result.data.updateDate").type(JsonFieldType.STRING).description("수정일"),
					
					fieldWithPath("result.data.generalCardFareRuleInfo").type(JsonFieldType.OBJECT).description("카드일반요금"),
					fieldWithPath("result.data.generalCardFareRuleInfo.fareId").type(JsonFieldType.NUMBER).description("요금 ID"),
					fieldWithPath("result.data.generalCardFareRuleInfo.baseFare").type(JsonFieldType.NUMBER).description("기본요금"),
					fieldWithPath("result.data.generalCardFareRuleInfo.baseDist").type(JsonFieldType.NUMBER).description("기본거리"),
					fieldWithPath("result.data.generalCardFareRuleInfo.unitFare").type(JsonFieldType.NUMBER).description("단위요금"),
					fieldWithPath("result.data.generalCardFareRuleInfo.unitDist").type(JsonFieldType.NUMBER).description("단위거리"),
					fieldWithPath("result.data.generalCardFareRuleInfo.maxFare").type(JsonFieldType.NUMBER).description("최대 요금"),
					fieldWithPath("result.data.generalCardFareRuleInfo.baseYn").type(JsonFieldType.STRING).description("요금 룰의 기본 정보 여부"),
					fieldWithPath("result.data.generalCardFareRuleInfo.ageId").type(JsonFieldType.NUMBER).description("연령별 타입"),
					fieldWithPath("result.data.generalCardFareRuleInfo.paymentId").type(JsonFieldType.NUMBER).description("지불 방식 식별"),
					
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
	 * 요금 룰 작업 요약 목록
	 * @throws Exception
	 */
	@Test
	public void listFareRuleTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		TaskSummaryOutputVo taskSummaryOuputVo1 = new TaskSummaryOutputVo();
		taskSummaryOuputVo1.setTaskId(1);
		taskSummaryOuputVo1.setTaskType(TaskType.REGISTER);
		taskSummaryOuputVo1.setTaskStatusType(TaskStatusType.PROGRESS);
		taskSummaryOuputVo1.setPubTransType(PubTransType.FARE_RULE);
		taskSummaryOuputVo1.setTaskComment("신규 요금 등록");
		taskSummaryOuputVo1.setRegDate("2020-04-10");
		taskSummaryOuputVo1.setWorkUserName("test");

		TaskSummaryOutputVo taskSummaryOuputVo2 = new TaskSummaryOutputVo();
		taskSummaryOuputVo2.setTaskId(2);
		taskSummaryOuputVo2.setTaskType(TaskType.MODIFY);
		taskSummaryOuputVo2.setTaskStatusType(TaskStatusType.WAIT);
		taskSummaryOuputVo2.setPubTransType(PubTransType.FARE_RULE);
		taskSummaryOuputVo2.setTaskComment("서울 간선 요금 변경");
		taskSummaryOuputVo2.setRegDate("2020-04-10");
		taskSummaryOuputVo2.setWorkUserName("test");


		List<TaskSummaryOutputVo> taskSummaryOutputVoList = new ArrayList<>() ;
		taskSummaryOutputVoList.add(taskSummaryOuputVo1) ;
		taskSummaryOutputVoList.add(taskSummaryOuputVo2) ;

		// 페이징 정보
		PagingVo pagingVo = new PagingVo(2, 1, 20) ;

		CommonResult commonResult = outputFmtUtil.setCommonListFmt(pagingVo, taskSummaryOutputVoList);

		//given
		given(fareRuleService.getFareRuleTaskSummaryList(ArgumentMatchers.anyInt(), any()))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/fareRuleTask/summary/{fareId}", 1)
	                .param("pageNo", "1")
	            	.param("listSize", "20")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("fareRule/fareRuleTaskList",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            pathParameters(
                        parameterWithName("fareId").description("요금 룰 ID")
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
	             		fieldWithPath("result.data[].taskType").type(JsonFieldType.STRING).description(CodeType.TASK_TYPE.getCodeDescription() + TaskType.getCodeAndDescriptionWithColon()),
	             		fieldWithPath("result.data[].taskStatusType").type(JsonFieldType.STRING).description(CodeType.TASK_STATUS_TYPE.getCodeDescription() + TaskStatusType.getCodeAndDescriptionWithColon()),
	             		fieldWithPath("result.data[].pubTransType").type(JsonFieldType.STRING).description(CodeType.PUB_TRANS_TYPE.getCodeDescription() + PubTransType.getCodeAndDescriptionWithColon()),
	             		fieldWithPath("result.data[].taskComment").type(JsonFieldType.STRING).description("작업내용"),
	             		fieldWithPath("result.data[].regDate").type(JsonFieldType.STRING).description("등록일"),
	             		fieldWithPath("result.data[].workUserName").type(JsonFieldType.STRING).description("작업자")


	             )
 		));

	}


	/**
	 * 요금 룰 상세정보 작업정보 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoFareRuleTask() throws Exception {

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

		FareRuleInfoVo fareRuleInfoVo = new FareRuleInfoVo();
		
		fareRuleInfoVo.setFareId(1);
		fareRuleInfoVo.setBaseFare(1200);
		fareRuleInfoVo.setBaseDist(10);
		fareRuleInfoVo.setUnitFare(100);
		fareRuleInfoVo.setUnitDist(5);
		fareRuleInfoVo.setMaxFare(2300);
		fareRuleInfoVo.setBaseYn("Y");
		fareRuleInfoVo.setAgeId(1);
		fareRuleInfoVo.setPaymentId(1);
		
		
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
		

		FareRuleTaskOutputVoWithRouteList fareRuleTaskOutputVoWithRouteList = new FareRuleTaskOutputVoWithRouteList();

		fareRuleTaskOutputVoWithRouteList.setFareId(1);
		fareRuleTaskOutputVoWithRouteList.setStartStopId(55000452);
		fareRuleTaskOutputVoWithRouteList.setEndStopId(55000758);
		fareRuleTaskOutputVoWithRouteList.setStartStopName("북부수도사업소");
		fareRuleTaskOutputVoWithRouteList.setEndStopName("한성운수종점");
		fareRuleTaskOutputVoWithRouteList.setServiceId(5);
		fareRuleTaskOutputVoWithRouteList.setCityCode(1000);
		fareRuleTaskOutputVoWithRouteList.setBusClass(11);
		fareRuleTaskOutputVoWithRouteList.setSourceName("출처");
		fareRuleTaskOutputVoWithRouteList.setSourceUrl("arointech.co.kr");
		fareRuleTaskOutputVoWithRouteList.setComment("설명");
		fareRuleTaskOutputVoWithRouteList.setTotalRouteCount(3);
		fareRuleTaskOutputVoWithRouteList.setCreateDate("2020.04.17");
		fareRuleTaskOutputVoWithRouteList.setUpdateDate("2020.04.17");
		fareRuleTaskOutputVoWithRouteList.setSourceName("출처");
		fareRuleTaskOutputVoWithRouteList.setGeneralCardFareRuleInfo(fareRuleInfoVo);
		fareRuleTaskOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);


		TaskOutputVo taskOutputVo = new TaskOutputVo() ;
		taskOutputVo.setTaskId(1);
		taskOutputVo.setProviderId(4);
		taskOutputVo.setProviderName("서울");
		taskOutputVo.setTaskType(TaskType.REGISTER);
		taskOutputVo.setTaskStatusType(TaskStatusType.PROGRESS);
		taskOutputVo.setPubTransId(1);
		taskOutputVo.setPubTransType(PubTransType.COMPANY);
		taskOutputVo.setPubTransName("서울운수");
		taskOutputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);
		taskOutputVo.setTaskCheckRequestType(TaskCheckRequestType.values()[0]);
		taskOutputVo.setTaskComment("주소 변경");
		taskOutputVo.setAutoRegisterYn("N");
		taskOutputVo.setRegUserName("test_name");
		taskOutputVo.setRegUserId("test");
		taskOutputVo.setRegDate("2020-04-10 10:00:00");
		taskOutputVo.setWorkUserName("test_name");
		taskOutputVo.setWorkUserId("test");
		taskOutputVo.setWorkAssignDate("2020-04-10 10:00:00");
		taskOutputVo.setWorkCompleteDate("2020-04-10 10:00:00");

		fareRuleTaskOutputVoWithRouteList.setTaskInfo(taskOutputVo);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareRuleTaskOutputVoWithRouteList);


		//given
		given(fareRuleService.getFareRuleTaskInfo(1))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/info/fareRuleTask/{taskId}", 1L)
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("fareRule/fareRuleTask",
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
						fieldWithPath("result.data.startStopName").type(JsonFieldType.STRING).description("시작 정류장 명 "),
						fieldWithPath("result.data.endStopName").type(JsonFieldType.STRING).description("도착 정류장 명"),
						fieldWithPath("result.data.serviceId").type(JsonFieldType.NUMBER).description("운행 요일 ID"),
						fieldWithPath("result.data.cityCode").type(JsonFieldType.NUMBER).description("도시 코드"),
						fieldWithPath("result.data.busClass").type(JsonFieldType.NUMBER).description("버스 노선 종류"),
						fieldWithPath("result.data.sourceName").type(JsonFieldType.STRING).description("출처명"),
						fieldWithPath("result.data.sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
						fieldWithPath("result.data.comment").type(JsonFieldType.STRING).description("설명"),
						fieldWithPath("result.data.totalRouteCount").type(JsonFieldType.NUMBER).description("총 노선수"),
						fieldWithPath("result.data.createDate").type(JsonFieldType.STRING).description("생성일"),
						fieldWithPath("result.data.updateDate").type(JsonFieldType.STRING).description("수정일"),
						
						fieldWithPath("result.data.generalCardFareRuleInfo").type(JsonFieldType.OBJECT).description("카드일반요금"),
						fieldWithPath("result.data.generalCardFareRuleInfo.fareId").type(JsonFieldType.NUMBER).description("요금 ID"),
						fieldWithPath("result.data.generalCardFareRuleInfo.baseFare").type(JsonFieldType.NUMBER).description("기본요금"),
						fieldWithPath("result.data.generalCardFareRuleInfo.baseDist").type(JsonFieldType.NUMBER).description("기본거리"),
						fieldWithPath("result.data.generalCardFareRuleInfo.unitFare").type(JsonFieldType.NUMBER).description("단위요금"),
						fieldWithPath("result.data.generalCardFareRuleInfo.unitDist").type(JsonFieldType.NUMBER).description("단위거리"),
						fieldWithPath("result.data.generalCardFareRuleInfo.maxFare").type(JsonFieldType.NUMBER).description("최대 요금"),
						fieldWithPath("result.data.generalCardFareRuleInfo.baseYn").type(JsonFieldType.STRING).description("요금 룰의 기본 정보 여부"),
						fieldWithPath("result.data.generalCardFareRuleInfo.ageId").type(JsonFieldType.NUMBER).description("연령별 타입"),
						fieldWithPath("result.data.generalCardFareRuleInfo.paymentId").type(JsonFieldType.NUMBER).description("지불 방식 식별"),
						
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
	             		fieldWithPath("result.data.taskInfo.taskType").type(JsonFieldType.STRING).description(CodeType.TASK_TYPE.getCodeDescription() + TaskType.getCodeAndDescriptionWithColon()),
	             		fieldWithPath("result.data.taskInfo.taskStatusType").type(JsonFieldType.STRING).description(CodeType.TASK_STATUS_TYPE.getCodeDescription() + TaskStatusType.getCodeAndDescriptionWithColon()),
	             		fieldWithPath("result.data.taskInfo.pubTransType").type(JsonFieldType.STRING).description(CodeType.PUB_TRANS_TYPE.getCodeDescription() + PubTransType.getCodeAndDescriptionWithColon()),
	             		fieldWithPath("result.data.taskInfo.taskDataSourceType").type(JsonFieldType.NUMBER).description("데이터 출처"),
	                    fieldWithPath("result.data.taskInfo.taskCheckRequestType").type(JsonFieldType.STRING).description("검수요청 구분"),
	             		fieldWithPath("result.data.taskInfo.pubTransId").type(JsonFieldType.NUMBER).description("대중교통 ID"),
	             		fieldWithPath("result.data.taskInfo.pubTransName").type(STRING_OR_NULL).description("데이터 이름"),
	             		fieldWithPath("result.data.taskInfo.taskComment").type(STRING_OR_NULL).description("작업내용"),
	             		fieldWithPath("result.data.taskInfo.bisChangeDataInfo").type(OBJECT_OR_NULL).description("BIS 자동등록 변경내용 - Null이 아닌경우 변경된 데이터 컬럼 정보만 하위 요소로 표출"),
	             		fieldWithPath("result.data.taskInfo.autoRegisterYn").type(JsonFieldType.STRING).description("자동 등록여부(Y/N)"),
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
	public void schemaFareRule() throws Exception {

		List<CommonSchema> commonSchema = new ArrayList<>();

		//given
		given(fareRuleService.selectFareRuleSchemaAll())
			.willReturn(commonSchema);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/schema/fareRule")
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("fareRule/fareRuleSchema",
				getDocumentRequest(),
				getDocumentResponse(),

				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
					subsectionWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보").optional(),

					subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY)
						.description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"))));
	}
	
	
	/**
	 * 요금 룰 등록 Task
	 * @throws Exception
	 */
	@Test
	public void registerFareRuleAddTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;
		
		Integer[] routeIds = {11000006};
		Integer[] startStopIds = {55000691, 55000102};
		Integer[] endStopIds = {55000126, 55000669};

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

		List<Long> taskIdList = new ArrayList<Long>();
		taskIdList.add(1412L);
		taskIdList.add(1413L);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK_LIST, taskIdList);
		

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		//given
		given(fareRuleService.registerFareRuleTask(ArgumentMatchers.eq(TaskType.REGISTER), any()))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/fareRuleTask/addTask")
	                .content(objectMapper.writeValueAsString(fareRuleTaskInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("fareRule/fareRuleAddTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
                        fieldWithPath("baseYn").type(JsonFieldType.STRING).description("[필수]요금 룰의 기본 정보 여부 (Y, N)"),
                        fieldWithPath("cityCode").type(JsonFieldType.NUMBER).description("[필수]도시코드"),
                        fieldWithPath("busClass").type(JsonFieldType.NUMBER).description("[필수]노선 타입"),
                        fieldWithPath("baseFare").type(JsonFieldType.NUMBER).description("주소"),
                        fieldWithPath("baseDist").type(JsonFieldType.NUMBER).description("주소"),
                        fieldWithPath("unitFare").type(JsonFieldType.NUMBER).description("주소"),
                        fieldWithPath("unitDist").type(JsonFieldType.NUMBER).description("주소"),
                        fieldWithPath("maxFare").type(JsonFieldType.NUMBER).description("주소"),
                        fieldWithPath("sourceName").type(JsonFieldType.STRING).description("출처명"),
                        fieldWithPath("sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
                        fieldWithPath("comment").type(JsonFieldType.STRING).description("비고"),
                        fieldWithPath("routeIds").type(JsonFieldType.ARRAY).description("예외 요금 적용할 노선 목록"),
                        fieldWithPath("startStopIds").type(JsonFieldType.ARRAY).description("구간 시작 정류장 목록"),
                        fieldWithPath("endStopIds").type(JsonFieldType.ARRAY).description("구간 도착 정류장 목록"),
                        fieldWithPath("taskDataSourceType").type(JsonFieldType.NUMBER).description("[필수]데이터 출처"),
                        fieldWithPath("taskComment").type(JsonFieldType.STRING).description("[필수]변경내용"),
                        fieldWithPath("checkUserId").type(JsonFieldType.STRING).description("[필수]검수자ID")
                ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("상세 정보"),
	             		fieldWithPath("result.data.taskIdList").type(JsonFieldType.ARRAY).description("등록 성공한 작업 ID 목록")
	             )
 		));

	}


	/**
	 * 요금 룰 수정 Task
	 * @throws Exception
	 */
	@Test
	public void registerEditTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;
		
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


		// 성공시 작업ID 리턴
		List<Long> taskIdList = new ArrayList<Long>();
		taskIdList.add(1412L);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK_LIST, taskIdList);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		//given
		given(fareRuleService.registerFareRuleTask(ArgumentMatchers.eq(TaskType.MODIFY), any()))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/fareRuleTask/editTask")
	                .content(objectMapper.writeValueAsString(fareRuleTaskInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("fareRule/fareRuleEditTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
                    fieldWithPath("fareId").type(JsonFieldType.NUMBER).description("[필수]요금 룰 ID"),
                    fieldWithPath("baseYn").type(JsonFieldType.STRING).description("[필수]요금 룰의 기본 정보 여부 (Y, N)"),
                    fieldWithPath("cityCode").type(JsonFieldType.NUMBER).description("[필수]도시코드"),
                    fieldWithPath("busClass").type(JsonFieldType.NUMBER).description("[필수]노선 타입"),
                    fieldWithPath("baseFare").type(JsonFieldType.NUMBER).description("기본요금"),
                    fieldWithPath("baseDist").type(JsonFieldType.NUMBER).description("기본거리"),
                    fieldWithPath("unitFare").type(JsonFieldType.NUMBER).description("단위요금"),
                    fieldWithPath("unitDist").type(JsonFieldType.NUMBER).description("단위거리"),
                    fieldWithPath("maxFare").type(JsonFieldType.NUMBER).description("최대요금"),
                    fieldWithPath("sourceName").type(JsonFieldType.STRING).description("출처명"),
                    fieldWithPath("sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
                    fieldWithPath("comment").type(JsonFieldType.STRING).description("비고"),
                    fieldWithPath("routeIds").type(JsonFieldType.ARRAY).description("예외 요금 적용할 노선 목록"),
                    fieldWithPath("startStopIds").type(JsonFieldType.ARRAY).description("구간 시작 정류장 목록"),
                    fieldWithPath("endStopIds").type(JsonFieldType.ARRAY).description("구간 도착 정류장 목록"),
                    fieldWithPath("taskDataSourceType").type(JsonFieldType.NUMBER).description("[필수]데이터 출처"),
                    fieldWithPath("taskComment").type(JsonFieldType.STRING).description("[필수]변경내용"),
                    fieldWithPath("checkUserId").type(JsonFieldType.STRING).description("[필수]검수자ID")
	            ),
	            responseFields(
            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("상세 정보"),
             		fieldWithPath("result.data.taskIdList").type(JsonFieldType.ARRAY).description("등록 성공한 작업 ID 목록")
	            )
 		));

	}

	/**
	 * 요금 룰 Task 수정
	 * @throws Exception
	 */
	@Test
	public void modifyFareRuleTask() throws Exception {

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;
		
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

		//when
		ResultActions result = this.mockMvc.perform(
                put("/v1/ntool/api/modify/fareRuleTask")
	                .content(objectMapper.writeValueAsString(fareRuleTaskInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("fareRule/fareRuleModifyTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
	            		fieldWithPath("taskId").type(JsonFieldType.NUMBER).description("[필수]작업ID"),
	                    fieldWithPath("fareId").type(JsonFieldType.NUMBER).description("[필수]요금 룰 ID"),
	                    fieldWithPath("baseYn").type(JsonFieldType.STRING).description("[필수]요금 룰의 기본 정보 여부 (Y, N)"),
	                    fieldWithPath("cityCode").type(JsonFieldType.NUMBER).description("[필수]도시코드"),
	                    fieldWithPath("busClass").type(JsonFieldType.NUMBER).description("[필수]노선 타입"),
	                    fieldWithPath("baseFare").type(JsonFieldType.NUMBER).description("기본요금"),
	                    fieldWithPath("baseDist").type(JsonFieldType.NUMBER).description("기본거리"),
	                    fieldWithPath("unitFare").type(JsonFieldType.NUMBER).description("단위요금"),
	                    fieldWithPath("unitDist").type(JsonFieldType.NUMBER).description("단위거리"),
	                    fieldWithPath("maxFare").type(JsonFieldType.NUMBER).description("최대요금"),
	                    fieldWithPath("sourceName").type(JsonFieldType.STRING).description("출처명"),
	                    fieldWithPath("sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
	                    fieldWithPath("comment").type(JsonFieldType.STRING).description("비고"),
	                    fieldWithPath("routeIds").type(JsonFieldType.ARRAY).description("예외 요금 적용할 노선 목록"),
	                    fieldWithPath("startStopIds").type(JsonFieldType.ARRAY).description("구간 시작 정류장 목록"),
	                    fieldWithPath("endStopIds").type(JsonFieldType.ARRAY).description("구간 도착 정류장 목록"),
	                    fieldWithPath("taskDataSourceType").type(JsonFieldType.NUMBER).description("[필수]데이터 출처"),
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
	 * 요금 룰 삭제요청 Task 등록
	 * @throws Exception
	 */
	@Test
	public void fareRemoveTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		FareRuleRemoveTaskInputVo fareRuleRemoveTaskInputVo = new FareRuleRemoveTaskInputVo() ;
		fareRuleRemoveTaskInputVo.setFareId(3);
		fareRuleRemoveTaskInputVo.setTaskComment("미존재 요금 룰 - 삭제 처리");
		fareRuleRemoveTaskInputVo.setCheckUserId("test");
		fareRuleRemoveTaskInputVo.setTaskDataSourceType(TaskDataSourceType.values()[0]);

		// 성공시 작업ID 리턴
		List<Long> taskIdList = new ArrayList<Long>();
		taskIdList.add(1412L);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK_LIST, taskIdList);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		//given
		given(fareRuleService.registerFareRuleRemoveTask(any()))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/fareRuleTask/removeTask")
	                .content(objectMapper.writeValueAsString(fareRuleRemoveTaskInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("fareRule/fareRuleRemoveTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
	            		fieldWithPath("fareId").type(JsonFieldType.NUMBER).description("[필수]요금 룰 ID"),
	            		fieldWithPath("taskDataSourceType").type(JsonFieldType.NUMBER).description("[필수]데이터 출처"),
                        fieldWithPath("taskComment").type(JsonFieldType.STRING).description("[필수]변경내용"),
                        fieldWithPath("checkUserId").type(JsonFieldType.STRING).description("[필수]검수자ID")
                ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("상세 정보"),
	             		fieldWithPath("result.data.taskIdList").type(JsonFieldType.ARRAY).description("등록 성공한 작업 ID 목록")
	             )
 		));

	}

}
