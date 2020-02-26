package com.naver.pubtrans.itn.api.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.common.StructureVo;
import com.naver.pubtrans.itn.api.vo.member.MemberVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;

@Repository
public interface MemberDao {
	
	public int checkDuplicate(String userId) throws DataAccessException ;
	
	public void insMember(MemberInputVo memberInputVo) throws DataAccessException ;
	
	public List<StructureVo> getMemberSchema() throws DataAccessException ;
	
	
	
}
