package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.MemberPasswordEncoder;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.repository.MemberRepository;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
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

		CommonResult commonResult = new CommonResult(resultMap) ;

		return commonResult;

	}
	
	
	/**
	 * 회원 데이터를 가져온다.
	 * @param userId - 체크할 회원 ID
	 * @return
	 */
	public MemberOutputVo getMemberData(MemberSearchVo memberSearchVo) {
		
		// 데이터 조회
		MemberOutputVo memberOutputVo = memberRepository.selectMember(memberSearchVo) ;
		
		return memberOutputVo ;
		
	}


	/**
	 * 회원 데이터 입/출력 구조를 가져온다
	 * @return
	 */
	public CommonResult selectMemberSchema() {

		ArrayList<String> ignoreColumnNameList = new ArrayList<>();
		ignoreColumnNameList.add("upd_date") ;
		ignoreColumnNameList.add("reg_date") ;

		List<SchemaVo> columnList = memberRepository.selectMemberSchema() ;

		// 재정의
		columnList = outputFmtUtil.refineSchemaVoIngnoreColumns(columnList, ignoreColumnNameList) ;

		List<CommonSchema> schemaList = outputFmtUtil.makeCommonSchema(columnList) ;

		// 문서 공통 포맷으로 포맷
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(schemaList) ;

		return commonResult ;
	}


}
