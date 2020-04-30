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
import com.naver.pubtrans.itn.api.consts.CodeType;
import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.controller.TaskController;
import com.naver.pubtrans.itn.api.handler.MemberAccessDeniedHandler;
import com.naver.pubtrans.itn.api.service.TaskService;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.task.input.TaskSearchVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskListOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskStatisticsOutputVo;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerDocumentTest {

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
	private TaskService taskService;

	@MockBean
	private OutputFmtUtil outputFmtUtil ;

	@MockBean
	private JwtAdapter jwtAdapter;

	@MockBean
	private MemberAccessDeniedHandler memberAccessDeniedHandler;

	/**
	 * 작업 목록 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void listTask() throws Exception {
		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		// 리스트 페이징 정보
		PagingVo pagingVo = new PagingVo(100, 1, 20) ;


		// 스키마
		SchemaVo schemaVo = new SchemaVo();
		schemaVo.setColumnName("task_id");
		schemaVo.setColumnComment("작업ID");
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



		TaskListOutputVo taskListOutputVo = new TaskListOutputVo();
		taskListOutputVo.setTaskId(1420);
		taskListOutputVo.setCityCode(1000);
		taskListOutputVo.setCityName("서울");
		taskListOutputVo.setProviderId(0);
		taskListOutputVo.setProviderName(null);
		taskListOutputVo.setTaskStatusType(TaskStatusType.CHECKING);
		taskListOutputVo.setTaskDataSourceType(TaskDataSourceType.ETC);
		taskListOutputVo.setPubTransId(0);
		taskListOutputVo.setPubTransType(PubTransType.STOP);
		taskListOutputVo.setPubTransName("한글 정류장");
		taskListOutputVo.setTaskComment("정류장 위치변경");
		taskListOutputVo.setRegUserName("test_name");
		taskListOutputVo.setRegDate("2020-04-22");
		taskListOutputVo.setWorkUserName("test_name");
		taskListOutputVo.setWorkAssignDate("2020-04-22");
		taskListOutputVo.setWorkCompleteDate("2020-04-22");
		taskListOutputVo.setCheckUserName("test_name");

		List<TaskListOutputVo> taskListOutputVoList = new ArrayList<>();
		taskListOutputVoList.add(taskListOutputVo);


		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, taskListOutputVoList) ;


		//given
		given(taskService.getTaskList(any(TaskSearchVo.class)))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/task")
                	.param("pubTransType", "stop")
	                .param("taskDataSourceType", "7")
	                .param("cityCode", "1000")
	            	.param("pageNo", "1")
	            	.param("listSize", "20")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("task/taskList",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("taskId").description("[선택]작업ID").optional(),
	            		parameterWithName("providerId").description("[선택]DCC").optional(),
	            		parameterWithName("taskStatusType").description("[선택]" + CodeType.TASK_STATUS_TYPE.getCodeDescription()).optional(),
	            		parameterWithName("pubTransId").description("[선택]대중교통 ID").optional(),
	            		parameterWithName("pubTransType").description("[선택]" + CodeType.PUB_TRANS_TYPE.getCodeDescription()).optional(),
	            		parameterWithName("taskDataSourceType").description("[선택]" + CodeType.TASK_DATA_SOURCE_TYPE.getCodeDescription()).optional(),
	            		parameterWithName("pubTransName").description("[선택]데이터 이름").optional(),
	            		parameterWithName("cityCode").description("[선택]도시코드").optional(),
	            		parameterWithName("regUserId").description("[선택]등록자").optional(),
	            		parameterWithName("startRegDate").description("[선택]등록 시작일(yyyy-MM-dd)").optional(),
	            		parameterWithName("endRegDate").description("[선택]등록 종료일(yyyy-MM-dd)").optional(),
	            		parameterWithName("workUserId").description("[선택]작업자").optional(),
	            		parameterWithName("startWorkDate").description("[선택]할당 시작일(yyyy-MM-dd)").optional(),
	            		parameterWithName("endWorkDate").description("[선택]할당 종료일(yyyy-MM-dd)").optional(),
	            		parameterWithName("checkUserId").description("[선택]검수자").optional(),
	            		parameterWithName("startCheckDate").description("[선택]검수완료 시작일(yyyy-MM-dd)").optional(),
	            		parameterWithName("endCheckDate").description("[선택]검수완료 종료일(yyyy-MM-dd)").optional(),

	            		parameterWithName("pageNo").description("[선택]페이지 번호(기본:1)").optional(),
	            		parameterWithName("listSize").description("[선택]페이지당 목록 수(기본:20)").optional(),
	            		parameterWithName("sort").description("[선택]정렬(기본:목록 첫번째 Key 내림차순) - 사용 예:taskId,asc").optional()
	            ),
	            responseFields(
	            		fieldWithPath("code").type(NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.meta").type(OBJECT).description("페이징 정보 - link:#_데이터_목록_페이징_정보[공통사항 참고]"),
	             		subsectionWithPath("result.schema[]").type(ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"),

	             		fieldWithPath("result.data[]").type(ARRAY).description("작업 목록"),
	             		fieldWithPath("result.data[].taskId").type(NUMBER).description("작업ID"),
	             		fieldWithPath("result.data[].cityCode").type(NUMBER).description("도시코드"),
	             		fieldWithPath("result.data[].cityName").type(STRING).description("도시코드명"),
	             		fieldWithPath("result.data[].providerId").type(NUMBER).description("DCC ID"),
	             		fieldWithPath("result.data[].providerName").type(STRING_OR_NULL).description("DCC 명칭"),
	             		fieldWithPath("result.data[].taskStatusTypeName").type(STRING).description("Task 진행상태명"),
	             		fieldWithPath("result.data[].taskDataSourceTypeName").type(STRING).description("작업 데이터 출처 구분명칭"),
	             		fieldWithPath("result.data[].pubTransId").type(NUMBER).description("데이터 ID"),
	             		fieldWithPath("result.data[].pubTransTypeName").type(STRING).description("대중교통 구분 명칭"),
	             		fieldWithPath("result.data[].pubTransName").type(STRING).description("데이터 명"),
	             		fieldWithPath("result.data[].taskComment").type(STRING_OR_NULL).description("작업정보"),
	             		fieldWithPath("result.data[].regUserName").type(STRING).description("등록자명"),
	             		fieldWithPath("result.data[].regDate").type(STRING).description("등록일"),
	             		fieldWithPath("result.data[].workUserName").type(STRING_OR_NULL).description("작업자명"),
	             		fieldWithPath("result.data[].workAssignDate").type(STRING_OR_NULL).description("작업 할당일"),
	             		fieldWithPath("result.data[].workCompleteDate").type(STRING_OR_NULL).description("작업 완료일"),
	             		fieldWithPath("result.data[].checkUserName").type(STRING_OR_NULL).description("검수자명"),
	             		fieldWithPath("result.data[].checkCompleteDate").type(STRING_OR_NULL).description("검수 완료일")
	             )
 		));

	}


	/**
	 * 작업 통계  rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void taskStatistics() throws Exception {
		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		TaskStatisticsOutputVo taskStatisticsOutputVo = new TaskStatisticsOutputVo();
		taskStatisticsOutputVo.setTotalCnt(1379);
		taskStatisticsOutputVo.setWaitCnt(0);
		taskStatisticsOutputVo.setProgressCnt(1156);
		taskStatisticsOutputVo.setCheckingCnt(223);
		taskStatisticsOutputVo.setCheckCompletionCnt(0);
		taskStatisticsOutputVo.setExceptionCompletionCnt(0);
		taskStatisticsOutputVo.setProgressCnt(0);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(taskStatisticsOutputVo) ;


		//given
		given(taskService.getTaskSummaryStatistics(any(TaskSearchVo.class)))
				.willReturn(commonResult) ;

		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/task/summaryStatistics")
                	.param("pubTransType", "stop")
	                .param("taskDataSourceType", "7")
	                .param("cityCode", "1000")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );

		//then
		result.andExpect(status().isOk())
	 		.andDo(document("task/taskSummaryStatistics",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("taskId").description("[선택]작업ID").optional(),
	            		parameterWithName("providerId").description("[선택]DCC").optional(),
	            		parameterWithName("taskStatusType").description("[선택]" + CodeType.TASK_STATUS_TYPE.getCodeDescription()).optional(),
	            		parameterWithName("pubTransId").description("[선택]데이터 ID").optional(),
	            		parameterWithName("pubTransType").description("[선택]" + CodeType.PUB_TRANS_TYPE.getCodeDescription()).optional(),
	            		parameterWithName("taskDataSourceType").description("[선택]" + CodeType.TASK_DATA_SOURCE_TYPE.getCodeDescription()).optional(),
	            		parameterWithName("pubTransName").description("[선택]데이터 이름").optional(),
	            		parameterWithName("cityCode").description("[선택]도시코드").optional(),
	            		parameterWithName("regUserId").description("[선택]등록자").optional(),
	            		parameterWithName("startRegDate").description("[선택]등록 시작일(yyyy-MM-dd)").optional(),
	            		parameterWithName("endRegDate").description("[선택]등록 종료일(yyyy-MM-dd)").optional(),
	            		parameterWithName("workUserId").description("[선택]작업자").optional(),
	            		parameterWithName("startWorkDate").description("[선택]할당 시작일(yyyy-MM-dd)").optional(),
	            		parameterWithName("endWorkDate").description("[선택]할당 종료일(yyyy-MM-dd)").optional(),
	            		parameterWithName("checkUserId").description("[선택]검수자").optional(),
	            		parameterWithName("startCheckDate").description("[선택]검수완료 시작일(yyyy-MM-dd)").optional(),
	            		parameterWithName("endCheckDate").description("[선택]검수완료 종료일(yyyy-MM-dd)").optional()
	            ),
	            responseFields(
	            		fieldWithPath("code").type(NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(OBJECT).description("결과 정보"),
	             		fieldWithPath("result.data.totalCnt").type(NUMBER).description("전체 작업수"),
	             		fieldWithPath("result.data.waitCnt").type(NUMBER).description("대기중인 작업수"),
	             		fieldWithPath("result.data.progressCnt").type(NUMBER).description("진행중인 작업수"),
	             		fieldWithPath("result.data.checkingCnt").type(NUMBER).description("검수중인 작업수"),
	             		fieldWithPath("result.data.checkCompletionCnt").type(NUMBER).description("검수완료된 작업수"),
	             		fieldWithPath("result.data.exceptionCompletionCnt").type(NUMBER).description("예외완료된 작업수"),
	             		fieldWithPath("result.data.progressRate").type(NUMBER).description("진행률(%)")
	             )
 		));

	}

	/**
	 * 작업 상세정보 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoTask() throws Exception {
		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;


		// 스키마
		SchemaVo schemaVo = new SchemaVo();
		schemaVo.setColumnName("task_id");
		schemaVo.setColumnComment("작업ID");
		schemaVo.setIsNullable("NO");
		schemaVo.setColumnKey("PRI");
		schemaVo.setColumnType("int(11)");


		SchemaVo schemaCityVo = new SchemaVo();
		schemaCityVo.setColumnName("provider_id");
		schemaCityVo.setColumnComment("DCC");
		schemaCityVo.setIsNullable("NO");
		schemaCityVo.setColumnKey("");
		schemaCityVo.setColumnType("int(11)");

		List<SchemaVo> schemaVoList = new ArrayList<>();
		schemaVoList.add(schemaVo) ;
		schemaVoList.add(schemaCityVo) ;

		// 코드 속성 정의
		FieldValue fieldValue = new FieldValue(23, "강릉") ;

		List<FieldValue> FieldValueList = new ArrayList<>();
		FieldValueList.add(fieldValue) ;

		HashMap<String, List<FieldValue>> valuesMap = new HashMap<>() ;
		valuesMap.put("provider_id", FieldValueList) ;


		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, valuesMap) ;


		TaskOutputVo taskOutputVo = new TaskOutputVo() ;
		taskOutputVo.setTaskId(1367);
		taskOutputVo.setProviderId(4);
		taskOutputVo.setProviderName("서울");
		taskOutputVo.setTaskType(TaskType.REGISTER);
		taskOutputVo.setTaskStatusType(TaskStatusType.PROGRESS);
		taskOutputVo.setPubTransId(0);
		taskOutputVo.setPubTransType(PubTransType.ROUTE);
		taskOutputVo.setPubTransName("서울01");
		taskOutputVo.setTaskComment("버스노선 Task 등록");
		taskOutputVo.setTaskDataSourceType(TaskDataSourceType.BIS);
		taskOutputVo.setTaskCheckRequestType(TaskCheckRequestType.CHECK_REQUEST);
		taskOutputVo.setAutoRegisterYn("N");
		taskOutputVo.setRegUserName("test_name");
		taskOutputVo.setRegUserId("test");
		taskOutputVo.setRegDate("2020-04-21 17:35:30");
		taskOutputVo.setWorkUserName("test_name");
		taskOutputVo.setWorkUserId("test");
		taskOutputVo.setWorkAssignDate("2020-04-21 17:35:33");
		taskOutputVo.setWorkCompleteDate("2020-04-21 17:35:33");


		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, taskOutputVo) ;


		//given
		given(taskService.getTaskInfoWithSchema(1367))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/info/task/{taskId}", 1367)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("task/taskInfo",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            pathParameters(
                    parameterWithName("taskId").description("작업 ID")
	            ),
	            responseFields(
	            		fieldWithPath("code").type(NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.schema[]").type(ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"),

	             		fieldWithPath("result.data").type(OBJECT).description("작업 정보"),
	             		fieldWithPath("result.data.taskId").type(NUMBER).description("작업ID"),
	             		fieldWithPath("result.data.providerId").type(JsonFieldType.NUMBER).description("BIS 지역코드"),
	             		fieldWithPath("result.data.providerName").type(STRING_OR_NULL).description("BIS 지역명"),
	             		fieldWithPath("result.data.taskType").type(JsonFieldType.STRING).description(CodeType.TASK_TYPE.getCodeDescription() + TaskType.getCodeAndDescriptionWithColon()),
	             		fieldWithPath("result.data.taskStatusType").type(JsonFieldType.STRING).description(CodeType.TASK_STATUS_TYPE.getCodeDescription() + TaskStatusType.getCodeAndDescriptionWithColon()),
	             		fieldWithPath("result.data.pubTransType").type(JsonFieldType.STRING).description(CodeType.PUB_TRANS_TYPE.getCodeDescription() + PubTransType.getCodeAndDescriptionWithColon()),
	             		fieldWithPath("result.data.taskDataSourceType").type(NUMBER).description(CodeType.TASK_DATA_SOURCE_TYPE.getCodeDescription()),
	                    fieldWithPath("result.data.taskCheckRequestType").type(STRING).description(CodeType.TASK_CHECK_REQUEST_TYPE.getCodeDescription()),
	             		fieldWithPath("result.data.pubTransId").type(NUMBER).description("대중교통 ID"),
	             		fieldWithPath("result.data.pubTransName").type(STRING).description("데이터 이름"),
	             		fieldWithPath("result.data.taskComment").type(STRING).description("작업내용"),
	             		fieldWithPath("result.data.bisChangeDataInfo").type(OBJECT_OR_NULL).description("BIS 자동등록 변경내용"),
	             		fieldWithPath("result.data.autoRegisterYn").type(JsonFieldType.STRING).description("자동 등록여부(Y/N)"),
	             		fieldWithPath("result.data.regUserName").type(STRING).description("등록자명"),
	             		fieldWithPath("result.data.regUserId").type(STRING).description("등록자ID"),
	             		fieldWithPath("result.data.regDate").type(STRING).description("등록일"),
	             		fieldWithPath("result.data.workUserName").type(STRING_OR_NULL).description("작업자명"),
	             		fieldWithPath("result.data.workUserId").type(STRING_OR_NULL).description("작업자ID"),
	             		fieldWithPath("result.data.workAssignDate").type(STRING_OR_NULL).description("작업자 할당일"),
	             		fieldWithPath("result.data.workCompleteDate").type(STRING_OR_NULL).description("작업 완료일"),
	             		fieldWithPath("result.data.checkUserName").type(STRING_OR_NULL).description("검수자명"),
	             		fieldWithPath("result.data.checkUserId").type(STRING_OR_NULL).description("검수자ID"),
	             		fieldWithPath("result.data.checkAssignDate").type(STRING_OR_NULL).description("검수자 할당일"),
	             		fieldWithPath("result.data.checkCompleteDate").type(STRING_OR_NULL).description("검수일")

	             )
 		));

	}

}
