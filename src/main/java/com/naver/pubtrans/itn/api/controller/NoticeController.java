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

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.service.NoticeService;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
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
public class NoticeController {

	private final NoticeService noticeService;

	private final OutputFmtUtil outputFmtUtil;

	@Autowired
	NoticeController(NoticeService noticeService, OutputFmtUtil outputFmtUtil) {
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

		// 공지사항 등록 서비스
		CommonResult commonReuslt = noticeService.insertNotice(noticeInputVo);

		return new CommonOutput(commonReuslt);

	}

	/**
	 * 공지사항을 가져온다
	 * @param seq - 공지사항 ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/v1/ntool/api/notice/{seq}")
	public CommonOutput getNotice(@PathVariable int seq) throws Exception {

		NoticeSearchVo noticeSearchVo = new NoticeSearchVo();
		noticeSearchVo.setSeq(seq);

		// 공지사항 조회 서비스
		CommonResult commonResult = noticeService.getNotice(noticeSearchVo);

		return new CommonOutput(commonResult);

	}

	/**
	 * 공지사항을 수정한다.
	 * <pre>
	 * Valid를 이용하여 유효성 검사를 진행한다
	 * </pre>
	 * @param noticeInputVo - 공지사항 입력값
	 * @return
	 * @throws Exception
	 */
	@PutMapping(value = "/v1/ntool/api/notice")
	public CommonOutput updateNotice(@RequestBody @Valid NoticeInputVo noticeInputVo) throws Exception {

		// 공지사항 수정 서비스
		noticeService.updateNotice(noticeInputVo);

		return new CommonOutput();

	}

	/**
	 * 공지사항을 삭제한다.
	 * @param noticeParameterVo - 공지사항 파라미터 Vo
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping(value = "/v1/ntool/api/notice")
	public CommonOutput deleteNotice(@RequestBody @Valid NoticeParameterVo noticeParameterVo) throws Exception {

		NoticeSearchVo noticeSearchVo = new NoticeSearchVo();
		noticeSearchVo.setSeq(noticeParameterVo.getSeq());

		// 공지사항 삭제 서비스
		noticeService.deleteNotice(noticeSearchVo);

		return new CommonOutput();

	}

	/**
	 * 공지사항 목록 조회
	 * @param noticeSearchVo - 공지사항 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/notice")
	public CommonOutput listNotice(NoticeSearchVo noticeSearchVo) throws Exception {
		CommonResult commonResult = noticeService.selectNoticeList(noticeSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 공지사항 데이터 상세 구조를 조회한다
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/v1/ntool/api/schema/notice")
	public CommonOutput selectNoticeSchema() throws Exception {
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(noticeService.selectNoticeSchema());
		return new CommonOutput(commonResult);
	}

}
