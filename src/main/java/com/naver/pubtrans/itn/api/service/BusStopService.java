package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.common.MemberUtil;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskAssignType;
import com.naver.pubtrans.itn.api.consts.TaskDataType;
import com.naver.pubtrans.itn.api.consts.TaskStatus;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.BusStopRepository;
import com.naver.pubtrans.itn.api.repository.TaskRepository;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusRouteVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopTaskVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopChangeVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopListOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopOutputVoWithRoute;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusRouteOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopTaskOutputVoWithRoute;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;
import com.naver.pubtrans.itn.api.vo.task.input.TaskInputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 버스정류장 관리 서비스
 *
 * @author adtec10
 *
 */

@Service
public class BusStopService {

	private final OutputFmtUtil outputFmtUtil;

	private final CommonService commonService;

	private final TaskService taskService;

	private final BusStopRepository busStopRepository;

	private final TaskRepository taskRepository;

	@Autowired
	BusStopService(OutputFmtUtil outputFmtUtil, CommonService commonService, TaskService taskService, BusStopRepository busStopRepository, TaskRepository taskRepository){
		this.outputFmtUtil = outputFmtUtil;
		this.commonService = commonService;
		this.taskService = taskService;
		this.busStopRepository = busStopRepository;
		this.taskRepository = taskRepository;
	}



	/**
	 * 버스정류장 목록을 가져온다
	 * @param busStopSearchVo - 목록 검색 조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusStopList(BusStopSearchVo busStopSearchVo) throws Exception {

		/**
		 * 1. 페이지 목록 조회
		 */

