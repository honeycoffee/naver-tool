package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberAuthorityOutputVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

/**
 * 네이버 대중교통 내재화 사용자관리 Repository
 * @author westwind
 *
 */
@Repository
public interface MemberRepository {

	/**
	 * 중복 ID 확인
	 * @param userId - 회원 ID
	 * @return
	 * @throws DataAccessException
	 */
	int checkDuplicate(String userId) throws DataAccessException;

	/**
	 * 회원 정보를 등록한다
	 * @param memberInputVo - 회원 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	void insertMember(MemberInputVo memberInputVo) throws DataAccessException;

	/**
	 * 회원 정보를 수정한다
	 * @param memberInputVo - 회원 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	int updateMember(MemberInputVo memberInputVo) throws DataAccessException;

	/**
	 * 회원 정보를 삭제한다
	 * @param memberSearchVo - 회원 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	int deleteMember(MemberSearchVo memberSearchVo) throws DataAccessException;

	/**
	 * 테스트 회원 정보를 삭제한다
	 * @param memberSearchVo - 회원 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	int deleteTestMember(MemberSearchVo memberSearchVo) throws DataAccessException;

	/**
	 * 테스트 회원 권한 정보를 삭제한다
	 * @param memberSearchVo - 회원 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	int deleteTestMemberAuthority(MemberSearchVo memberSearchVo) throws DataAccessException;

	/**
	 * 회원 정보를 가져온다.
	 * @param memberSearchVo - 회원 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	MemberOutputVo getMember(MemberSearchVo memberSearchVo) throws DataAccessException;

	/**
	 * 회원 목록 수를 가져온다.
	 * @param memberSearchVo - 회원 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	int getMemberListTotalCnt(MemberSearchVo memberSearchVo) throws DataAccessException;

	/**
	 * 회원 목록을 가져온다.
	 * @param memberSearchVo - 회원 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	List<MemberOutputVo> selectMemberList(MemberSearchVo memberSearchVo) throws DataAccessException;

	/**
	 * 회원의 권한 목록을 가져온다.
	 * @param memberSearchVo - 회원 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	List<MemberAuthorityOutputVo> selectMemberAuthorityList(MemberSearchVo memberSearchVo) throws DataAccessException;

	/**
	 * 회원 권한을 등록한다
	 * @param memberInputVo - 회원 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	void insertMemberAuthority(MemberInputVo memberInputVo) throws DataAccessException;

	/**
	 * 회원 권한을 수정한다
	 * @param memberInputVo - 회원 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	int updateMemberAuthority(MemberInputVo memberInputVo) throws DataAccessException;

}
