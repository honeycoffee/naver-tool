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
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.controller.BusCompanyController;
import com.naver.pubtrans.itn.api.handler.MemberAccessDeniedHandler;
import com.naver.pubtrans.itn.api.service.BusCompanyService;
import com.naver.pubtrans.itn.api.vo.bus.company.BusCompanyVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanyRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanySearchVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanyTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.company.output.BusCompanyListOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.company.output.BusCompanyTaskOutputVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskSummaryOutputVo;

@RunWith(SpringRunner.class)
@WebMvcTest(BusCompanyController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class BusCompanyControllerDocumentTest {

	private static final List<JsonFieldType> STRING_OR_NULL = Arrays.asList(JsonFieldType.STRING, JsonFieldType.NULL) ;
	private static final List<JsonFieldType> NUMBER_OR_NULL = Arrays.asList(JsonFieldType.NUMBER, JsonFieldType.NULL) ;
	private static final List<JsonFieldType> OBJECT_OR_NULL = Arrays.asList(JsonFieldType.OBJECT, JsonFieldType.NULL) ;


	@Autowired
	MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@MockBean
	private BusCompanyService busCompanyService ; 
	

	@MockBean
	private OutputFmtUtil outputFmtUtil ;

	@MockBean
	private JwtAdapter jwtAdapter;

	@MockBean
	private MemberAccessDeniedHandler memberAccessDeniedHandler;



	/**
	 * 버스 운수사 목록 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void listBusCompany() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;



		// 리스트 페이징 정보
		PagingVo pagingVo = new PagingVo(100, 1, 20) ;



		// 스키마
		SchemaVo schemaVo = new SchemaVo();
		schemaVo.setColumnName("company_id");
		schemaVo.setColumnComment("운수사 ID");
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
		BusCompanyListOutputVo busCompanyListOutputVo = new BusCompanyListOutputVo();
		busCompanyListOutputVo.setCompanyId(1);
		busCompanyListOutputVo.setCompanyName("서울운수");
		busCompanyListOutputVo.setTel("02-352-0491");
		busCompanyListOutputVo.setCityCode(1000);
		busCompanyListOutputVo.setCityName("서울");


		List<BusCompanyListOutputVo> busCompanyListOutputVoList = new ArrayList<>();
		busCompanyListOutputVoList.add(busCompanyListOutputVo) ;




		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, busCompanyListOutputVoList) ;


		//given
		given(busCompanyService.getBusCompanyList(any(BusCompanySearchVo.class)))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/busCompany")
                	.param("companyName", "")
                	.param("cityCode", "1000")
                	.param("pageNo", "1")
                	.param("listSize", "20")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busCompany/busCompanyList",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("companyName").description("[선택]운수사명").optional(),
	            		parameterWithName("companyId").description("[선택]운수사ID").optional(),
	            		parameterWithName("cityCode").description("[선택]도시코드").optional(),
	            		parameterWithName("tel").description("[선택]전화번호").optional(),
	            		parameterWithName("pageNo").description("[선택]페이지 번호(기본:1)").optional(),
	            		parameterWithName("listSize").description("[선택]페이지당 목록 수(기본:20)").optional()
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.meta").type(JsonFieldType.OBJECT).description("페이징 정보 - link:#_데이터_목록_페이징_정보[공통사항 참고]"),
	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"),

	             		fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("운수사 목록"),
	             		fieldWithPath("result.data[].companyId").type(JsonFieldType.NUMBER).description("운수사ID"),
	             		fieldWithPath("result.data[].companyName").type(JsonFieldType.STRING).description("운수사명"),
	             		fieldWithPath("result.data[].tel").type(JsonFieldType.STRING).description("전화번호"),
	             		fieldWithPath("result.data[].cityCode").type(JsonFieldType.NUMBER).description("도시코드"),
	             		fieldWithPath("result.data[].cityName").type(JsonFieldType.STRING).description("도시코드명")
	             )
 		));

	}


	/**
	 * 버스 운수사 상세정보 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoBusCompany() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		BusCompanyVo busCompanyVo = new BusCompanyVo() ;
		busCompanyVo.setCompanyId(1);
		busCompanyVo.setCompanyName("서울운수");
		busCompanyVo.setCityCode(1000);
		busCompanyVo.setTel("02-352-0491");
		busCompanyVo.setAddressName("서울시 영등포구 당산로 41길 11 ");
		busCompanyVo.setSourceName("아로정보기술");
		busCompanyVo.setSourceUrl("http://www.arointech.co.kr");
		busCompanyVo.setComment("비고");
		
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(busCompanyVo) ;

		//given
		given(busCompanyService.getBusCompanyInfo(1))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/info/busCompany/{companyId}", 1)
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busCompany/busCompanyInfo",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            pathParameters(
                        parameterWithName("companyId").description("운수사 ID")
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]").optional(),

	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("운수사 정보"),
	             		fieldWithPath("result.data.companyId").type(JsonFieldType.NUMBER).description("운수사ID"),
	             		fieldWithPath("result.data.companyName").type(JsonFieldType.STRING).description("운수사명"),
	             		fieldWithPath("result.data.cityCode").type(JsonFieldType.NUMBER).description("도시코드"),
	             		fieldWithPath("result.data.tel").type(JsonFieldType.STRING).description("전화번호"),
	             		fieldWithPath("result.data.addressName").type(JsonFieldType.STRING).description("주소"),
	             		fieldWithPath("result.data.sourceName").type(JsonFieldType.STRING).description("출처명"),
	             		fieldWithPath("result.data.sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
	             		fieldWithPath("result.data.comment").type(JsonFieldType.STRING).description("비고")

	             )
 		));

	}


	/**
	 * 버스 운수사 상세정보 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoBusCompanyTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		BusCompanyTaskOutputVo busCompanyTaskOutputVo = new BusCompanyTaskOutputVo() ;
		busCompanyTaskOutputVo.setCompanyId(1);
		busCompanyTaskOutputVo.setCompanyName("서울운수");
		busCompanyTaskOutputVo.setCityCode(1000);
		busCompanyTaskOutputVo.setTel("02-352-0491");
		busCompanyTaskOutputVo.setAddressName("서울시 영등포구 당산로 41길 11 ");
		busCompanyTaskOutputVo.setSourceName("아로정보기술");
		busCompanyTaskOutputVo.setSourceUrl("http://www.arointech.co.kr");
		busCompanyTaskOutputVo.setComment("비고");



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

		busCompanyTaskOutputVo.setTaskInfo(taskOutputVo);


		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(busCompanyTaskOutputVo) ;


		//given
		given(busCompanyService.getBusCompanyTaskInfo(1))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/info/busCompanyTask/{taskId}", 1L)
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busCompany/busCompanyTask",
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

	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("운수사 정보"),
	             		fieldWithPath("result.data.companyId").type(JsonFieldType.NUMBER).description("운수사ID"),
	             		fieldWithPath("result.data.companyName").type(JsonFieldType.STRING).description("운수사명"),
	             		fieldWithPath("result.data.cityCode").type(JsonFieldType.NUMBER).description("도시코드"),
	             		fieldWithPath("result.data.tel").type(JsonFieldType.STRING).description("전화번호"),
	             		fieldWithPath("result.data.addressName").type(JsonFieldType.STRING).description("주소"),
	             		fieldWithPath("result.data.sourceName").type(JsonFieldType.STRING).description("출처명"),
	             		fieldWithPath("result.data.sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
	             		fieldWithPath("result.data.comment").type(JsonFieldType.STRING).description("비고"),

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
	 * 버스 운수사 작업 요약 목록
	 * @throws Exception
	 */
	@Test
	public void listBusCompanyTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;



		TaskSummaryOutputVo taskSummaryOuputVo1 = new TaskSummaryOutputVo();
		taskSummaryOuputVo1.setTaskId(1);
		taskSummaryOuputVo1.setTaskType("register");
		taskSummaryOuputVo1.setTaskStatus("01");
		taskSummaryOuputVo1.setTaskDataType("company");
		taskSummaryOuputVo1.setTaskComment("신규 운수사 등록");
		taskSummaryOuputVo1.setRegDate("2020-04-10");
		taskSummaryOuputVo1.setWorkUserName("test");

		TaskSummaryOutputVo taskSummaryOuputVo2 = new TaskSummaryOutputVo();
		taskSummaryOuputVo2.setTaskId(2);
		taskSummaryOuputVo2.setTaskType("modify");
		taskSummaryOuputVo2.setTaskStatus("00");
		taskSummaryOuputVo2.setTaskDataType("company");
		taskSummaryOuputVo2.setTaskComment("전화번호 변경");
		taskSummaryOuputVo2.setRegDate("2020-04-10");
		taskSummaryOuputVo2.setWorkUserName("test");


		List<TaskSummaryOutputVo> taskSummaryOutputVoList = new ArrayList<>() ;
		taskSummaryOutputVoList.add(taskSummaryOuputVo1) ;
		taskSummaryOutputVoList.add(taskSummaryOuputVo2) ;

		// 페이징 정보
		PagingVo pagingVo = new PagingVo(2, 1, 20) ;

		CommonResult commonResult = outputFmtUtil.setCommonListFmt(pagingVo, taskSummaryOutputVoList);

		//given
		given(busCompanyService.getBusCompanyTaskSummaryList(ArgumentMatchers.anyInt(), any()))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/busCompanyTask/summary/{companyId}", 1)
	                .param("pageNo", "1")
	            	.param("listSize", "20")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busCompany/busCompanyTaskList",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            pathParameters(
                        parameterWithName("companyId").description("운수사 ID")
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
	 * 버스 운수사 기본 스키마를 조회한다
	 * @throws Exception
	 */
	@Test
	public void schemaBusCompany() throws Exception {


		List<CommonSchema> commonSchema = new ArrayList<>() ;

		//given
		given(busCompanyService.selectBusCompanySchemaAll())
				.willReturn(commonSchema) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/schema/busCompany")
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busCompany/busCompanySchema",
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
	 * 운수사 등록 Task
	 * @throws Exception
	 */
	@Test
	public void registerBusCompanyAddTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;

		BusCompanyTaskInputVo busCompanyTaskInputVo = new BusCompanyTaskInputVo() ;
		busCompanyTaskInputVo.setCompanyName("서울운수");
		busCompanyTaskInputVo.setTel("010-1234-5678");
		busCompanyTaskInputVo.setAddressName("서울시 영등포구 당산로41길 11");
		busCompanyTaskInputVo.setCityCode(1000);
		busCompanyTaskInputVo.setSourceName("네이버");
		busCompanyTaskInputVo.setSourceUrl("www.naver.com");
		busCompanyTaskInputVo.setComment("테스트 등록");
		busCompanyTaskInputVo.setTaskComment("운수사 등록");
		busCompanyTaskInputVo.setCheckUserId("test");


		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, 950);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		//given
		given(busCompanyService.registerBusCompanyTask(ArgumentMatchers.anyString(), any()))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/busCompanyTask/addTask")
	                .content(objectMapper.writeValueAsString(busCompanyTaskInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );
		

		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busCompany/busCompanyAddTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
                        fieldWithPath("companyName").type(JsonFieldType.STRING).description("[필수]운수사 명칭"),
                        fieldWithPath("cityCode").type(JsonFieldType.NUMBER).description("[필수]도시코드"),
                        fieldWithPath("tel").type(JsonFieldType.STRING).description("전화번호"),
                        fieldWithPath("addressName").type(JsonFieldType.STRING).description("주소"),
                        fieldWithPath("sourceName").type(JsonFieldType.STRING).description("출처명"),
                        fieldWithPath("sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
                        fieldWithPath("comment").type(JsonFieldType.STRING).description("비고"),
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
	 * 운수사 수정 Task
	 * @throws Exception
	 */
	@Test
	public void registerBusCompanyEditTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;

		BusCompanyTaskInputVo busCompanyTaskInputVo = new BusCompanyTaskInputVo() ;
		busCompanyTaskInputVo.setCompanyId(1);
		busCompanyTaskInputVo.setCompanyName("서울운수");
		busCompanyTaskInputVo.setTel("010-1234-5678");
		busCompanyTaskInputVo.setAddressName("서울시 영등포구 당산로41길 11");
		busCompanyTaskInputVo.setCityCode(1000);
		busCompanyTaskInputVo.setSourceName("네이버");
		busCompanyTaskInputVo.setSourceUrl("www.naver.com");
		busCompanyTaskInputVo.setComment("테스트 등록");
		busCompanyTaskInputVo.setTaskComment("운수사 수정");
		busCompanyTaskInputVo.setCheckUserId("test");


		// 성공시 작업ID 리턴
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, 950);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		//given
		given(busCompanyService.registerBusCompanyTask(ArgumentMatchers.anyString(), any()))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/busCompanyTask/editTask")
	                .content(objectMapper.writeValueAsString(busCompanyTaskInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busCompany/busCompanyEditTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
	            		fieldWithPath("companyId").type(JsonFieldType.NUMBER).description("[필수]운수사 ID"),
                        fieldWithPath("companyName").type(JsonFieldType.STRING).description("[필수]운수사 명칭"),
                        fieldWithPath("cityCode").type(JsonFieldType.NUMBER).description("[필수]도시코드"),
                        fieldWithPath("tel").type(JsonFieldType.STRING).description("전화번호"),
                        fieldWithPath("addressName").type(JsonFieldType.STRING).description("주소"),
                        fieldWithPath("sourceName").type(JsonFieldType.STRING).description("출처명"),
                        fieldWithPath("sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
                        fieldWithPath("comment").type(JsonFieldType.STRING).description("비고"),
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
	 * 운수사 Task 수정
	 * @throws Exception
	 */
	@Test
	public void modifyBusCompanyTask() throws Exception {

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT) ;

		BusCompanyTaskInputVo busCompanyTaskInputVo = new BusCompanyTaskInputVo() ;
		busCompanyTaskInputVo.setTaskId(1);
		busCompanyTaskInputVo.setCompanyId(1);
		busCompanyTaskInputVo.setCompanyName("서울운수");
		busCompanyTaskInputVo.setTel("010-1234-5678");
		busCompanyTaskInputVo.setAddressName("서울시 영등포구 당산로41길 11");
		busCompanyTaskInputVo.setCityCode(1000);
		busCompanyTaskInputVo.setSourceName("네이버");
		busCompanyTaskInputVo.setSourceUrl("www.naver.com");
		busCompanyTaskInputVo.setComment("테스트 등록");
		busCompanyTaskInputVo.setTaskComment("운수사 등록");
		busCompanyTaskInputVo.setCheckUserId("test");



		//when
		ResultActions result = this.mockMvc.perform(
                put("/v1/ntool/api/modify/busCompanyTask")
	                .content(objectMapper.writeValueAsString(busCompanyTaskInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busCompany/busCompanyModifyTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
	            		fieldWithPath("taskId").type(JsonFieldType.NUMBER).description("[필수]작업ID"),
	            		fieldWithPath("companyId").type(JsonFieldType.NUMBER).description("[필수]운수사 ID"),
                        fieldWithPath("companyName").type(JsonFieldType.STRING).description("[필수]운수사 명칭"),
                        fieldWithPath("cityCode").type(JsonFieldType.NUMBER).description("[필수]도시코드"),
                        fieldWithPath("tel").type(JsonFieldType.STRING).description("전화번호"),
                        fieldWithPath("addressName").type(JsonFieldType.STRING).description("주소"),
                        fieldWithPath("sourceName").type(JsonFieldType.STRING).description("출처명"),
                        fieldWithPath("sourceUrl").type(JsonFieldType.STRING).description("출처 URL"),
                        fieldWithPath("comment").type(JsonFieldType.STRING).description("비고"),
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
	 * 운수사 삭제요청 Task 등록
	 * @throws Exception
	 */
	@Test
	public void busCompanyRemoveTask() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		BusCompanyRemoveTaskInputVo busCompanyRemoveInputVo = new BusCompanyRemoveTaskInputVo() ;
		busCompanyRemoveInputVo.setCompanyId(3);
		busCompanyRemoveInputVo.setTaskComment("미존재 운수사 - 삭제 처리");
		busCompanyRemoveInputVo.setCheckUserId("test");

		// 성공시 작업ID 리턴
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, 950);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		//given
		given(busCompanyService.registerBusCompanyRemoveTask(any()))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/busCompanyTask/removeTask")
	                .content(objectMapper.writeValueAsString(busCompanyRemoveInputVo))
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busCompany/busCompanyRemoveTask",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestFields(
	            		fieldWithPath("companyId").type(JsonFieldType.NUMBER).description("[필수]운수사ID"),
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
