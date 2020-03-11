package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.common.MemberPasswordEncoder;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.repository.MemberRepository;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberListOutputVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

import lombok.RequiredArgsConstructor;

/**
 * 네이버 대중교통 내재화 사용자관리 서비스
 * @author westwind
 *
 */
@Service
@RequiredArgsConstructor
public class MemberService {

	@Autowired
	private OutputFmtUtil outputFmtUtil ;

	@Autowired
	private CommonService commonService ;

	@Autowired
	private MemberRepository memberRepository ;

	@Autowired
	private MemberPasswordEncoder memberPasswordEncoder;


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

		if(result>0) {
			resultMap.put("duplicate", true);
		} else {
			resultMap.put("duplicate", false);
		}

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap) ;

		return commonResult;

	}


	/**
	 * 회원 비밀번호 검증
	 * @param memberSearchVo - 회원 검색조건
     * @param userPw - 현재 회원 비밀번호
     * return 
	 */
	public CommonResult verifyPassword(MemberSearchVo memberSearchVo, String userPw) {

		MemberOutputVo memberOutputVo = this.getMemberData(memberSearchVo);
		HashMap<String, Boolean> resultMap = new HashMap<String, Boolean>();

		if(memberPasswordEncoder.matches(userPw, memberOutputVo.getUserPw())){
			resultMap.put("verify", true);
		} else {
			resultMap.put("verify", false);
		}

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap) ;

		return commonResult;

	}
	
	
	/**
	 * 회원 데이터를 가져온다.
	 * @param memberSearchVo - 회원 검색조건
	 * @return
	 */
	public MemberOutputVo getMemberData(MemberSearchVo memberSearchVo) {
		
		// 데이터 조회
		MemberOutputVo memberOutputVo = memberRepository.selectMember(memberSearchVo) ;
		
		return memberOutputVo ;
		
	}


	/**
	 * 회원 정보를 수정한다
	 * @param memberInputVo - 입력값
	 */
	public void updateMember(MemberInputVo memberInputVo) {
		
		if(StringUtils.isNotEmpty(memberInputVo.getUserPw())) {
			memberInputVo.setUserPw(memberPasswordEncoder.encode(memberInputVo.getUserPw()));
		}

		memberRepository.updateMember(memberInputVo);
	}


	/**
	 * 회원 정보를 삭제한다
	 * @param memberSearchVo - 회원 검색조건
	 */
	public void deleteMember(MemberSearchVo memberSearchVo) {
		
		memberRepository.deleteMember(memberSearchVo);
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
		int totalListCnt = memberRepository.selectMemberListTotalCnt(memberSearchVo) ;


		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalListCnt, memberSearchVo.getPageNo(), memberSearchVo.getListSize()) ;

		// 목록 조회 페이징 정보 set
		memberSearchVo.setStartPageLimit(pagingVo.getStartPageLimit());
		memberSearchVo.setEndPageLimit(pagingVo.getEndPageLimit());


		// 목록 조회
		List<MemberListOutputVo> memberListOutputVoList = memberRepository.selectMemberList(memberSearchVo) ;


		/**
		 * 2. 검색 폼 데이터 구조 생성
		 */

		// 커스텀 검색조건 데이터 구조 생성
		String tableName = "tb_z_svc_member" ;
		List<SchemaVo> schemaVoList = commonService.getTableSchema(tableName) ;

		// 사용하고자 하는 컬럼 목록
		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("user_id") ;
		usableColumnNameList.add("user_name") ;
		usableColumnNameList.add("company") ;
		usableColumnNameList.add("reg_date") ;


		// 컬럼목록 재정의
		schemaVoList = outputFmtUtil.refineSchemaVo(schemaVoList, usableColumnNameList) ;


		// 검색 폼 데이터 구조
		List<CommonSchema> schemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, null) ;



		/**
		 * 3. 공통 출력포맷 생성
		 */
		CommonResult commonResult = outputFmtUtil.setCommonListFmt(schemaList, pagingVo, memberListOutputVoList) ;

		return commonResult ;
	}


	/**
	 * 회원 데이터 입/출력 구조를 가져온다
	 * @return
	 */
	public List<CommonSchema> selectMemberSchema() {

		ArrayList<String> ignoreColumnNameList = new ArrayList<>();
		ignoreColumnNameList.add("upd_date") ;
		ignoreColumnNameList.add("reg_date") ;

		List<SchemaVo> columnList = memberRepository.selectMemberSchema() ;

		// 재정의
		columnList = outputFmtUtil.refineSchemaVoIngnoreColumns(columnList, ignoreColumnNameList) ;

		List<CommonSchema> schemaList = outputFmtUtil.makeCommonSchema(columnList) ;

		return schemaList ;
	}


}
