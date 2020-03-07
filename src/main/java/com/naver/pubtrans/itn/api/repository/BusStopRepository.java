package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopListOutputVo;

/**
 * 버스정류장 Repository
 *
 * @author adtec10
 *
 */
@Repository
public interface BusStopRepository {

	/**
	 * 정류장 목록 수 가져오기
	 * @param busStopSearchVo - 정류장 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	int selectBusStopListTotalCnt(BusStopSearchVo busStopSearchVo) throws DataAccessException ;

	/**
	 * 정류장 목록 가져오기
	 * @param busStopSearchVo - 정류장 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	List<BusStopListOutputVo> selectBusStopList(BusStopSearchVo busStopSearchVo) throws DataAccessException ;


}
