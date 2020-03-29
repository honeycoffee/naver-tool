package com.naver.pubtrans.itn.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.MemberUtil;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.service.NoticeService;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.fare.FareTaskVo;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeInputVo;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeParameterVo;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeSearchVo;

/**
 * 네이버 대중교통 DB 내재화 공지사항 컨트롤러
 * 
 * @author westwind
 *
 */
@Slf4j
@RestController
public class FareController {

	private final NoticeService noticeService;

	private final OutputFmtUtil outputFmtUtil;

	@Autowired
	FareController(NoticeService noticeService, OutputFmtUtil outputFmtUtil) {
		this.outputFmtUtil = outputFmtUtil;
		this.noticeService = noticeService;
	}

	/**
	 * 공지사항을 등록한다.
	 * <pre>
	 * Valid를 이용하여 유효성 검사를 진행한다
	 * </pre>
	 * @param noticeInputVo - 공지사항 입력값
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/v1/ntool/api/notice")
	public CommonOutput insertNotice(@RequestBody @Valid NoticeInputVo noticeInputVo)
		throws Exception {
		
		FareTaskVo vo = new FareTaskVo();
		
		// 공지사항 등록 서비스
		CommonResult commonReuslt = noticeService.insertNotice(noticeInputVo);

		return new CommonOutput(commonReuslt);

	}

}
