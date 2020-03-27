package com.naver.pubtrans.itn.api.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.auth.input.AuthenticationInputVo;
import com.naver.pubtrans.itn.api.vo.auth.output.AuthenticationOutputVo;

/**
 * 네이버 대중교통 내재화 인증관리 Repository
 * @authenticationor westwind
 *
 */
@Repository
public interface AuthenticationRepository {

	/**
	 * 회원 로그인 기록을 등록한다.
	 * @param authenticationInputVo - 인증 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	void insertMemberLoginLog(AuthenticationInputVo authenticationInputVo) throws DataAccessException;

	/**
	 * 회원 토큰 정보를 등록한다.
	 * @param authenticationInputVo - 인증 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	void insertMemberTokenInfo(AuthenticationInputVo authenticationInputVo) throws DataAccessException;

	/**
	 * 회원 토큰 정보를 가져온다.
	 * @param authenticationInputVo - 인증 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	AuthenticationOutputVo getMemberTokenInfo(AuthenticationInputVo authenticationInputVo) throws DataAccessException;

	/**
	 * 회원 토큰 정보를 삭제한다.
	 * @param authenticationInputVo - 인증 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	void deleteMemberTokenInfo(AuthenticationInputVo authenticationInputVo) throws DataAccessException;

}
