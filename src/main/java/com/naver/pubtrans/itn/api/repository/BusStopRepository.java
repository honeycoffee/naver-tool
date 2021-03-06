package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.bus.stop.BusRouteVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopMappingVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopTaskVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopTaskInputVo;
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
	int getBusStopListTotalCnt(BusStopSearchVo busStopSearchVo) throws DataAccessException;

	/**
	 * 정류장 목록 가져오기
	 * @param busStopSearchVo - 정류장 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	List<BusStopListOutputVo> selectBusStopList(BusStopSearchVo busStopSearchVo) throws DataAccessException;


	/**
	 * 정류장 정보 가져오기
	 * @param busStopId - 버스정류장 ID
	 * @return
	 * @throws DataAccessException
	 */
	BusStopVo getBusStop(int busStopId) throws DataAccessException;


	/**
	 * 정류장 작업정보 가져오기
	 * @param taskId - 작업ID
	 * @return
	 * @throws DataAccessException
	 */
	BusStopTaskVo getBusStopTask(long taskId) throws DataAccessException;


	/**
	 * 정류장 경유 노선 목록 가져오기
	 * @param busStopId - 버스정류장 ID
	 * @return
	 * @throws DataAccessException
	 */
	List<BusRouteVo> selectBusRouteList(int busStopId) throws DataAccessException;

	/**
	 * 버스정류장 작업 정보 저장
	 * @param busStopTaskInputVo - 정류장 정보
	 * @return
	 * @throws DataAccessException
	 */
	void insertBusStopTask(BusStopTaskInputVo busStopTaskInputVo) throws DataAccessException;

	/**
	 * 버스정류장 부가정보 작업 정보 저장
	 * @param busStopTaskInputVo - 정류장 정보
	 * @return
	 * @throws DataAccessException
	 */
	void insertBusStopSubInfoTask(BusStopTaskInputVo busStopTaskInputVo) throws DataAccessException;

	/**
	 * 버스정류장 BIS 매핑 작업 정보 저장
	 * @param busStopTaskInputVo - 정류장 정보
	 * @throws DataAccessException
	 */
	void insertBusStopMappingTask(BusStopTaskInputVo busStopTaskInputVo) throws DataAccessException;

	/**
	 * 버스정류장 작업 정보 업데이트
	 * @param busStopTaskInputVo - 정류장 정보
	 * @return
	 * @throws DataAccessException
	 */
	int updateBusStopTask(BusStopTaskInputVo busStopTaskInputVo) throws DataAccessException;

	/**
	 * 버스정류장 부가정보 작업 정보 업데이트
	 * @param busStopTaskInputVo - 정류장 정보
	 * @return
	 * @throws DataAccessException
	 */
	int updateBusStopSubInfoTask(BusStopTaskInputVo busStopTaskInputVo) throws DataAccessException;

	/**
	 * 버스정류장 BIS 매핑 작업 정보 업데이트
	 * @param busStopTaskInputVo - 정류장 정보
	 * @throws DataAccessException
	 */
	void updateBusStopMappingTask(BusStopTaskInputVo busStopTaskInputVo) throws DataAccessException;

	/**
	 * 버스 정류장 매핑 정보에서 BIS 정류장 ID에 매핑된 네이버 버스 정류장 ID를 가져온다.매핑된 네이버 버스 정류장 ID가 없는경우 null 값이 리턴된다.
	 * @param busStopMappingVo - BIS 정류장 정보
	 * @return
	 * @throws DataAccessException
	 */
	Integer getBusStopIdMappedToBisStopId(BusStopMappingVo busStopMappingVo) throws DataAccessException;

	/**
	 * 영역내 버스정류장 목록을 가져온다
	 * @param busStopSearchVo - 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	List<BusStopListOutputVo> selectBusStopListFromMapBounds(BusStopSearchVo busStopSearchVo) throws DataAccessException;

}
