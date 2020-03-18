package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.MemberPasswordEncoder;
import com.naver.pubtrans.itn.api.common.MemberUtil;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.MemberRepository;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberParameterVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

/**
 * 네이버 대중교통 내재화 사용자관리 서비스
 * @author westwind
 *
 */
@Service
public class MemberService {

	private final OutputFmtUtil outputFmtUtil;

	private final CommonService commonService;

	private final MemberUtil memberUtil;

	private final MemberRepository memberRepository;

	private final MemberPasswordEncoder memberPasswordEncoder;

	@Autowired
	MemberService(OutputFmtUtil outputFmtUtil, CommonService commonService, MemberUtil memberUtil,
		MemberRepository memberRepository, MemberPasswordEncoder memberPasswordEncoder) {
		this.outputFmtUtil = outputFmtUtil;
		this.commonService = commonService;
		this.memberUtil = memberUtil;
		this.memberRepository = memberRepository;
		this.memberPasswordEncoder = memberPasswordEncoder;
	}

	/**
	 * 회원 정보를 등록한다
	 * @param memberInputVo - 입력값
	 */
	public void insertMember(MemberInputVo memberInputVo) {
		memberInputVo.setUserPw(memberPasswordEncoder.encode(memberInputVo.getUserPw()));

		memberRepository.insertMember(memberInputVo);
	}

	/**
	 * ID 중복 체크
	 * @param userId - 체크할 회원 ID
	 */
	public CommonResult checkDuplicate(String userId) {

		int result = memberRepository.checkDuplicate(userId);
		HashMap<String, Boolean> resultMap = new HashMap<String, Boolean>();

		if (result > 0) {
			resultMap.put("duplicate", true);
		} else {
			resultMap.put("duplicate", false);
		}

		CommonResult commonResult = new CommonResult(resultMap);

		return commonResult;

	}

	/**
	 * 회원 비밀번호 검증
	 * @param memberParameterVo - 회원 파라미터 Vo
	 * return 
	 */
	public CommonResult verifyPassword(MemberParameterVo memberParameterVo) throws Exception {

		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId(memberParameterVo.getUserId());

		MemberOutputVo memberOutputVo = this.getMember(memberSearchVo);

		if (memberOutputVo == null) {
			throw new ApiException(ResultCode.MEMBER_DATA_NULL.getApiErrorCode(),
				ResultCode.MEMBER_DATA_NULL.getDisplayMessage());
		}

		HashMap<String, Boolean> resultMap = new HashMap<String, Boolean>();

		if (memberPasswordEncoder.matches(memberParameterVo.getUserPw(), memberOutputVo.getUserPw())) {
			resultMap.put("verify", true);
		} else {
			resultMap.put("verify", false);
		}

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		return commonResult;

	}

	/**
	 * 회원 데이터를 가져온다.
	 * @param memberSearchVo - 회원 검색조건
	 * @return
	 */
	public MemberOutputVo getMember(MemberSearchVo memberSearchVo) throws Exception {

		// 데이터 조회
		MemberOutputVo memberOutputVo = memberRepository.getMember(memberSearchVo);

		if (memberOutputVo == null) {
			throw new ApiException(ResultCode.MEMBER_DATA_NULL.getApiErrorCode(),
				ResultCode.MEMBER_DATA_NULL.getDisplayMessage());
		}

		return memberOutputVo;

	}

	/**
	 * 자신의 정보를 조회한다
	 * @param accessToken - 입력값
	 */
	public CommonResult getMe(String accessToken) throws Exception {

		String userId = memberUtil.getUserIdFromToken(accessToken);

		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId(userId);

		CommonResult commonResult = this.getMemberDataWithSchema(memberSearchVo);

		return commonResult;

	}

