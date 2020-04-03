package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.common.BusProviderVo;
import com.naver.pubtrans.itn.api.vo.common.BusRouteClassVo;
import com.naver.pubtrans.itn.api.vo.common.CityCodeVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.TransportVo;
import com.naver.pubtrans.itn.api.vo.member.AuthorityInfoVo;

/**
 * 공통 Repository
 * @author adtec10
 *
 */
@Repository
public interface CommonRepository {

	/**
	 * 테이블 구조 가져오기
	 * @param tableName - 조회할 테이블명
	 * @return
	 * @throws DataAccessException
	 */
	List<SchemaVo> selectTableSchema(String tableName) throws DataAccessException;

	/**
	 * 전체 도시코드 목록을 가져온다
	 * @return
	 * @throws DataAccessException
	 */
	List<CityCodeVo> selectCityCodeList() throws DataAccessException;

	/**
	 * 전체 BIS 대상지역 목록을 가져온다
	 * @return
	 * @throws DataAccessException
	 */
	List<BusProviderVo> selectBusProviderList() throws DataAccessException;

	/**
	 * 전체 버스노선 클래스 목록을 가져온다
	 * @return
	 * @throws DataAccessException
	 */
	List<BusRouteClassVo> selectBusRouteClassList() throws DataAccessException;

	/**
	 * 모든 권한 목록을 가져온다.
	 * @return
	 * @throws DataAccessException
	 */
	List<AuthorityInfoVo> selectAuthorityInfoList() throws DataAccessException;

	/**
	 * 전체 대중교통 구분 코드 목록을 가져온다.
	 * @return
	 * @throws DataAccessException
	 */
	List<TransportVo> selectTransportList() throws DataAccessException;
}
