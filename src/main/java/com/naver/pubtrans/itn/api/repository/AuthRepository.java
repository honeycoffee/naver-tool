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

	/**
	 * 회원 로그인 기록을 등록한다.
	 * @param authInputVo - 인증 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	void insertMemberLoginLog(AuthInputVo authInputVo) throws DataAccessException;

	/**
	 * 회원 토큰 정보를 등록한다.
	 * @param authInputVo - 인증 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	void insertMemberTokenInfo(AuthInputVo authInputVo) throws DataAccessException;

	/**
	 * 회원 토큰 정보를 가져온다.
	 * @param authInputVo - 인증 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	AuthOutputVo getMemberTokenInfo(AuthInputVo authInputVo) throws DataAccessException;

	/**
	 * 회원 토큰 정보를 삭제한다.
	 * @param authInputVo - 인증 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	void deleteMemberTokenInfo(AuthInputVo authInputVo) throws DataAccessException;

}
