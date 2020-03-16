package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.notice.input.NoticeInputVo;
import com.naver.pubtrans.itn.api.vo.notice.input.NoticeSearchVo;
import com.naver.pubtrans.itn.api.vo.notice.output.NoticeOutputVo;

/**
 * 네이버 대중교통 내재화 공지사항 관리 Repository
 * @author westwind
 *
 */
@Repository
public interface NoticeRepository {

	/**
	 * 공지사항을 등록한다.
	 * @param noticeInputVo - 공지사항 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	int insertNotice(NoticeInputVo noticeInputVo) throws DataAccessException;

	/**
	 * 공지사항을 수정한다.
	 * @param noticeInputVo - 공지사항 입력 값
	 * @return
	 * @throws DataAccessException
	 */
	int updateNotice(NoticeInputVo noticeInputVo) throws DataAccessException;

	/**
	 * 공지사항을 삭제한다
	 * @param noticeSearchVo - 공지사항 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	int deleteNotice(NoticeSearchVo noticeSearchVo) throws DataAccessException;

	/**
	 * 공지사항을 가져온다.
	 * @param noticeSearchVo - 공지사항 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	NoticeOutputVo getNotice(NoticeSearchVo noticeSearchVo) throws DataAccessException;

	/**
	 * 공지사항 조회수에 1을 더한다.
	 * @param noticeSearchVo - 공지사항 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	void plusReadCnt(NoticeSearchVo noticeSearchVo) throws DataAccessException;

	/**
	 * 공지사항 목록 수를 가져온다.
	 * @param noticeSearchVo - 공지사항 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	int getNoticeListTotalCnt(NoticeSearchVo noticeSearchVo) throws DataAccessException;

	/**
	 * 공지사항 목록을 가져온다.
	 * @param noticeSearchVo - 공지사항 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	List<NoticeOutputVo> selectNoticeList(NoticeSearchVo noticeSearchVo) throws DataAccessException;

}
