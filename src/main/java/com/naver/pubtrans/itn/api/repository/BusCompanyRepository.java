package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.bus.company.BusCompanyVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanySearchVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanyTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.company.output.BusCompanyListOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.company.output.BusCompanyTaskOutputVo;

/**
 * 버스 운수사 Repository
 *
 * @author westwind
 *
 */
@Repository
public interface BusCompanyRepository {

	/**
	 * 버스 운수사 목록 수 가져오기
	 * @param busCompanySearchVo - 버스 운수사 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	int getBusCompanyListTotalCnt(BusCompanySearchVo busCompanySearchVo) throws DataAccessException;

	/**
	 * 버스 운수사 목록 가져오기
	 * @param busCompanySearchVo - 버스 운수사 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	List<BusCompanyListOutputVo> selectBusCompanyList(BusCompanySearchVo busCompanySearchVo) throws DataAccessException;

	/**
	 * 버스 운수사 정보 가져오기
	 * @param companyId - 운수사 ID
	 * @return
	 * @throws DataAccessException
	 */
	BusCompanyVo getBusCompany(int companyId) throws DataAccessException;

	/**
	 * 버스 운수사 작업 정보 가져오기
	 * @param taskId - 작업ID
	 * @return
	 * @throws DataAccessException
	 */
	BusCompanyTaskOutputVo getBusCompanyTask(long taskId) throws DataAccessException;

	/**
	 * 버스 운수사 작업 정보 저장
	 * @param busCompanyTaskInputVo - 운수사 작업정보
	 * @return
	 * @throws DataAccessException
	 */
	void insertBusCompanyTask(BusCompanyTaskInputVo busCompanyTaskInputVo) throws DataAccessException;

	/**
	 * 버스 운수사 작업 정보 업데이트
	 * @param busCompanyTaskInputVo - 운수사 작업정보
	 * @return
	 * @throws DataAccessException
	 */
	int updateBusCompanyTask(BusCompanyTaskInputVo busCompanyTaskInputVo) throws DataAccessException;

}
