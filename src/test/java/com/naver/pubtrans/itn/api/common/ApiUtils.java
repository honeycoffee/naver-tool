package com.naver.pubtrans.itn.api.common;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedHashMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.vo.auth.LoginVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeInputVo;

/**
 * 테스트용 공통 유틸 Class
 * @author westwind
 *
 */
public class ApiUtils {

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	public ApiUtils(MockMvc mockMvc, ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.mockMvc = mockMvc;
	}

	/**
	 * accessToken, refreshToken이 필요한 테스트를 위해 로그인 진행 후 TokenMap 전달
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, String> getTokenMap() throws Exception {
		LoginVo loginVo = new LoginVo();
		loginVo.setUserId("test");
		loginVo.setUserPw("qwer1234");

		// accessToken을 가져오기 위해 Login API 실행
		MvcResult mvcResult = mockMvc.perform(post("/v1/ntool/api/auth/login")
			.content(objectMapper.writeValueAsString(loginVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andReturn();

		String jsonString = mvcResult.getResponse().getContentAsString();
		CommonOutput commonOutput = objectMapper.readValue(jsonString, CommonOutput.class);
		CommonResult commonReuslt = commonOutput.getResult();

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> tokenMap = (LinkedHashMap<String, String>)commonReuslt.getData();

		// 테스트 회원ID 삽입
		tokenMap.put("userId", loginVo.getUserId());

		return tokenMap;
	}

	/**
	 * 공지사항 seq가 필요한 test를 위해 공지사항 등록 후 seq 전달
	 * @return
	 * @throws Exception
	 */
	public int getNoticeSeq() throws Exception {

		LinkedHashMap<String, String> tokenMap = this.getTokenMap();
		String accessToken = tokenMap.get("accessToken");

		NoticeInputVo noticeInputVo = new NoticeInputVo();
		noticeInputVo.setTitle("test notice");
		noticeInputVo.setContent("test content");
		noticeInputVo.setImportantYn("Y");

		MvcResult mvcResult = mockMvc.perform(post("/v1/ntool/api/notice")
			.header(JwtAdapter.HEADER_NAME, accessToken)
			.content(objectMapper.writeValueAsString(noticeInputVo))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"))
			.andReturn();

		String jsonString = mvcResult.getResponse().getContentAsString();
		CommonOutput commonOutput = objectMapper.readValue(jsonString, CommonOutput.class);
		CommonResult commonReuslt = commonOutput.getResult();

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> seqMap = (LinkedHashMap<String, Integer>)commonReuslt.getData();
		int seq = seqMap.get("seq");

		return seq;


	}


	/**
	 * 버스노선 상세정보를 가져온다.
	 * @param routeId - 노선ID
	 * @return
	 * @throws Exception
	 */
	public String getBusRouteDetailInfo(int routeId) throws Exception {

		LinkedHashMap<String, String> tokenMap = this.getTokenMap();
		String accessToken = tokenMap.get("accessToken");

		MvcResult mvcResult = mockMvc.perform(get("/v1/ntool/api/info/busRoute/{routeId}", routeId)
			.header(JwtAdapter.HEADER_NAME, accessToken)
        	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
        	.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))
			.andReturn();


		String jsonString = mvcResult.getResponse().getContentAsString();

		return jsonString;
	}

}