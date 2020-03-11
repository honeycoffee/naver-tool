package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberListOutputVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

/**
 * 네이버 대중교통 내재화 사용자관리 Repository
 * @author westwind
 *
 */
@Repository
public interface NoticeRepository {

	int checkDuplicate(String userId) throws DataAccessException;

	void insertMember(MemberInputVo memberInputVo) throws DataAccessException;

	void updateMember(MemberInputVo memberInputVo) throws DataAccessException;

	void deleteMember(MemberSearchVo memberSearchVo) throws DataAccessException;

	MemberOutputVo selectMember(MemberSearchVo memberSearchVo) throws DataAccessException;

	int selectMemberListTotalCnt(MemberSearchVo memberSearchVo) throws DataAccessException;

	List<MemberListOutputVo> selectMemberList(MemberSearchVo memberSearchVo) throws DataAccessException;

	List<SchemaVo> selectMemberSchema() throws DataAccessException;

}
