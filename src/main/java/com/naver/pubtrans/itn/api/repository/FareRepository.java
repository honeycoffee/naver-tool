package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.fare.FareInfoTaskVo;
import com.naver.pubtrans.itn.api.vo.fare.FareInfoVo;
import com.naver.pubtrans.itn.api.vo.fare.FareTaskVo;
import com.naver.pubtrans.itn.api.vo.fare.FareVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareSearchVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.output.IgnoredFareListOutputVo;

/**
 * 버스정류장 Repository
 *
 * @author adtec10
 *
 */
@Repository
public interface FareRepository {

	/**
	 * 기본 요금 룰 가져오기
	 * @param fareSearchVo - 요금 룰 검색 Vo
	 * @return
	 * @throws DataAccessException
	 */
	FareVo getBaseFareRule(FareSearchVo fareSearchVo) throws DataAccessException;


	/**
	 * 예외 요금 룰 가져오기
	 * @param fareSearchVo - 요금 룰 검색 Vo
	 * @return
	 * @throws DataAccessException
	 */
	FareVo getIngoredFareRule(FareSearchVo fareSearchVo) throws DataAccessException;


	/**
	 * 예외 요금 룰 목록 가져오기
	 * @param busStopId - 버스정류장 ID
	 * @return
	 * @throws DataAccessException
	 */
	List<IgnoredFareListOutputVo> selectIgnoredFareRuleList(FareSearchVo fareSearchVo) throws DataAccessException;

	/**
	 * 요금 룰 작업정보 가져오기
	 * @param taskId - 작업 ID
	 * @return
	 * @throws DataAccessException
	 */
	FareTaskVo getFareRuleTask(long taskId) throws DataAccessException;

	/**
	 * 요금 룰 상세정보 가져오기
	 * @param fareSearchVo - 요금 룰 검색 Vo
	 * @return
	 * @throws DataAccessException
	 */
	FareInfoVo getFareRuleInfo(FareSearchVo fareSearchVo) throws DataAccessException;

	/**
	 * 요금 룰 상세정보 작업정보 가져오기
	 * @param fareSearchVo - 요금 룰 검색 Vo
	 * @return
	 * @throws DataAccessException
	 */
	FareInfoVo getFareRuleInfoTask(long taskId) throws DataAccessException;

	/**
	 * 요금 룰 작업정보 저장
	 * @param fareTaskInputVo - 요금 룰 정보
	 * @return
	 * @throws DataAccessException
	 */
	void insertFareRuleTask(FareTaskInputVo fareTaskInputVo) throws DataAccessException;

	/**
	 * 요금 룰 작업정보 업데이트
	 * @param fareTaskInputVo - 요금 룰 정보
	 * @return
	 * @throws DataAccessException
	 */
	void updateFareRuleTask(FareTaskInputVo fareTaskInputVo) throws DataAccessException;

	/**
	 * 요금 룰 상세정보 작업정보 저장
	 * @param fareTaskInputVo - 요금 룰 정보
	 * @return
	 * @throws DataAccessException
	 */
	void insertFareRuleInfoTask(FareTaskInputVo fareTaskInputVo) throws DataAccessException;

	/**
	 * 요금 룰 상세정보 작업정보 업데이트
	 * @param fareTaskInputVo - 요금 룰 정보
	 * @return
	 * @throws DataAccessException
	 */
	void updateFareRuleInfoTask(FareTaskInputVo fareTaskInputVo) throws DataAccessException;

	/**
	 * 요금 룰과 노선 매핑 변경정보 저장
	 * @param fareTaskInputVo - 요금 룰 정보
	 * @throws DataAccessException
	 */
	void insertRoutFareMappingTask(FareTaskInputVo fareTaskInputVo) throws DataAccessException;

	/**
	 * 요금 룰과 매핑된 노선 리스트 가져오기
	 * @param fareId - 요금 룰 ID
	 * @return
	 * @throws DataAccessException
	 */
	List<BusRouteListOutputVo> selectBusRouteFareMappingList(int fareId) throws DataAccessException;

	/**
	 * 요금 룰과 매핑된 노선 작업정보 리스트 가져오기
	 * @param taskId - 작업정보 ID 
	 * @return
	 * @throws DataAccessException
	 */
	List<BusRouteListOutputVo> selectBusRouteFareMappingTaskList(long taskId) throws DataAccessException;

}
