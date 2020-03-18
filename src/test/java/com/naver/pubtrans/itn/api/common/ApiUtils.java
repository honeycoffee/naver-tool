package com.naver.pubtrans.itn.api.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.vo.auth.LoginVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeInputVo;

/**
 * 테스트용 공통 유틸 Class
 * @author westwind
 *
 */
@Component
public class ApiUtils {

	private final MockMvc mockMvc;

	private final ObjectMapper objectMapper;

	@Autowired
	ApiUtils(MockMvc mockMvc, ObjectMapper objectMapper) {
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

		return tokenMap;
	}

	/**
	 * 공지사항 seq가 필요한 test를 위해 공지사항 등록 후 seq 전달
	 * @return
	 * @throws Exception
	 */
	public int getNoticeSeq() throws Exception {

		NoticeInputVo noticeInputVo = new NoticeInputVo();
		noticeInputVo.setTitle("test notice");
		noticeInputVo.setContent("test content");
		noticeInputVo.setImportantYn("Y");

		MvcResult mvcResult = mockMvc.perform(post("/v1/ntool/api/notice")
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
	
}