	/**
	 * 자신의 정보를 수정한다
	 * @param memberInputVo - 입력값
	 */
	public void updateMe(MemberInputVo memberInputVo, String accessToken) throws Exception {

		String userId = memberUtil.getUserIdFromToken(accessToken);

		if (!memberInputVo.getUserId().equals(userId)) {
			throw new ApiException(ResultCode.MEMBER_TOKEN_NOT_MATCH.getApiErrorCode(),
				ResultCode.MEMBER_TOKEN_NOT_MATCH.getDisplayMessage());
		}

		this.updateMember(memberInputVo);

	}

	/**
	 * 회원 정보를 수정한다
	 * @param memberInputVo - 입력값
	 * @throws Exception 
	 */
	public void updateMember(MemberInputVo memberInputVo) throws Exception {

		if (StringUtils.isNotEmpty(memberInputVo.getUserPw())) {
			memberInputVo.setUserPw(memberPasswordEncoder.encode(memberInputVo.getUserPw()));
		}

		int updateMemberCnt = memberRepository.updateMember(memberInputVo);

		// 저장 오류 처리
		if (updateMemberCnt == 0) {
			throw new ApiException(ResultCode.UPDATE_FAIL.getApiErrorCode(), ResultCode.UPDATE_FAIL.getDisplayMessage());
		}
	}

	/**
	 * 회원 정보를 삭제한다
	 * @param memberSearchVo - 회원 검색조건
	 */
	public void deleteMember(MemberSearchVo memberSearchVo) throws Exception {

		int deleteMemberCnt = memberRepository.deleteMember(memberSearchVo);

		// 삭제 오류 처리
		if (deleteMemberCnt == 0) {
			throw new ApiException(ResultCode.DELETE_FAIL.getApiErrorCode(),
				ResultCode.DELETE_FAIL.getDisplayMessage());
		}

	}

	/**
	 * 테스트 회원 정보를 삭제한다
	 * @param memberSearchVo - 회원 검색조건
	 */
	public void deleteTestMember(MemberSearchVo memberSearchVo) throws Exception {

		memberRepository.deleteTestMember(memberSearchVo);

	}

	/**
	 * 회원 목록을 가져온다
	 * @param memberSearchVo - 회원 검색조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult selectMemberList(MemberSearchVo memberSearchVo) throws Exception {

		/**
		 * 1. 페이지 목록 조회
		 */

		// 전체 목록 수 가져오기
		int totalListCnt = memberRepository.getMemberListTotalCnt(memberSearchVo);

		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalListCnt, memberSearchVo.getPageNo(), memberSearchVo.getListSize());

		// 목록 조회 페이징 정보 set
		memberSearchVo.setStartPageLimit(pagingVo.getStartPageLimit());
		memberSearchVo.setEndPageLimit(pagingVo.getEndPageLimit());

		// 목록 조회
		List<MemberOutputVo> memberOutputVoList = memberRepository.selectMemberList(memberSearchVo);

		/**
		 * 2. 검색 폼 데이터 구조 생성
		 */

		// 사용하고자 하는 컬럼 목록
		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("user_id");
		usableColumnNameList.add("user_name");
		usableColumnNameList.add("company");

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(
			PubTransTable.TB_Z_SVC_MEMBER.getName(),
			CommonConstant.USABLE_COLUMN, usableColumnNameList);

		/**
		 * 3. 공통 출력포맷 생성
		 */
		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, memberOutputVoList);

		return commonResult;
	}

	/**
	 * 회원 데이터 입/출력 구조를 가져온다
	 * @return
	 * @throws Exception 
	 */
	public List<CommonSchema> selectMemberSchema() throws Exception {

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService
			.selectCommonSchemaList(PubTransTable.TB_Z_SVC_MEMBER.getName(), null, null);

		return commonSchemaList;
	}

	/**
	 * 회원 데이터 및 데이터 입/출력 구조를 가져온다
	 * @param memberSearchVo - 회원 검색조건
	 * @return
	 * @throws Exception 
	 */
	public CommonResult getMemberDataWithSchema(MemberSearchVo memberSearchVo) throws Exception {

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(this.selectMemberSchema(),
			this.getMember(memberSearchVo));

		return commonResult;
	}

}
