package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.bus.graph.BusStopGraphVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphSearchVo;

/**
 * 버스 그래프 Repository
 * @author adtec10
 *
 */
@Repository
public interface BusGraphRepository {

	/**
	 * 버스정류장간의 그래프 정보 목록을 가져온다
	 * @param busStopGraphSearchVoList - 출/도착 버스정류장 검색정보 목록
	 * @return
	 * @throws DataAccessException
	 */
	List<BusStopGraphVo> selectBusStopGraphList(List<BusStopGraphSearchVo> busStopGraphSearchVoList) throws DataAccessException;
}
