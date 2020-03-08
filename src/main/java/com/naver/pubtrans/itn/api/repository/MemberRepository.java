package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.auth.input.AuthInputVo;
import com.naver.pubtrans.itn.api.vo.auth.output.AuthOutputVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

/**
 * 네이버 대중교통 내재화 사용자관리 Repository
 * @author westwind
 *
 */
@Repository
public interface MemberRepository {
	
	public int checkDuplicate(String userId) throws DataAccessException ;
	
	public void insertMember(MemberInputVo memberInputVo) throws DataAccessException ;
	
	public MemberOutputVo selectMember(MemberSearchVo searchVo) throws DataAccessException ;
	
	public List<SchemaVo> selectMemberSchema() throws DataAccessException ;
	
	public void insertMemberLoginLog(AuthInputVo authInputVo) throws DataAccessException ;

	public void insertMemberTokenInfo(AuthInputVo authInputVo) throws DataAccessException ;
	
	public AuthOutputVo selectMemberTokenInfo(AuthInputVo authInputVo) throws DataAccessException ;
	
	public void deleteMemberTokenInfo(AuthInputVo authInputVo) throws DataAccessException ;
	
}
