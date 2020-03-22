package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;

/**
 * 버스 노선 Repository
 * @author adtec10
 *
 */
@Repository
public interface BusRouteRepository {

	/**
	 * 버스 노선 목록 수 가져오기
	 * @param busRouteSearchVo - 버스노선 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	int getBusRouteListTotalCnt(BusRouteSearchVo busRouteSearchVo) throws DataAccessException;

	/**
	 * 버스 노선 목록 가져오기
	 * @param busRouteSearchVo - 버스노선 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	List<BusRouteListOutputVo> selectBusRouteList(BusRouteSearchVo busRouteSearchVo) throws DataAccessException;
}
