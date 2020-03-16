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
import com.naver.pubtrans.itn.api.controller.NoticeController;
import com.naver.pubtrans.itn.api.service.NoticeService;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeInputVo;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeParameterVo;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeSearchVo;
import com.naver.pubtrans.itn.api.vo.notice.output.NoticeOutputVo;

@RunWith(SpringRunner.class)
@WebMvcTest(NoticeController.class)
@AutoConfigureRestDocs
public class NoticeControllerDocumentTest {

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
	private NoticeService noticeService;

	/**
	 * 공지사항 등록 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void insertNotice() throws Exception {

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT);

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();

		NoticeInputVo noticeInputVo = new NoticeInputVo();
		noticeInputVo.setTitle("test notice");
		noticeInputVo.setContent("test content");
		noticeInputVo.setImportantYn("Y");

		NoticeOutputVo noticeOutputVo = new NoticeOutputVo();
		noticeOutputVo.setSeq(1);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(noticeOutputVo);

		//given
		given(noticeService.insertNotice(any(NoticeInputVo.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			post("/v1/ntool/api/notice")
				.content(objectMapper.writeValueAsString(noticeInputVo))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("notice/insertNotice",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
					fieldWithPath("importantYn").type(JsonFieldType.STRING).description("중요여부(Y/N)")),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
					fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

					fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
					fieldWithPath("result.data.seq").type(JsonFieldType.NUMBER).description("공지사항 ID"))

			));

	}

	/**
	 * 공지사항 정보 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void getNotice() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();

		// 스키마
		SchemaVo schemaSeqVo = new SchemaVo();
		schemaSeqVo.setColumnName("seq");
		schemaSeqVo.setColumnComment("공지사항ID");
		schemaSeqVo.setIsNullable("NO");
		schemaSeqVo.setColumnKey("PRI");
		schemaSeqVo.setColumnType("int(11)");

		SchemaVo schemaTitleVo = new SchemaVo();
		schemaTitleVo.setColumnName("title");
		schemaTitleVo.setColumnComment("제목");
		schemaTitleVo.setIsNullable("YES");
		schemaTitleVo.setColumnKey("");
		schemaTitleVo.setColumnType("string(100)");

		List<SchemaVo> schemaVoList = new ArrayList<>();
		schemaVoList.add(schemaSeqVo);
		schemaVoList.add(schemaTitleVo);

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, null);

		NoticeSearchVo noticeSearchVo = new NoticeSearchVo();
		noticeSearchVo.setSeq(1);

		NoticeOutputVo noticeOutputVo = new NoticeOutputVo();

		noticeOutputVo.setSeq(1);
		noticeOutputVo.setTitle("test title");
		noticeOutputVo.setContent("test content");
		noticeOutputVo.setImportantYn("Y");
		noticeOutputVo.setRegUserId("test");
		noticeOutputVo.setRegUserName("test_user");
		noticeOutputVo.setRegDate("2020.03.10");

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, noticeOutputVo);

		//given
		given(noticeService.getNotice(any(NoticeSearchVo.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/notice/{seq}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("notice/getNotice",
				getDocumentRequest(),
				getDocumentResponse(),
				pathParameters(
					parameterWithName("seq").description("공지사항 ID")),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
					fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

					subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY)
						.description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]").optional(),

					fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
					fieldWithPath("result.data.seq").type(JsonFieldType.NUMBER).description("공지사항 ID"),
					fieldWithPath("result.data.title").type(JsonFieldType.STRING).description("제목"),
					fieldWithPath("result.data.content").type(JsonFieldType.STRING).description("내용"),
					fieldWithPath("result.data.importantYn").type(JsonFieldType.STRING).description("중요여부(Y/N)"),
					fieldWithPath("result.data.regUserId").type(JsonFieldType.STRING).description("등록자 ID"),
					fieldWithPath("result.data.regUserName").type(JsonFieldType.STRING).description("등록자명"),
					fieldWithPath("result.data.regDate").type(JsonFieldType.STRING).description("등록일"))));

	}

	/**
	 * 공지사항 정보 수정 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void putNotice() throws Exception {

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT);

		NoticeInputVo noticeInputVo = new NoticeInputVo();
		noticeInputVo.setSeq(1);
		noticeInputVo.setTitle("test notice");
		noticeInputVo.setContent("test content");
		noticeInputVo.setImportantYn("Y");

		//when
		ResultActions result = this.mockMvc.perform(
			put("/v1/ntool/api/notice")
				.content(objectMapper.writeValueAsString(noticeInputVo))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("notice/updateNotice",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					fieldWithPath("seq").type(JsonFieldType.NUMBER).description("공지사항 ID"),
					fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
					fieldWithPath("importantYn").type(JsonFieldType.STRING).description("중요여부(Y/N)")),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"))));
	}

	/**
	 * 공지사항 정보 삭제 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void deleteNotice() throws Exception {

		NoticeParameterVo noticeParameterVo = new NoticeParameterVo();
		noticeParameterVo.setSeq(1);

		//when
		ResultActions result = this.mockMvc.perform(
			delete("/v1/ntool/api/notice")
				.content(objectMapper.writeValueAsString(noticeParameterVo))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("notice/deleteNotice",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					fieldWithPath("seq").type(JsonFieldType.NUMBER).description("공지사항ID")),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"))));
	}

	/**
	 * 공지사항 목록 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void listNotice() throws Exception {

		objectMapper.setSerializationInclusion(Include.NON_DEFAULT);

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();

		// 리스트 페이징 정보
		PagingVo pagingVo = new PagingVo(100, 1, 20);

		// 스키마
		SchemaVo schemaUserIdVo = new SchemaVo();
		schemaUserIdVo.setColumnName("user_id");
		schemaUserIdVo.setColumnComment("공지사항ID");
		schemaUserIdVo.setIsNullable("NO");
		schemaUserIdVo.setColumnKey("PRI");
		schemaUserIdVo.setColumnType("string(30)");

		SchemaVo schemaUserNameVo = new SchemaVo();
		schemaUserNameVo.setColumnName("user_name");
		schemaUserNameVo.setColumnComment("이름");
		schemaUserNameVo.setIsNullable("NO");
		schemaUserNameVo.setColumnKey("");
		schemaUserNameVo.setColumnType("string(30)");

		List<SchemaVo> schemaVoList = new ArrayList<>();
		schemaVoList.add(schemaUserIdVo);
		schemaVoList.add(schemaUserNameVo);

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, null);

		// 목록
		NoticeOutputVo noticeOutputVo = new NoticeOutputVo();
		noticeOutputVo.setSeq(1);
		noticeOutputVo.setTitle("test title");
		noticeOutputVo.setImportantYn("Y");
		noticeOutputVo.setRegUserId("test");
		noticeOutputVo.setRegUserName("test_user");
		noticeOutputVo.setRegDate("2020.03.10");

		List<NoticeOutputVo> noticeOutputVoList = new ArrayList<>();
		noticeOutputVoList.add(noticeOutputVo);

		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, noticeOutputVoList);

		//given
		given(noticeService.selectNoticeList(any(NoticeSearchVo.class)))
			.willReturn(commonResult);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/list/notice")
				.param("pageNo", "1")
				.param("listSize", "20")
				.param("searchType", "title")
				.param("searchKeyword", "test")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("notice/listNotice",
				getDocumentRequest(),
				getDocumentResponse(),
				requestParameters(
					parameterWithName("pageNo").description("[선택]페이지 번호(기본:1)").optional(),
					parameterWithName("listSize").description("[선택]페이지당 목록 수(기본:20)").optional(),
					parameterWithName("searchType").description("[선택]검색 유형(title: 제목, reg_user_name: 작성자, content: 내용)")
						.optional(),
					parameterWithName("searchKeyword").description("[선택]검색 키워드")
						.optional()),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
					fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

					subsectionWithPath("result.meta").type(JsonFieldType.OBJECT)
						.description("페이징 정보 - link:#_데이터_목록_페이징_정보[공통사항 참고]"),
					subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY)
						.description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"),

					fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("공지사항 목록"),
					fieldWithPath("result.data[].seq").type(JsonFieldType.NUMBER).description("공지사항 ID"),
					fieldWithPath("result.data[].title").type(JsonFieldType.STRING).description("제목"),
					fieldWithPath("result.data[].importantYn").type(JsonFieldType.STRING).description("중요여부(Y/N)"),
					fieldWithPath("result.data[].regUserId").type(JsonFieldType.STRING).description("등록자 ID"),
					fieldWithPath("result.data[].regUserName").type(JsonFieldType.STRING).description("등록자명"),
					fieldWithPath("result.data[].regDate").type(JsonFieldType.STRING).description("등록일"))));

	}

	/**
	 * 공지사항 스키마 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void schemaNotice() throws Exception {

		List<CommonSchema> commonSchema = new ArrayList<>();

		//given
		given(noticeService.selectNoticeSchema())
			.willReturn(commonSchema);

		//when
		ResultActions result = this.mockMvc.perform(
			get("/v1/ntool/api/schema/notice")
				.characterEncoding("UTF-8"));

		//then
		result.andExpect(status().isOk())
			.andDo(document("notice/schemaNotice",
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
