package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.common.BusProviderVo;
import com.naver.pubtrans.itn.api.vo.common.CityCodeVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;

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
	List<SchemaVo> selectTableSchema(String tableName) throws DataAccessException ;


	/**
	 * 전체 도시코드 목록을 가져온다
	 * @return
	 * @throws DataAccessException
	 */
	List<CityCodeVo> selectCityCodeList() throws DataAccessException ;


	/**
	 * 전체 BIS 대상지역 목록을 가져온다
	 * @return
	 * @throws DataAccessException
	 */
	List<BusProviderVo> selectBusProviderList() throws DataAccessException ;



}
