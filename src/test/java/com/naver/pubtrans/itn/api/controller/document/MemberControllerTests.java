package com.naver.pubtrans.itn.api.controller.document;

import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.controller.MemberController;
import com.naver.pubtrans.itn.api.service.MemberService;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberListOutputVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;



@RunWith(SpringRunner.class)
@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs
public class MemberControllerTests {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	private OutputFmtUtil outputFmtUtil ;
	
	@MockBean
	private MemberService memberService ;

	/**
	 * 회원 정보 등록 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void registerMember() throws Exception {
		MemberInputVo memberInputVo = new MemberInputVo();
		
		memberInputVo.setUserId("test_id");
		memberInputVo.setUserName("test_name");
		memberInputVo.setUserPw("test_password");
		memberInputVo.setCompany("test_company");
				
		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/register/member")
                    .content(objectMapper.writeValueAsString(memberInputVo))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
        );
		
		
		//then
		result.andExpect(status().isOk())
	 		.andDo(document("member/registerMember",
	 			getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("userId").type(JsonFieldType.STRING).description("회원ID"),
                        fieldWithPath("userName").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("userPw").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("company").type(JsonFieldType.STRING).description("소속")
                        
                ),
                responseFields(
                 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
                 		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지")
                )
	 	));
	}

	/**
	 * ID 중복 확인 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void checkDuplicate() throws Exception {
		
		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();
		
		HashMap<String, Boolean> resultMap = new HashMap<String, Boolean>();
		resultMap.put("duplicate", true);
		
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt((resultMap)) ;
		
		//given
		given(memberService.checkDuplicate("test_id"))
				.willReturn(commonResult) ;
				
		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/duplicate/member")
                	.param("userId", "test_id")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
        );
		
		
		//then
		result.andExpect(status().isOk())
	 		.andDo(document("member/checkDuplicate",
	 			getDocumentRequest(),
                getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("userId").description("회원ID")
	            ),
                responseFields(
                 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
                 		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
                 		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
                 		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
                 		fieldWithPath("result.data.duplicate").type(JsonFieldType.BOOLEAN).description("중복여부 (true: 중복, false: 사용가능)")
                )
	 	));
	}

	/**
	 * 비밀번호 검증 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void verifyPassword() throws Exception {
		
		OutputFmtUtil outputFmtUtil = new OutputFmtUtil();
		
		HashMap<String, Boolean> resultMap = new HashMap<String, Boolean>();
		resultMap.put("verify", true);
		
		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId("test");
		
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap) ;
		
		//given
		given(memberService.verifyPassword(any(MemberSearchVo.class), ArgumentMatchers.anyString()))
				.willReturn(commonResult) ;
		
		//when
		ResultActions result = this.mockMvc.perform(
                post("/v1/ntool/api/verify/password")
                	.param("userPw", "qwer1234")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
        );
		
		
		//then
		result.andExpect(status().isOk())
	 		.andDo(document("member/verifyPassword",
	 			getDocumentRequest(),
                getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("userPw").description("비밀번호")
	            ),
                responseFields(
                 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
                 		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
                 		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
                 		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
                 		fieldWithPath("result.data.verify").type(JsonFieldType.BOOLEAN).description("비밀번호 일치여부 (true: 일치, false: 불일치)")
                )
	 	));
	}

	/**
	 * 내 정보 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoMe() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;
		
		// 스키마
		SchemaVo schemaUserIdVo = new SchemaVo();
		schemaUserIdVo.setColumnName("user_id");
		schemaUserIdVo.setColumnComment("회원ID");
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
		schemaVoList.add(schemaUserIdVo) ;
		schemaVoList.add(schemaUserNameVo) ;

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, null) ;
		
		MemberSearchVo memberSearchVo = new MemberSearchVo();
    	memberSearchVo.setUserId("test");
    	
    	String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RfdXNlciIsImV4cCI6MTU5MTgzMTM2NCwidXNlcklkIjoidGVzdCJ9.J3jWR6IDJU6Ly_okU-T3F8lSQXC9tpgbX6TSH7R8hHo";
    	
    	MemberOutputVo memberOutputVo = new MemberOutputVo();
    	
    	memberOutputVo.setUserId("test");
    	memberOutputVo.setUserName("test_user");
    	memberOutputVo.setCompany("test_company");
    	memberOutputVo.setRegDate("2020.03.10");
		
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, memberOutputVo);
		
		//given
		given(memberService.getMemberDataWithSchema(any(MemberSearchVo.class)))
				.willReturn(commonResult) ;
		
		//when
		ResultActions result = this.mockMvc.perform(
            get("/v1/ntool/api/info/me")
	        	.header(JwtAdapter.HEADER_NAME, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8")
        );
		
		
		//then
		result.andExpect(status().isOk())
	 		.andDo(document("member/infoMe",
	 			getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
                 		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
                 		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
                 		
                 		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]").optional(),
                 		
                 		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
                 		fieldWithPath("result.data.userId").type(JsonFieldType.STRING).description("회원 ID"),
                 		fieldWithPath("result.data.userName").type(JsonFieldType.STRING).description("이름"),
                 		fieldWithPath("result.data.company").type(JsonFieldType.STRING).description("소속"),
                 		fieldWithPath("result.data.regDate").type(JsonFieldType.STRING).description("가입일")
                )
	 	));
	}

	/**
	 * 내 정보 수정 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void modifyMe() throws Exception {

    	String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RfdXNlciIsImV4cCI6MTU5MTgzMTM2NCwidXNlcklkIjoidGVzdCJ9.J3jWR6IDJU6Ly_okU-T3F8lSQXC9tpgbX6TSH7R8hHo";
    	
    	MemberInputVo memberInputVo = new MemberInputVo();
    	
    	memberInputVo.setUserId("test");
    	memberInputVo.setUserName("test_user");
    	memberInputVo.setCompany("test_company");
    	memberInputVo.setUserPw("qwer1234");
		
		//when
		ResultActions result = this.mockMvc.perform(
		        put("/v1/ntool/api/modify/me")
	        		.header(JwtAdapter.HEADER_NAME, accessToken)
		            .content(objectMapper.writeValueAsString(memberInputVo))
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON)
		            .characterEncoding("UTF-8")
		);
		
		
		//then
		result.andExpect(status().isOk())
			.andDo(document("member/modifyMe",
				getDocumentRequest(),
		        getDocumentResponse(),
		        requestFields(
		                fieldWithPath("userId").type(JsonFieldType.STRING).description("회원ID"),
		                fieldWithPath("userName").type(JsonFieldType.STRING).description("이름"),
		                fieldWithPath("userPw").type(JsonFieldType.STRING).description("비밀번호"),
		                fieldWithPath("company").type(JsonFieldType.STRING).description("소속")
		                
		        ),
		        responseFields(
		         		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
		         		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지")
		        )
			));
	}

	/**
	 * 회원 정보 조회 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void infoMember() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;
		
		// 스키마
		SchemaVo schemaUserIdVo = new SchemaVo();
		schemaUserIdVo.setColumnName("user_id");
		schemaUserIdVo.setColumnComment("회원ID");
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
		schemaVoList.add(schemaUserIdVo) ;
		schemaVoList.add(schemaUserNameVo) ;

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, null) ;
		
		MemberSearchVo memberSearchVo = new MemberSearchVo();
    	memberSearchVo.setUserId("test");
    	
    	MemberOutputVo memberOutputVo = new MemberOutputVo();
    	
    	memberOutputVo.setUserId("test");
    	memberOutputVo.setUserName("test_user");
    	memberOutputVo.setCompany("test_company");
    	memberOutputVo.setRegDate("2020.03.10");
		
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, memberOutputVo);
		
		//given
		given(memberService.getMemberDataWithSchema(any(MemberSearchVo.class)))
				.willReturn(commonResult) ;
		
		//when
		ResultActions result = this.mockMvc.perform(
            get("/v1/ntool/api/info/member/${userId}", "test")
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8")
		);


		//then
		result.andExpect(status().isOk())
			.andDo(document("member/infoMember",
				getDocumentRequest(),
		        getDocumentResponse(),
	            pathParameters(
	                    parameterWithName("userId").description("회원 ID")
	            ),
	            responseFields(
	             		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
	             		
	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]").optional(),
	             		
	             		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
	             		fieldWithPath("result.data.userId").type(JsonFieldType.STRING).description("회원 ID"),
	             		fieldWithPath("result.data.userName").type(JsonFieldType.STRING).description("이름"),
	             		fieldWithPath("result.data.company").type(JsonFieldType.STRING).description("소속"),
	             		fieldWithPath("result.data.regDate").type(JsonFieldType.STRING).description("가입일")
	            )
		 	));
	}

	/**
	 * 회원 정보 수정 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void modifyMember() throws Exception {

    	MemberInputVo memberInputVo = new MemberInputVo();
    	
    	memberInputVo.setUserId("test");
    	memberInputVo.setUserName("test_user");
    	memberInputVo.setCompany("test_company");
    	memberInputVo.setUserPw("qwer1234");
		
		//when
		ResultActions result = this.mockMvc.perform(
		        put("/v1/ntool/api/modify/member")
		            .content(objectMapper.writeValueAsString(memberInputVo))
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON)
		            .characterEncoding("UTF-8")
		);
		
		
		//then
		result.andExpect(status().isOk())
			.andDo(document("member/modifyMember",
				getDocumentRequest(),
		        getDocumentResponse(),
		        requestFields(
		                fieldWithPath("userId").type(JsonFieldType.STRING).description("회원ID"),
		                fieldWithPath("userName").type(JsonFieldType.STRING).description("이름"),
		                fieldWithPath("userPw").type(JsonFieldType.STRING).description("비밀번호"),
		                fieldWithPath("company").type(JsonFieldType.STRING).description("소속")
		                
		        ),
		        responseFields(
		         		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
		         		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지")
		        )
			));
	}

	/**
	 * 회원 정보 삭제 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void removeMember() throws Exception {

		MemberSearchVo memberSearchVo = new MemberSearchVo();
    	memberSearchVo.setUserId("test");
    	
		//when
		ResultActions result = this.mockMvc.perform(
            delete("/v1/ntool/api/remove/member")
		    	.param("userId", "test")
		        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		        .accept(MediaType.APPLICATION_JSON)
		        .characterEncoding("UTF-8")
			);


		//then
		result.andExpect(status().isOk())
			.andDo(document("member/removeMember",
				getDocumentRequest(),
		        getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("userId").description("회원ID")
	            ),
	            responseFields(
                 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
                 		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지")
	            )
		 	));
	}
	
	/**
	 * 회원 목록 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void listMember() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;



		// 리스트 페이징 정보
		PagingVo pagingVo = new PagingVo(100, 1, 20) ;



		// 스키마
		SchemaVo schemaUserIdVo = new SchemaVo();
		schemaUserIdVo.setColumnName("user_id");
		schemaUserIdVo.setColumnComment("회원ID");
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
		schemaVoList.add(schemaUserIdVo) ;
		schemaVoList.add(schemaUserNameVo) ;

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, null) ;



		// 목록
		MemberListOutputVo memberListOutputVo = new MemberListOutputVo();
		memberListOutputVo.setUserId("test");
    	memberListOutputVo.setUserName("test_user");
    	memberListOutputVo.setCompany("test_company");
    	memberListOutputVo.setRegDate("2020.03.10");


		List<MemberListOutputVo> memberListOutputVoList = new ArrayList<>();
		memberListOutputVoList.add(memberListOutputVo) ;




		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, memberListOutputVoList) ;


		//given
		given(memberService.selectMemberList(any(MemberSearchVo.class)))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/member")
                	.param("userName", "")
                	.param("pageNo", "1")
                	.param("listSize", "20")
                	.param("sort", "userName,asc")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("member/listMember",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("userName").description("[선택]이름").optional(),
	            		parameterWithName("pageNo").description("[선택]페이지 번호(기본:1)").optional(),
	            		parameterWithName("listSize").description("[선택]페이지당 목록 수(기본:20)").optional(),
	            		parameterWithName("sort").description("[선택]정렬(기본:목록 첫번째 Key 내림차순) - 사용 예:userName,asc").optional()
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.meta").type(JsonFieldType.OBJECT).description("페이징 정보 - link:#_데이터_목록_페이징_정보[공통사항 참고]"),
	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"),

	             		fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("회원 목록"),
	             		fieldWithPath("result.data[].userId").type(JsonFieldType.STRING).description("회원 ID"),
	             		fieldWithPath("result.data[].userName").type(JsonFieldType.STRING).description("이름"),
	             		fieldWithPath("result.data[].company").type(JsonFieldType.STRING).description("소속"),
	             		fieldWithPath("result.data[].regDate").type(JsonFieldType.STRING).description("가입일")
	             )
 		));

	}

//	/**
//	 * API 호출 토큰 갱신 rest docs 생성
//	 * @throws Exception
//	 */
//	@Test
//	public void schemaMember() throws Exception {
//
//		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;
//		
//		// 스키마
//		SchemaVo schemaUserIdVo = new SchemaVo();
//		schemaUserIdVo.setColumnName("user_id");
//		schemaUserIdVo.setColumnComment("회원ID");
//		schemaUserIdVo.setIsNullable("NO");
//		schemaUserIdVo.setColumnKey("PRI");
//		schemaUserIdVo.setColumnType("string(30)");
//
//
//		SchemaVo schemaUserNameVo = new SchemaVo();
//		schemaUserNameVo.setColumnName("user_name");
//		schemaUserNameVo.setColumnComment("이름");
//		schemaUserNameVo.setIsNullable("NO");
//		schemaUserNameVo.setColumnKey("");
//		schemaUserNameVo.setColumnType("string(30)");
//
//
//		List<SchemaVo> schemaVoList = new ArrayList<>();
//		schemaVoList.add(schemaUserIdVo) ;
//		schemaVoList.add(schemaUserNameVo) ;
//
//		// 검색 폼 데이터 구조
//		List<CommonSchema> commonSchema= outputFmtUtil.makeCommonSchema(schemaVoList, null, null) ;
//		
//		//given
//		given(memberService.selectMemberSchema())
//				.willReturn(commonSchema) ;
//		
//		//when
//		ResultActions result = this.mockMvc.perform(
//            get("/v1/ntool/api/schema/member")
//	            .characterEncoding("UTF-8")
//		);
//
//
//		//then
//		result.andExpect(status().isOk())
//			.andDo(document("schemaMember",
//				getDocumentRequest(),
//		        getDocumentResponse(),
//	            responseFields(
//	             		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
//	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
//	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
//	             		
//	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]").optional()
//	            )
//		 	));
//	}
	
}
