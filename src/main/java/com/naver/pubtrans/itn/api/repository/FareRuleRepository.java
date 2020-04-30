package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.fare.FareRuleInfoVo;
import com.naver.pubtrans.itn.api.vo.fare.FareRuleTaskVo;
import com.naver.pubtrans.itn.api.vo.fare.FareRuleVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleSearchVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.output.ExceptionalFareRuleOutputVo;

/**
 * 요금 룰 Repository
 *
 * @author westwind
 *
 */
@Repository
public interface FareRuleRepository {

	/**
	 * 기본 요금 룰 가져오기
	 * @param fareRuleSearchVo - 요금 룰 검색 Vo
	 * @return
	 * @throws DataAccessException
	 */
	FareRuleVo getBaseFareRule(FareRuleSearchVo fareRuleSearchVo) throws DataAccessException;


	/**
	 * 예외 요금 룰 가져오기
	 * @param fareRuleSearchVo - 요금 룰 검색 Vo
	 * @return
	 * @throws DataAccessException
	 */
	FareRuleVo getExceptionalFareRule(FareRuleSearchVo fareRuleSearchVo) throws DataAccessException;


	/**
	 * 예외 요금 룰 목록 가져오기
	 * @param fareRuleSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	List<ExceptionalFareRuleOutputVo> selectExceptionalFareRuleList(FareRuleSearchVo fareRuleSearchVo) throws DataAccessException;

	/**
	 * 요금 룰 작업정보 가져오기
	 * @param taskId - 작업 ID
	 * @return
	 * @throws DataAccessException
	 */
	FareRuleTaskVo getFareRuleTask(long taskId) throws DataAccessException;

	/**
	 * 요금 룰 상세정보 가져오기
	 * @param fareRuleSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	FareRuleInfoVo getFareRuleInfo(FareRuleSearchVo fareRuleSearchVo) throws DataAccessException;

	/**
	 * 요금 룰 상세정보 작업정보 가져오기
	 * @param fareRuleSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws DataAccessException
	 */
	FareRuleInfoVo getFareRuleInfoTask(FareRuleSearchVo fareRuleSearchVo) throws DataAccessException;

	/**
	 * 요금 룰 작업정보 저장
	 * @param fareRuleTaskInputVo - 요금 룰 입력정보
	 * @throws DataAccessException
	 */
	void insertFareRuleTask(FareRuleTaskInputVo fareRuleTaskInputVo) throws DataAccessException;

	/**
	 * 요금 룰 작업정보 업데이트 
	 * @param fareRuleTaskInputVo - 요금 룰 입력정보
	 * @return
	 * @throws DataAccessException
	 */
	int updateFareRuleTask(FareRuleTaskInputVo fareRuleTaskInputVo) throws DataAccessException;

	/**
	 * 요금 룰 상세정보 작업정보 저장
	 * @param fareRuleTaskInputVo - 요금 룰 입력정보
	 * @throws DataAccessException
	 */
	void insertFareRuleInfoTask(FareRuleTaskInputVo fareRuleTaskInputVo) throws DataAccessException;

	/**
	 * 요금 룰 상세정보 작업정보 업데이트
	 * @param fareRuleTaskInputVo - 요금 룰 입력정보
	 * @return
	 * @throws DataAccessException
	 */
	int updateFareRuleInfoTask(FareRuleTaskInputVo fareRuleTaskInputVo) throws DataAccessException;

	/**
	 * 노선-요금 매핑 작업정보 저장
	 * @param fareRuleTaskInputVo - 요금 룰 입력정보
	 * @throws DataAccessException
	 */
	void insertRoutFareMappingTask(FareRuleTaskInputVo fareRuleTaskInputVo) throws DataAccessException;

	/**
	 * 노선-요금 매핑 작업정보 삭제
	 * @param taskId - 작업정보 ID
	 * @throws DataAccessException
	 */
	void deleteRoutFareMappingTask(long taskId) throws DataAccessException;

	/**
	 * 노선-요금 매핑 정보의 fareId에 해당하는 버스 노선 리스트 가져오기
	 * @param fareId - 요금 룰 ID
	 * @return
	 * @throws DataAccessException
	 */
	List<BusRouteListOutputVo> selectBusRouteListMappedToRouteFareRule(int fareId) throws DataAccessException;

	/**
	 * 노선-요금 매핑 정보의 taskId에 해당하는 버스 노선 리스트 가져오기
	 * @param taskId - 작업정보 ID 
	 * @return
	 * @throws DataAccessException
	 */
	List<BusRouteListOutputVo> selectBusRouteListMappedToRouteFareRuleTask(long taskId) throws DataAccessException;

}
