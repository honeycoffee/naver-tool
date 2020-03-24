package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberAuthOutputVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

/**
 * 네이버 대중교통 내재화 사용자관리 서비스
 * @author westwind
 *
 */
@Service
public class MemberService implements UserDetailsService {

	private final OutputFmtUtil outputFmtUtil;

	private final CommonService commonService;

	private final MemberRepository memberRepository;

	private final MemberPasswordEncoder memberPasswordEncoder;

	@Autowired
	MemberService(OutputFmtUtil outputFmtUtil, CommonService commonService, 
		MemberRepository memberRepository,
		MemberPasswordEncoder memberPasswordEncoder) {
		this.outputFmtUtil = outputFmtUtil;
		this.commonService = commonService;
		this.memberRepository = memberRepository;
		this.memberPasswordEncoder = memberPasswordEncoder;
	}

	/**
	 * 회원 정보를 등록한다
	 * @param memberInputVo - 회원 입력 Vo
	 * @throws Exception
	 */
	public void insertMember(MemberInputVo memberInputVo) throws Exception {

		String userPw = memberInputVo.getUserPw();

		// 비밀번호 입력 값 검증
		if (StringUtils.isEmpty(userPw) || userPw.length() < CommonConstant.PASSWORD_MIN) {
			throw new ApiException(ResultCode.PASSWORD_NOT_VALID.getApiErrorCode(),
				ResultCode.PASSWORD_NOT_VALID.getDisplayMessage());
		}

		memberInputVo.setUserPw(memberPasswordEncoder.encode(memberInputVo.getUserPw()));
		memberInputVo.setAuthid(CommonConstant.ROLE_USER);

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
	 * @param memberInputVo - 회원 입력 Vo
	 * return 
	 * @throws Exception
	 */
	public void verifyPassword(MemberInputVo memberInputVo) throws Exception {

		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId(memberInputVo.getUserId());

		MemberOutputVo memberOutputVo = this.getMember(memberSearchVo);

		if (memberOutputVo == null) {
			throw new ApiException(ResultCode.MEMBER_DATA_NULL.getApiErrorCode(),
				ResultCode.MEMBER_DATA_NULL.getDisplayMessage());
		}

		// 현재 비밀번호와 DB에 저장된 인코딩된 비밀번호 검증
		if (StringUtils.isEmpty(memberInputVo.getCurrentUserPw())
			|| !memberPasswordEncoder.matches(memberInputVo.getCurrentUserPw(), memberOutputVo.getEncodedUserPw())) {
			throw new ApiException(ResultCode.PASSWORD_NOT_MATCH.getApiErrorCode(),
				ResultCode.PASSWORD_NOT_MATCH.getDisplayMessage());
		}

	}

	/**
	 * 회원 데이터를 가져온다.
	 * @param memberSearchVo - 회원 검색조건
	 * @return
	 * @throws Exception
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
	 * @param accessToken - API 호출 accessToken
	 * @throws Exception
	 */
	public CommonResult getMe() throws Exception {

		String userId = MemberUtil.getUserIdFromToken();

		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId(userId);

		CommonResult commonResult = this.getMemberDataWithSchema(memberSearchVo);

		return commonResult;

	}

	/**
	 * 자신의 정보를 수정한다
	 * @param memberInputVo - 회원 입력 Vo
	 * @param accessToken - API 호출 accessToken
	 * @throws Exception
	 */
	public void updateMe(MemberInputVo memberInputVo) throws Exception {

		String userId = MemberUtil.getUserIdFromToken();
		
		if (!memberInputVo.getUserId().equals(userId)) {
			throw new ApiException(ResultCode.MEMBER_TOKEN_NOT_MATCH.getApiErrorCode(),
				ResultCode.MEMBER_TOKEN_NOT_MATCH.getDisplayMessage());
		}

		this.verifyPassword(memberInputVo);

		this.updateMember(memberInputVo);

	}

	/**
	 * 회원 정보를 수정한다
	 * @param memberInputVo - 회원 입력 Vo
	 * @throws Exception 
	 */
	public void updateMember(MemberInputVo memberInputVo) throws Exception {

		if (StringUtils.isNotEmpty(memberInputVo.getUserPw())) {
			memberInputVo.setUserPw(memberPasswordEncoder.encode(memberInputVo.getUserPw()));
		}

		int updateMemberCnt = memberRepository.updateMember(memberInputVo);

		// 저장 오류 처리
		if (updateMemberCnt == 0) {
			throw new ApiException(ResultCode.UPDATE_FAIL.getApiErrorCode(),
				ResultCode.UPDATE_FAIL.getDisplayMessage());
		}
	}

	/**
	 * 회원 정보를 삭제한다
	 * @param memberSearchVo - 회원 검색조건
	 * @throws Exception
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
	 * @throws Exception
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

	/**
	 * 회원 권한 목록을 가져온다.
	 * @param memberSearchVo - 회원 검색조건
	 * @return
	 */
	public List<MemberAuthOutputVo> selectMemberAuthList(MemberSearchVo memberSearchVo) {

		List<MemberAuthOutputVo> memberAuthOutputVoList = memberRepository.selectMemberAuthList(memberSearchVo);

		return memberAuthOutputVoList;

	}

	/**
	 * 권한 ID만 있는 배열을 생성한다..
	 * @param memberSearchVo - 회원 검색조건
	 * @return
	 */
	public String[] getMemberAuthArray(MemberSearchVo memberSearchVo) {

		List<MemberAuthOutputVo> memberAuthOutputVoList = this.selectMemberAuthList(memberSearchVo);
		
		int cnt = 0;
		String[] memberAuthArray = new String[memberAuthOutputVoList.size()];
		
		for(MemberAuthOutputVo memberAuthOutputVo : memberAuthOutputVoList) {
			memberAuthArray[cnt++] = memberAuthOutputVo.getAuthId();
		}

		return memberAuthArray;

	}

	/**
	 * Spring Security 에서 권한 검증을 위한 정보를 가져온다.
	 * @param userId - 회원 ID
	 * @return
	 */
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId(userId);

		MemberOutputVo memberOutputVo = memberRepository.getMember(memberSearchVo);
		MemberAuthOutputVo returnMemberAuthOutputVo = new MemberAuthOutputVo();

		returnMemberAuthOutputVo.setUserId(memberOutputVo.getUserId());
		returnMemberAuthOutputVo.setUsername(memberOutputVo.getUserName());

		List<MemberAuthOutputVo> memberAuthOutputVoList = this.selectMemberAuthList(memberSearchVo);
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();

		for (MemberAuthOutputVo memberAuthOutputVo : memberAuthOutputVoList) {
			grantedAuthorityList.add(new SimpleGrantedAuthority(memberAuthOutputVo.getAuthId()));
		}

		returnMemberAuthOutputVo.setAuthorities(grantedAuthorityList);

		return returnMemberAuthOutputVo;

	}

}
