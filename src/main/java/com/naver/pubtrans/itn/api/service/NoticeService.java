package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.MemberUtil;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.NoticeRepository;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeInputVo;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeSearchVo;
import com.naver.pubtrans.itn.api.vo.notice.output.NoticeOutputVo;

/**
 * 네이버 대중교통 내재화 공지사항 서비스
 * 
 * @author westwind
 *
 */
@Service
public class NoticeService {

	private final OutputFmtUtil outputFmtUtil;

	private final CommonService commonService;

	private final NoticeRepository noticeRepository;

	private final JwtAdapter jwtAdapter;

	@Autowired
	NoticeService(OutputFmtUtil outputFmtUtil, CommonService commonService, NoticeRepository noticeRepository,
		JwtAdapter jwtAdapter) {
		this.outputFmtUtil = outputFmtUtil;
		this.commonService = commonService;
		this.noticeRepository = noticeRepository;
		this.jwtAdapter = jwtAdapter;
	}

	/**
	 * 공지사항을 등록한다
	 * 
	 * @param noticeInputVo - 공지사항 입력값
	 * @throws Exception 
	 */
	public CommonResult insertNotice(NoticeInputVo noticeInputVo) throws Exception {

		MemberOutputVo memberOutputVo = MemberUtil.getMemberFromToken();

		noticeInputVo.setRegUserId(memberOutputVo.getUserId());
		noticeInputVo.setRegUserName(memberOutputVo.getUserName());

		noticeRepository.insertNotice(noticeInputVo);

		NoticeOutputVo noticeOutputVo = new NoticeOutputVo();
		noticeOutputVo.setSeq(noticeInputVo.getSeq());

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(noticeOutputVo);

		return commonResult;
	}

	/**
	 * 공지사항을 가져온다.
	 * 
	 * @param noticeSearchVo - 공지사항 검색조건
	 * @return
	 * @throws Exception 
	 */
	public CommonResult getNotice(NoticeSearchVo noticeSearchVo) throws Exception {

		// 데이터 조회
		NoticeOutputVo noticeOutputVo = noticeRepository.getNotice(noticeSearchVo);

		if (noticeOutputVo == null) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

		noticeRepository.plusReadCnt(noticeSearchVo);

		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectNoticeSchema();

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, noticeOutputVo);

		return commonResult;

	}

	/**
	 * 공지사항을 수정한다
	 * 
	 * @param noticeInputVo - 공지사항 입력값
	 * @throws Exception 
	 */
	public void updateNotice(NoticeInputVo noticeInputVo) throws Exception {
		
		MemberOutputVo memberOutputVo = MemberUtil.getMemberFromToken();

		noticeInputVo.setRegUserId(memberOutputVo.getUserId());
		noticeInputVo.setRegUserName(memberOutputVo.getUserName());

		int updateNoticeCnt = noticeRepository.updateNotice(noticeInputVo);

		// 저장 오류 처리
		if (updateNoticeCnt == 0) {
			throw new ApiException(ResultCode.UPDATE_FAIL.getApiErrorCode(), ResultCode.UPDATE_FAIL.getDisplayMessage());
		}

	}

	/**
	 * 공지사항을 삭제한다
	 * 
	 * @param noticeSearchVo - 공지사항 검색조건
	 * @throws Exception 
	 */
	public void deleteNotice(NoticeSearchVo noticeSearchVo) throws Exception {

		MemberOutputVo memberOutputVo = MemberUtil.getMemberFromToken();

		if (StringUtils.isEmpty(memberOutputVo.getUserId())) {
			throw new ApiException(ResultCode.MEMBER_DATA_NULL.getApiErrorCode(),
				ResultCode.MEMBER_DATA_NULL.getDisplayMessage());
		}

		int deleteNoticeCnt = noticeRepository.deleteNotice(noticeSearchVo);

		// 삭제 오류 처리
		if (deleteNoticeCnt == 0) {
			throw new ApiException(ResultCode.DELETE_FAIL.getApiErrorCode(),
				ResultCode.DELETE_FAIL.getDisplayMessage());
		}

	}

	/**
	 * 공지사항 목록을 가져온다
	 * @param noticeSearchVo - 공지사항 검색조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult selectNoticeList(NoticeSearchVo noticeSearchVo) throws Exception {

		/**
		 * 1. 페이지 목록 조회
		 */

		// 전체 목록 수 가져오기
		int totalListCnt = noticeRepository.getNoticeListTotalCnt(noticeSearchVo);

		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalListCnt, noticeSearchVo.getPageNo(), noticeSearchVo.getListSize());

		// 목록 조회 페이징 정보 set
		noticeSearchVo.setStartPageLimit(pagingVo.getStartPageLimit());
		noticeSearchVo.setEndPageLimit(pagingVo.getEndPageLimit());

		// 목록 조회
		List<NoticeOutputVo> noticeOutputVoList = noticeRepository.selectNoticeList(noticeSearchVo);

		/**
		 * 2. 검색 폼 데이터 구조 생성
		 */

		// 사용하고자 하는 컬럼 목록
		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("title");
		usableColumnNameList.add("content");
		usableColumnNameList.add("important_yn");
		usableColumnNameList.add("reg_user_id");
		usableColumnNameList.add("reg_user_name");

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(
			PubTransTable.TB_Z_SVC_NOTICE.getName(),
			CommonConstant.USABLE_COLUMN, usableColumnNameList);

		/**
		 * 3. 공통 출력포맷 생성
		 */
		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, noticeOutputVoList);

		return commonResult;
	}

	/**
	 * 공지사항 데이터 입/출력 구조를 가져온다
	 * @return
	 * @throws Exception 
	 */
	public List<CommonSchema> selectNoticeSchema() throws Exception {

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService
			.selectCommonSchemaList(PubTransTable.TB_Z_SVC_NOTICE.getName(), null, null);

		return commonSchemaList;
	}

}
