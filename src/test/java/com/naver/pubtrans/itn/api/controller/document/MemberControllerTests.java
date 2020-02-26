package com.naver.pubtrans.itn.api.controller.document;

import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.pubtrans.itn.api.controller.MemberController;
import com.naver.pubtrans.itn.api.service.MemberService;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;



@RunWith(SpringRunner.class)
@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs
public class MemberControllerTests {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	private MemberService memberService ;
	
	@Test
	public void memberRegister() throws Exception {
		MemberInputVo inputVo = new MemberInputVo();
		
		inputVo.setUserId("test_id");
		inputVo.setUserName("test_name");
		inputVo.setUserPw("test_password");
		inputVo.setCompany("test_company");
				
		ResultActions result = this.mockMvc.perform(
                post("/ntool/api/members/register")
                    .content(objectMapper.writeValueAsString(inputVo))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
        );
		
		
		//then
		result.andExpect(status().isOk())
	 		.andDo(document("memberRegister",
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
	
	@Test
	public void checkDuplicate() throws Exception {
		HashMap<String, Boolean> resultMap = new HashMap<String, Boolean>();
		resultMap.put("duplicate", true);
		
		CommonResult cmnRs = new CommonResult(resultMap) ;
		
		//given
		given(memberService.checkDuplicate("test_id"))
				.willReturn(cmnRs) ;
				
		ResultActions result = this.mockMvc.perform(
                get("/ntool/api/members/duplicate")
                	.param("userId", "test_id")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
        );
		
		
		//then
		result.andExpect(status().isOk())
	 		.andDo(document("checkDuplicate",
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
	
}
