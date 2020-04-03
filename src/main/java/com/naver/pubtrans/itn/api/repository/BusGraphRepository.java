package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.bus.graph.BusStopGraphVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphTaskInputVo;

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

	/**
	 * 버스노선의 그래프 정보 목록을 가져온다
	 * @param routeId - 노선ID
	 * @return
	 * @throws DataAccessException
	 */
	List<BusStopGraphVo> selectBusRouteGraphList(int routeId) throws DataAccessException;

	/**
	 * 노선별 경유정류장 그래프 작업 목록 개수를 가져온다
	 * @param taskId - 작업ID
	 * @return
	 * @throws DataAccessException
	 */
	int geBusRouteGraphTaskListCnt(long taskId) throws DataAccessException;

	/**
	 * 노선별 경유정류장 그래프 작업 목록을 가져온다
	 * @param taskId
	 * @return
	 * @throws DataAccessException
	 */
	List<BusStopGraphVo> selectBusRouteGraphTaskList(long taskId) throws DataAccessException;

	/**
	 * 그래프 작업정보를 추가한다
	 * @param busStopGraphTaskInputVo - 그래프 정보
	 * @throws DataAccessException
	 */
	void insertBusStopGraphTask(BusStopGraphTaskInputVo busStopGraphTaskInputVo) throws DataAccessException;
}
