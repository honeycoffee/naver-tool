package com.naver.pubtrans.itn.api.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.auth.input.AuthInputVo;
import com.naver.pubtrans.itn.api.vo.auth.output.AuthOutputVo;

/**
 * 네이버 대중교통 내재화 인증관리 Repository
 * @author westwind
 *
 */
@Repository
public interface AuthRepository {
	
	public void insertMemberLoginLog(AuthInputVo authInputVo) throws DataAccessException ;

	public void insertMemberTokenInfo(AuthInputVo authInputVo) throws DataAccessException ;
	
	public AuthOutputVo selectMemberTokenInfo(AuthInputVo authInputVo) throws DataAccessException ;
	
	public void deleteMemberTokenInfo(AuthInputVo authInputVo) throws DataAccessException ;
	
}