		// 전체 목록 수 가져오기
		int totalListCnt = busStopRepository.getBusStopListTotalCnt(busStopSearchVo);


		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalListCnt, busStopSearchVo.getPageNo(), busStopSearchVo.getListSize());

		// 목록 조회 페이징 정보 set
		busStopSearchVo.setStartPageLimit(pagingVo.getStartPageLimit());
		busStopSearchVo.setEndPageLimit(pagingVo.getEndPageLimit());


		// 목록 조회
		List<BusStopListOutputVo> busStopListOutputVoList = busStopRepository.selectBusStopList(busStopSearchVo);


		/**
		 * 2. 검색 폼 데이터 구조 생성
		 */

		// 사용하고자 하는 컬럼 목록
		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("stop_id");
		usableColumnNameList.add("stop_name");
		usableColumnNameList.add("cityCode");


		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_STOPS.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);



		/**
		 * 3. 공통 출력포맷 생성
		 */
		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, busStopListOutputVoList);

		return commonResult;
	}


	/**
	 * 정류장 작업 히스토리 요약목록을 가져온다
	 * @param busStopId - 정류장 ID
	 * @param searchVo - 페이징 Vo
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusStopTaskSummaryList(int busStopId, SearchVo searchVo) throws Exception {

		CommonResult commonResult = taskService.getTaskSummaryList(busStopId, TaskDataType.STOP.getCode(), searchVo);
		return commonResult;
	}


	/**
	 * 정류장 정보를 조회한다
	 * @param stopId - 정류장 ID
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusStopInfo(int stopId) throws Exception {

		// 정류장 정보
		BusStopVo busStopVo = busStopRepository.getBusStop(stopId);

		if(Objects.isNull(busStopVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}


		BusStopOutputVoWithRoute busStopOutputVoWithRoute = new BusStopOutputVoWithRoute();
		BeanUtils.copyProperties(busStopVo, busStopOutputVoWithRoute);


		// 경유노선 정보 가져오기
		BusRouteOutputVo busRouteOutputVo = this.getBusRouteInfo(stopId);
		if(!Objects.isNull(busRouteOutputVo)) {
			busStopOutputVoWithRoute.setBusRouteInfo(busRouteOutputVo);
		}


		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectBusStopSchemaAll();

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, busStopOutputVoWithRoute);


		return commonResult;
	}


	/**
	 * 정류장 작업정보
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusStopTaskInfo(long taskId) throws Exception {

		// 정류장 작업정보
		BusStopTaskVo busStopTaskVo = busStopRepository.getBusStopTask(taskId);

		// 작업 기본 정보
		TaskOutputVo taskOutputVo = taskRepository.getTaskInfo(taskId);


		if(Objects.isNull(busStopTaskVo) || Objects.isNull(taskOutputVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}


		if(StringUtils.isNotEmpty(taskOutputVo.getBisAutoChangeData())) {

			// Json형태의 Text를 vo로 변경한다
			BusStopChangeVo busStopChangeVo = new ObjectMapper().readValue(taskOutputVo.getBisAutoChangeData(), BusStopChangeVo.class);
			taskOutputVo.setBisChangeDataInfo(busStopChangeVo);
		}



		BusStopTaskOutputVoWithRoute busStopTaskOutputVoWithRoute = new BusStopTaskOutputVoWithRoute();

		BeanUtils.copyProperties(busStopTaskVo, busStopTaskOutputVoWithRoute);
		busStopTaskOutputVoWithRoute.setTaskInfo(taskOutputVo);

		// 경유노선 정보 가져오기
		BusRouteOutputVo busRouteOutputVo = this.getBusRouteInfo(busStopTaskVo.getStopId());

		if(!Objects.isNull(busRouteOutputVo)) {
			busStopTaskOutputVoWithRoute.setBusRouteInfo(busRouteOutputVo);
		}


		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectBusStopSchemaAll();

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, busStopTaskOutputVoWithRoute);


		return commonResult;
	}



	/**
	 * 버스 정류장 상세정보의 전체 스키마 정보를 가져온다
	 * @return
	 * @throws Exception
	 */
	public List<CommonSchema> selectBusStopSchemaAll() throws Exception {
		List<CommonSchema> commonSchemaList = new ArrayList<>();

		commonSchemaList.addAll(this.selectBusStopSchema());
		commonSchemaList.addAll(this.selectBusStopInfoSchema());
		commonSchemaList.addAll(this.selectBusStopMappingSchema());
		commonSchemaList.addAll(taskService.selectTaskSchemaMinimal());

		// 동일 컬럼에 대해 중복을 제거
		List<CommonSchema> distinctCommonSchemaVoList = outputFmtUtil.distinctCommonSchemaList(commonSchemaList);

		return distinctCommonSchemaVoList;
	}


	/**
	 * 정류장 기본정보 테이블 스키마
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectBusStopSchema() throws Exception {

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_STOPS.getName(), null, null);

		return commonSchemaList;
	}


	/**
	 * 정류장 부가정보 테이블 스키마
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectBusStopInfoSchema() throws Exception {


		ArrayList<String> ignoreColumnNameList = new ArrayList<>();
		ignoreColumnNameList.add("alias_kor");
		ignoreColumnNameList.add("mscode");

		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_BUS_STOPS_INFO.getName(), CommonConstant.IGNORE_COLUMN, ignoreColumnNameList);

		return commonSchemaList;
	}



	/**
	 * 정류장 매핑정보 테이블 스키마
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectBusStopMappingSchema() throws Exception {

		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("local_stop_id");

		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_BUS_STOP_MAPPING.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);

		return commonSchemaList;
	}



	/**
	 * 경유 노선 정보를 가져온다
	 * @param stopId - 정류장 ID
	 * @return
	 * @throws Exception
	 */
	public BusRouteOutputVo getBusRouteInfo(int stopId) throws Exception {
		List<BusRouteVo> busRouteVoList = busStopRepository.selectBusRouteList(stopId);

		if(busRouteVoList.size() == 0) {
			return null;
		}

		return new BusRouteOutputVo(busRouteVoList.size(), busRouteVoList);

	}


	/**
	 * 버스정류장 작업정보를 등록한다
	 * @param taskType - 작업 등록구분(컨텐츠 추가, 수정, 삭제)
	 * @param busStopTaskInputVo - 버스 정류장 입력정보
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public CommonResult registerBusStopTask(String taskType, BusStopTaskInputVo busStopTaskInputVo) throws Exception {


		// 정류장 신규등록
		if(taskType.equals(TaskType.REGISTER.getCode())) {

			/**
			 * 정류장 임시 ID 설정
			 *
			 * Task 검수 완료 후 배포시 정류장 테이블의 auto increment값을 사용하여 ID를 부여한다.
			 *
			 */
			busStopTaskInputVo.setStopId(0);
		}



		// Task 기본정보
		TaskInputVo taskInputVo = new TaskInputVo();
		taskInputVo.setTaskType(taskType);
		taskInputVo.setPubTransId(busStopTaskInputVo.getStopId());

		taskInputVo.setProviderId(busStopTaskInputVo.getProviderId());
		taskInputVo.setTaskStatus(TaskStatus.PROGRESS.getCode());
		taskInputVo.setTaskDataType(TaskDataType.STOP.getCode());
		taskInputVo.setTaskDataName(busStopTaskInputVo.getStopName());
		taskInputVo.setTaskComment(busStopTaskInputVo.getTaskComment());
		taskInputVo.setTaskRegisterType(CommonConstant.MANUAL);
		taskInputVo.setCheckUserId(busStopTaskInputVo.getCheckUserId());

		/*
		 * 등록자, 작업자 정보
		 * 작업 등록시  등록자,작업자는 본인 자신이다.
		 */
		taskService.addTaskMemberInfo(TaskAssignType.REGISTER.getCode(), taskInputVo);
		taskService.addTaskMemberInfo(TaskAssignType.WORK.getCode(), taskInputVo);

		// 검수자 정보
		taskService.addTaskMemberInfo(TaskAssignType.CHECK.getCode(), taskInputVo);



		/**
		 * Task를 구성하는 전체 정보를 저장한다
		 *
		 * 1. Task 기본정보 저장
		 * 2. Task 할당정보 저장
		 * 3. Task 상태변경 히스토리 정보 저장
		 */
		long taskId = taskService.registerTaskInfoAll(taskInputVo);

		busStopTaskInputVo.setTaskId(taskId);


		// 정류장 연관 Task 테이블에 저장
		busStopRepository.insertBusStopTask(busStopTaskInputVo);
		busStopRepository.insertBusStopSubInfoTask(busStopTaskInputVo);

		// BIS 매핑정보
		if(StringUtils.isNotEmpty(busStopTaskInputVo.getLocalStopId())) {
			busStopRepository.insertBusStopMappingTask(busStopTaskInputVo);
		}


		// 성공시 작업ID 리턴
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, taskId);
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		return commonResult;
	}


	/**
	 * 버스정류장 작업정보를 수정한다
	 * @param busStopTaskInputVo - 버스 정류장 작업정보
	 * @throws Exception
	 */
	@Transactional
	public void modifyBusStopTask(BusStopTaskInputVo busStopTaskInputVo) throws Exception {

		// Task 기본정보
		TaskInputVo taskInputVo = new TaskInputVo();
		taskInputVo.setTaskId(busStopTaskInputVo.getTaskId());
		taskInputVo.setProviderId(busStopTaskInputVo.getProviderId());
		taskInputVo.setTaskStatus(TaskStatus.PROGRESS.getCode());
		taskInputVo.setTaskDataName(busStopTaskInputVo.getStopName());
		taskInputVo.setTaskComment(busStopTaskInputVo.getTaskComment());
		taskInputVo.setCheckUserId(busStopTaskInputVo.getCheckUserId());

		// 등록자 정보
		taskService.addTaskMemberInfo(TaskAssignType.REGISTER.getCode(), taskInputVo);

		// 검수자 정보
		taskService.addTaskMemberInfo(TaskAssignType.CHECK.getCode(), taskInputVo);


		/**
		 * Task를 구성하는 전체 정보를 수정한다
		 *
		 * 1. Task 기본정보 저장
		 * 2. Task 할당정보 저장
		 */
		int updateTaskInfoCnt = taskService.modifyTaskInfoAll(taskInputVo);

		// 정류장 연관 Task 데이터 업데이트
		int updateBusStopTaskCnt = busStopRepository.updateBusStopTask(busStopTaskInputVo);
		int updateBusStopSubTaskCnt = busStopRepository.updateBusStopSubInfoTask(busStopTaskInputVo);

		// BIS 매핑정보 Task 업데이트
		busStopRepository.updateBusStopMappingTask(busStopTaskInputVo);


		// 저장 오류 처리
		if(!(updateTaskInfoCnt == 1 && updateBusStopTaskCnt == 1 && updateBusStopSubTaskCnt == 1)) {
			throw new ApiException(ResultCode.SAVE_FAIL.getApiErrorCode(), ResultCode.SAVE_FAIL.getDisplayMessage());
		}
	}


	/**
	 * 버스정류장을 삭제요청을 진행한다
	 * <pre>
	 * 삭제요청 작업(Task)을 생성한다
	 * 이후 검수자 확인 및 배포처리(배포관리 메뉴)를 통해 최종 마스터 테이블에 반영
	 * </pre>
	 * @param busStopRemoveTaskInputVo - 버스정류장 삭제정보
	 * @return
	 * @throws Exception
	 */
	public CommonResult registerBusStopRemoveTask(BusStopRemoveTaskInputVo busStopRemoveTaskInputVo) throws Exception {


		int stopId = busStopRemoveTaskInputVo.getStopId();

		// 기본정보 가져오기
		BusStopVo busStopVo = busStopRepository.getBusStop(stopId);

		if(Objects.isNull(busStopVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

		// 경유노선 정보 가져오기
		List<BusRouteVo> busRouteVoList = busStopRepository.selectBusRouteList(stopId);

		if(busRouteVoList.size() > 0) {
			throw new ApiException(ResultCode.STOP_REMOVE_EXISTS_BUS_ROUTE.getApiErrorCode(), ResultCode.STOP_REMOVE_EXISTS_BUS_ROUTE.getDisplayMessage());
		}

		BusStopTaskInputVo busStopTaskInputVo = new BusStopTaskInputVo();
		BeanUtils.copyProperties(busStopVo, busStopTaskInputVo);

		busStopTaskInputVo.setTaskComment(busStopRemoveTaskInputVo.getTaskComment());
		busStopTaskInputVo.setCheckUserId(busStopRemoveTaskInputVo.getCheckUserId());


		// 정류장 삭제요청 Task 등록
		return this.registerBusStopTask(TaskType.REMOVE.getCode(), busStopTaskInputVo);

	}

}
