package com.naver.pubtrans.itn.api.controller.document;

import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.RequestHeader;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.controller.AuthController;
import com.naver.pubtrans.itn.api.service.AuthService;
import com.naver.pubtrans.itn.api.vo.auth.LoginVo;
import com.naver.pubtrans.itn.api.vo.auth.input.AuthInputVo;
import com.naver.pubtrans.itn.api.vo.auth.output.AuthOutputVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;



@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@AutoConfigureRestDocs
public class AuthControllerTests {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	private AuthService authService ;

	/**
	 * 회원 로그인 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void login() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;
		
		// 회원 정보
		MemberOutputVo memberOutputVo = new MemberOutputVo();
		
		memberOutputVo.setUserId("1111");
		memberOutputVo.setUserName("테스트유저");
		memberOutputVo.setUserPw("$2a$10$VtdsXJoEdcw22nI6uMlAA..QjfuAK.kd8WC8U0W0pWBw6xCuLfVly");
		memberOutputVo.setCompany("소속");
		memberOutputVo.setRegDate("2020-02-27 16:35:02");
		memberOutputVo.setUpdDate("2020-02-27 16:35:02");

		// API 호출 토큰 및 갱신 토큰
		AuthOutputVo authOutputVo = new AuthOutputVo();
		
		String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzg1MDgzLCJ1c2VySWQiOiIzMzMzIn0.piLNmfoXMYZIh4_-k3Qut7mwqvkjvzItpVwZXJX5zBw";
		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzQ1NDg0LCJ1c2VySWQiOiIzMzMzIn0.ueSpRBHHgi5qPJlJl7AAkj9URMGDE6HK78HKT1rfa10";
		
		authOutputVo.setAccessToken(accessToken);
		authOutputVo.setRefreshToken(refreshToken);
		
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authOutputVo);
		
		//given
		given(authService.loginMember(any(LoginVo.class), any(HttpServletRequest.class)))
				.willReturn(commonResult) ;
		
		//when
		ResultActions result = this.mockMvc.perform(
            post("/v1/ntool/api/auth/login")
	        	.param("userId", "1111")
	        	.param("userPw", "qwer1234")
	        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8")
        );
		
		
		//then
		result.andExpect(status().isOk())
	 		.andDo(document("login",
	 			getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
	            		parameterWithName("userId").description("회원ID"),
	            		parameterWithName("userPw").description("비밀번호")
                        
                ),
                responseFields(
                 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
                 		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
                 		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
                 		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
                 		fieldWithPath("result.data.accessToken").type(JsonFieldType.STRING).description("API 인증 토큰"),
                 		fieldWithPath("result.data.refreshToken").type(JsonFieldType.STRING).description("API 인증 토큰 갱신 용 토큰")
                )
	 	));
	}

	/**
	 * API 호출 토큰 갱신 rest docs 생성
	 * @throws Exception
	 */
	@Test
	public void refresh() throws Exception {

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;
		
		String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzg1MDgzLCJ1c2VySWQiOiIzMzMzIn0.piLNmfoXMYZIh4_-k3Qut7mwqvkjvzItpVwZXJX5zBw";
		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RVc2VyMTExIiwiZXhwIjoxNTgzNzQ1NDg0LCJ1c2VySWQiOiIzMzMzIn0.ueSpRBHHgi5qPJlJl7AAkj9URMGDE6HK78HKT1rfa10";
		
		// API 호출 토큰 및 갱신 토큰
		AuthOutputVo authOutputVo = new AuthOutputVo();
		
		authOutputVo.setAccessToken(accessToken);
		authOutputVo.setRefreshToken(refreshToken);
		
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(authOutputVo);
		
		//given
		given(authService.refreshToken(any(HttpServletRequest.class)))
				.willReturn(commonResult) ;
		
		//when
		ResultActions result = this.mockMvc.perform(
            post("/v1/ntool/api/auth/refresh/token")
	        	.header(JwtAdapter.HEADER_NAME, refreshToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
	            .characterEncoding("UTF-8")
        );
		
		
		//then
		result.andExpect(status().isOk())
	 		.andDo(document("refreshToken",
	 			getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
                 		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
                 		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
                 		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
                 		fieldWithPath("result.data.accessToken").type(JsonFieldType.STRING).description("API 인증 토큰"),
                 		fieldWithPath("result.data.refreshToken").type(JsonFieldType.STRING).description("API 인증 토큰 갱신 용 토큰")
                )
	 	));
	}
	
}
