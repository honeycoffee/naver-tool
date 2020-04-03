package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteStopVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteTaskVo;
import com.naver.pubtrans.itn.api.vo.bus.route.BusRouteVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteCompanyTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteStopTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.input.BusRouteTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteBypassOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteCompanyOutputVo;
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

	/**
	 * 버스 노선 정보를 가져온다
	 * @param routeId - 노선ID
	 * @return
	 * @throws DataAccessException
	 */
	BusRouteVo getBusRoute(int routeId) throws DataAccessException;

	/**
	 * 버스 노선 작업정보를 가져온다
	 * @param taskId - 작업ID
	 * @return
	 * @throws DataAccessException
	 */
	BusRouteTaskVo getBusRouteTask(long taskId) throws DataAccessException;

	/**
	 * 버스노선을 운형하는 운수회사 목록을 가져온다
	 * @param routeId - 노선ID
	 * @return
	 * @throws DataAccessException
	 */
	List<BusRouteCompanyOutputVo> selectBusRouteCompanyList(int routeId) throws DataAccessException;

	/**
	 * 버스노선을 운행하는 운수회사 작업 목록 수를 가져온다
	 * @param taskId - 작업ID
	 * @return
	 * @throws DataAccessException
	 */
	int getBusRouteCompanyTaskListCnt(long taskId) throws DataAccessException;

	/**
	 * 버스노선을 운행하는 운수회사 작업 목록을 가져온다
	 * @param taskId - 작업ID
	 * @return
	 * @throws DataAccessException
	 */
	List<BusRouteCompanyOutputVo> selectBusRouteCompanyTaskList(long taskId) throws DataAccessException;

	/**
	 * 본 노선에 속해있는 우회노선 정보 목록을 가져온다
	 * @param parentRouteId - 부모 노선ID
	 * @return
	 * @throws DataAccessException
	 */
	List<BusRouteBypassOutputVo> selectBusRouteBypassList(int parentRouteId) throws DataAccessException;

	/**
	 * 버스 운행요일에 따른 서비스ID를 가져온다
	 * @param busRouteTaskInputVo - 버스노선 작업정보
	 * @return
	 * @throws DataAccessException
	 */
	Integer getCalendarServiceId(BusRouteTaskInputVo busRouteTaskInputVo) throws DataAccessException;

	/**
	 * 기본 요금ID를 가져온다
	 * @param busRouteTaskInputVo - 버스노선 작업정보
	 * @return
	 * @throws DataAccessException
	 */
	Integer getBaseFareId(BusRouteTaskInputVo busRouteTaskInputVo) throws DataAccessException;

	/**
	 * 버스노선 기본 작업정보를 저장한다
	 * @param busRouteTaskInputVo - 버스노선 작업정보
	 * @throws DataAccessException
	 */
	void insertBusRouteTask(BusRouteTaskInputVo busRouteTaskInputVo) throws DataAccessException;

	/**
	 * 버스노선 부가 작업정보를 저장한다
	 * @param busRouteTaskInputVo - 버스노선 작업정보
	 * @throws DataAccessException
	 */
	void insertBusRouteSubTask(BusRouteTaskInputVo busRouteTaskInputVo) throws DataAccessException;

	/**
	 * 버스노선 운수회사 매핑 작업정보를 저장한다
	 * @param busRouteCompanyTaskInputVo - 운수회사 정보
	 * @throws DataAccessException
	 */
	void insertBusRouteCompanyTask(BusRouteCompanyTaskInputVo busRouteCompanyTaskInputVo) throws DataAccessException;

	/**
	 * 버스노선 BIS 매핑 작업정보를 저장한다
	 * @param busRouteTaskInputVo - 버스노선 작업정보
	 * @throws DataAccessException
	 */
	void insertBusRouteMappingTask(BusRouteTaskInputVo busRouteTaskInputVo) throws DataAccessException;

	/**
	 * 버스노선 우회노선 작업정보를 저장한다
	 * @param busRouteTaskInputVo - 버스노선 작업정보
	 * @throws DataAccessException
	 */
	void insertBusRouteBypassTask(BusRouteTaskInputVo busRouteTaskInputVo) throws DataAccessException;

	/**
	 * 버스노선 요금 작업정보를 저장한다
	 * @param busRouteTaskInputVo - 버스노선 작업정보
	 * @throws DataAccessException
	 */
	void insertBusRouteFareTask(BusRouteTaskInputVo busRouteTaskInputVo) throws DataAccessException;

	/**
	 * 버스노선 경유정류장 작업정보를 저장한다
	 * @param busRouteStopTaskInputVo - 경유정류장 작업 정보
	 * @throws DataAccessException
	 */
	void insertBusRouteStopTask(BusRouteStopTaskInputVo busRouteStopTaskInputVo) throws DataAccessException;

}
