package com.naver.pubtrans.itn.api.controller.document;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;


import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.controller.SampleController;
import com.naver.pubtrans.itn.api.service.SampleService;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.sample.SampleVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleInputVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleSearchVo;



@RunWith(SpringRunner.class)
@WebMvcTest(SampleController.class)
@AutoConfigureRestDocs
public class SampleControllerTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
    private ObjectMapper objectMapper;

	@MockBean
	private SampleService sampleService ;


	@Test
	public void sampleList() throws Exception {


		SampleVo vo1 = new SampleVo() ;
		vo1.setId(1);
		vo1.setSampleData("개발팀");
		vo1.setRegDate("2020-01-20");

		SampleVo vo2 = new SampleVo() ;
		vo2.setId(2);
		vo2.setSampleData("기획팀");
		vo2.setRegDate("2020-01-20");

		SampleVo vo3 = new SampleVo() ;
		vo3.setId(3);
		vo3.setSampleData("디자인팀");
		vo3.setRegDate("2020-01-21");


		List<SampleVo> list = new ArrayList<>();
		list.add(vo1);
		list.add(vo2);
		list.add(vo3);


		SampleSearchVo searchVo = new SampleSearchVo();
		searchVo.setPageNo(1);
		searchVo.setListSize(20);


		PagingVo pagingVo = new PagingVo(list.size(), searchVo.getPageNo(), searchVo.getListSize()) ;

		OutputFmtUtil outputFmtUtil = new OutputFmtUtil() ;

		CommonResult cmnRs = outputFmtUtil.setCommonListFmt(pagingVo, list) ;



		//given
		given(sampleService.getSampleList(any(SampleSearchVo.class)))
				.willReturn(cmnRs) ;



		//when
		ResultActions result = this.mockMvc.perform(
                get("/samples")
	                .param("pageNo", "1")
	                .param("listSize", "20")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );

		//then
		result.andExpect(status().isOk())
	 		.andDo(document("sampleList",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("pageNo").description("[선택]페이지 번호(기본:1)"),
	            		parameterWithName("listSize").description("[선택]페이지당 목록 수(기본:20)")
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
	             		fieldWithPath("result.meta").type(JsonFieldType.OBJECT).description("페이지 정보"),
	             		fieldWithPath("result.meta.totalListCount").type(JsonFieldType.NUMBER).description("전체 목록 수"),
	             		fieldWithPath("result.meta.listCountPerPage").type(JsonFieldType.NUMBER).description("페이지당 목록 수"),
	             		fieldWithPath("result.meta.totalPageCount").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
	             		fieldWithPath("result.meta.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
	             		fieldWithPath("result.meta.firstPage").type(JsonFieldType.BOOLEAN).description("첫번째 페이지 여부"),
	             		fieldWithPath("result.meta.lastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
	             		fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("목록"),
	             		fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("아이디"),
	             		fieldWithPath("result.data[].sampleData").type(JsonFieldType.STRING).description("제목"),
	             		fieldWithPath("result.data[].regDate").type(JsonFieldType.STRING).description("등록일")
	             )
 		));

	}


	@Test
	public void sampleDetail() throws Exception {

		SampleVo sampleVo = new SampleVo() ;
		sampleVo.setId(1);
		sampleVo.setSampleData("개발팀");
		sampleVo.setRegDate("2020-01-20");

		CommonResult cmnRs = new CommonResult(sampleVo) ;



		//given
		given(sampleService.getSampleDataById(1))
				.willReturn(cmnRs) ;

		//when
		ResultActions result = this.mockMvc.perform(
                get("/samples/{id}", 1)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8")
        );

		//then
		result.andExpect(status().isOk())
	 		.andDo(document("sampleDetail",
	 			getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                         parameterWithName("id").description("컨텐츠 ID")
                ),
                responseFields(
                 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
                 		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
                 		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),
                 		fieldWithPath("result.data").type(JsonFieldType.OBJECT).description("데이터"),
                 		fieldWithPath("result.data.id").type(JsonFieldType.NUMBER).description("아이디"),
                 		fieldWithPath("result.data.sampleData").type(JsonFieldType.STRING).description("내용"),
                 		fieldWithPath("result.data.regDate").type(JsonFieldType.STRING).description("등록/수정일")
                 )
	 	));
	}



	@Test
	public void sampleRegister() throws Exception {
		SampleInputVo inputVo = new SampleInputVo();
		inputVo.setSampleData("샘플 데이터 입력");


		ResultActions result = this.mockMvc.perform(
                post("/samples/register")
                    .content(objectMapper.writeValueAsString(inputVo))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("sampleRegister",
	 			getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("sampleData").type(JsonFieldType.STRING).description("내용")

                ),
                responseFields(
                 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
                 		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지")
                )
	 	));
	}


	@Test
	public void sampleEdit() throws Exception {
		SampleInputVo inputVo = new SampleInputVo();
		inputVo.setId(2);
		inputVo.setSampleData("샘플 데이터 수정");


		ResultActions result = this.mockMvc.perform(
                put("/samples/edit")
                    .content(objectMapper.writeValueAsString(inputVo))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("sampleEdit",
	 			getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                		fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                        fieldWithPath("sampleData").type(JsonFieldType.STRING).description("내용")

                ),
                responseFields(
                 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
                 		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지")
                )
	 	));
	}

}
