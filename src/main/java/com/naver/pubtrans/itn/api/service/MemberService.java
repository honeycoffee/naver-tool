package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.MemberPasswordEncoder;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CodeType;
import com.naver.pubtrans.itn.api.repository.MemberRepository;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
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
	 * 회원 목록을 가져온다
	 * @param memberSearchVo - 목록 검색 조건
	 * @return
	 * @throws Exception
	 */
//	public CommonResult getMemberList(BusStopSearchVo busStopSearchVo) throws Exception {
//
//		/**
//		 * 1. 페이지 목록 조회
//		 */
//
//		// 전체 목록 수 가져오기
//		int totalListCnt = busStopRepository.selectBusStopListTotalCnt(busStopSearchVo) ;
//
//
//		// 페이징 정보
//		PagingVo pagingVo = new PagingVo(totalListCnt, busStopSearchVo.getPageNo(), busStopSearchVo.getListSize()) ;
//
//		// 목록 조회 페이징 정보 set
//		busStopSearchVo.setStartPageLimit(pagingVo.getStartPageLimit());
//		busStopSearchVo.setEndPageLimit(pagingVo.getEndPageLimit());
//
//
//		// 목록 조회
//		List<BusStopListOutputVo> busStopListOutputVoList = busStopRepository.selectBusStopList(busStopSearchVo) ;
//
//
//		/**
//		 * 2. 검색 폼 데이터 구조 생성
//		 */
//
//		// 커스텀 검색조건 데이터 구조 생성
//		String tableName = "tb_stops" ;
//		List<SchemaVo> schemaVoList = commonService.getTableSchema(tableName) ;
//
//		// 사용하고자 하는 컬럼 목록
//		ArrayList<String> usableColumnNameList = new ArrayList<>();
//		usableColumnNameList.add("stop_id") ;
//		usableColumnNameList.add("stop_name") ;
//		usableColumnNameList.add("citycode") ;
//
//
//		// 컬럼목록 재정의
//		schemaVoList = outputFmtUtil.refineSchemaVo(schemaVoList, usableColumnNameList) ;
//
//
//		// 도시코드 목록
//		HashMap<String, List<FieldValue>> valuesMap = new HashMap<>() ;
//		valuesMap.put("citycode", commonService.getCommonCode(CodeType.CITYCODE.getCodeName())) ;
//
//
//		// 검색 폼 데이터 구조
//		List<CommonSchema> schemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, valuesMap) ;
//
//
//
//		/**
//		 * 3. 공통 출력포맷 생성
//		 */
//		CommonResult commonResult = outputFmtUtil.setCommonListFmt(schemaList, pagingVo, busStopListOutputVoList) ;
//
//		return commonResult ;
//	}


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
