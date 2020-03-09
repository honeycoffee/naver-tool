package com.naver.pubtrans.itn.api.controller.document;

import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.naver.pubtrans.itn.api.controller.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.controller.BusStopController;
import com.naver.pubtrans.itn.api.service.BusStopService;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;

@RunWith(SpringRunner.class)
@WebMvcTest(BusStopController.class)
@AutoConfigureRestDocs
public class BusStopControllerTests {


	@Autowired
	MockMvc mockMvc;

	@MockBean
	private BusStopService busStopService ;



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
		schemaVo.setColumnName("stop_id");
		schemaVo.setColumnComment("정류장ID");
		schemaVo.setIsNullable("NO");
		schemaVo.setColumnKey("PRI");
		schemaVo.setColumnType("int(11)");


		SchemaVo schemaCityVo = new SchemaVo();
		schemaCityVo.setColumnName("citycode");
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
		valuesMap.put("cityCode", FieldValueList) ;


		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, valuesMap) ;



		// 목록
		BusStopListOutputVo busStopListOutputVo = new BusStopListOutputVo();
		busStopListOutputVo.setStopId(88);
		busStopListOutputVo.setStopName("금강제화");
		busStopListOutputVo.setLongitude(126.98757);
		busStopListOutputVo.setLatitude(37.570551);
		busStopListOutputVo.setCityCode("1000");
		busStopListOutputVo.setCityName("서울");


		List<BusStopListOutputVo> busStopListOutputVoList = new ArrayList<>();
		busStopListOutputVoList.add(busStopListOutputVo) ;




		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, busStopListOutputVoList) ;


		//given
		given(busStopService.getBusStopList(any(BusStopSearchVo.class)))
				.willReturn(commonResult) ;


		//when
		ResultActions result = this.mockMvc.perform(
                get("/v1/ntool/api/list/busStop")
                	.param("stopName", "")
                	.param("citycode", "1000")
                	.param("pageNo", "1")
                	.param("listSize", "20")
                	.param("sort", "stopId,asc")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .characterEncoding("UTF-8")
        );


		//then
		result.andExpect(status().isOk())
	 		.andDo(document("busStop/busStopList",
	 			getDocumentRequest(),
	            getDocumentResponse(),
	            requestParameters(
	            		parameterWithName("stopName").description("[선택]정류장명").optional(),
	            		parameterWithName("stopId").description("[선택]정류장ID").optional(),
	            		parameterWithName("citycode").description("[선택]도시코드").optional(),
	            		parameterWithName("pageNo").description("[선택]페이지 번호(기본:1)").optional(),
	            		parameterWithName("listSize").description("[선택]페이지당 목록 수(기본:20)").optional(),
	            		parameterWithName("sort").description("[선택]정렬(기본:목록 첫번째 Key 내림차순) - 사용 예:stopName,asc").optional()
	            ),
	            responseFields(
	            		fieldWithPath("code").type(JsonFieldType.NUMBER).description("API 응답코드"),
	             		fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메세지"),
	             		fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과 정보"),

	             		subsectionWithPath("result.meta").type(JsonFieldType.OBJECT).description("페이징 정보 - link:#_데이터_목록_페이징_정보[공통사항 참고]"),
	             		subsectionWithPath("result.schema[]").type(JsonFieldType.ARRAY).description("데이터 필드 정보 - link:#_데이터_스키마_정보[공통사항 참고]"),

	             		fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("정류장 목록"),
	             		fieldWithPath("result.data[].stopId").type(JsonFieldType.NUMBER).description("정류장ID"),
	             		fieldWithPath("result.data[].stopName").type(JsonFieldType.STRING).description("정류장명"),
	             		fieldWithPath("result.data[].longitude").type(JsonFieldType.NUMBER).description("경도"),
	             		fieldWithPath("result.data[].latitude").type(JsonFieldType.NUMBER).description("위도"),
	             		fieldWithPath("result.data[].cityCode").type(JsonFieldType.STRING).description("도시코드"),
	             		fieldWithPath("result.data[].cityName").type(JsonFieldType.STRING).description("도시코드명")
	             )
 		));

	}

}
