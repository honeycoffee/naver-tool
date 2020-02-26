package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.PasswordEncoding;
import com.naver.pubtrans.itn.api.dao.MemberDao;
import com.naver.pubtrans.itn.api.vo.common.StructureVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonStructure;
import com.naver.pubtrans.itn.api.vo.member.MemberVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	@Autowired
	private OutputFmtUtil outputFmtUtil ;

	@Autowired
	private MemberDao memberDao ; 
	
	@Autowired
	private PasswordEncoding passwordEncoding; 
	
	
	/**
	 * 회원 데이터 입력/수정
	 * @param memberInputVo
	 * @param type
	 */
	public void setMember(MemberInputVo memberInputVo, String type) {
		
		if("ins".equals(type)) {
			memberInputVo.setUserPw(passwordEncoding.encode(memberInputVo.getUserPw()));
			
			memberDao.insMember(memberInputVo);
		}
		
	}
	
	
	/**
	 * ID 중복 체크
	 * @param memberVo
	 */
	public CommonResult checkDuplicate(String userId) {
		
		int result = memberDao.checkDuplicate(userId);
		HashMap<String, Boolean> resultMap = new HashMap<String, Boolean>(); 
		
		if(result>0) {
			resultMap.put("duplicate", true);
		} else {
			resultMap.put("duplicate", false);
		}
		
		CommonResult cmnRs = new CommonResult(resultMap) ;
		
		return cmnRs;
		
	}
	
	
	/**
	 * 회원 데이터 구조 가져오기
	 * @return
	 */
	public CommonResult getMemberDataStructure() {
		
		ArrayList<String> ignoreColumns = new ArrayList<>();
		ignoreColumns.add("upd_date") ;
		ignoreColumns.add("reg_date") ;
		
		List<StructureVo> columnList = memberDao.getMemberSchema() ;

		List<CommonStructure> schemaList = outputFmtUtil.setCommonStructure(columnList, ignoreColumns) ; 
		
		// 문서 공통 포맷으로 포맷
		CommonResult cmnRs = outputFmtUtil.setCommonDocFmt(schemaList) ;
		
		return cmnRs ;
	}
	
	
}
