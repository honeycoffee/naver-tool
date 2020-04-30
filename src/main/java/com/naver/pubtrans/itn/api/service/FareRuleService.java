package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskAssignType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.FareRuleRepository;
import com.naver.pubtrans.itn.api.repository.TaskRepository;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.fare.FareRuleInfoVo;
import com.naver.pubtrans.itn.api.vo.fare.FareRuleTaskVo;
import com.naver.pubtrans.itn.api.vo.fare.FareRuleVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleSearchVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.output.ExceptionalFareRuleOutputVo;
import com.naver.pubtrans.itn.api.vo.fare.output.FareRuleOutputVoWithRouteList;
import com.naver.pubtrans.itn.api.vo.fare.output.FareRuleTaskOutputVoWithRouteList;
import com.naver.pubtrans.itn.api.vo.task.input.TaskInputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 네이버 대중교통 내재화 요금 룰 서비스
 * 
 * @author westwind
 *
 */
@Service
public class FareRuleService {

	private final OutputFmtUtil outputFmtUtil;

	private final CommonService commonService;

	private final TaskService taskService;

	private final FareRuleRepository fareRuleRepository;

	private final TaskRepository taskRepository;

	@Autowired
	FareRuleService(OutputFmtUtil outputFmtUtil, CommonService commonService, TaskService taskService,
		FareRuleRepository fareRuleRepository, TaskRepository taskRepository) {
		this.outputFmtUtil = outputFmtUtil;
		this.commonService = commonService;
		this.taskService = taskService;
		this.fareRuleRepository = fareRuleRepository;
		this.taskRepository = taskRepository;
	}

	/**
	 * 기본 요금 룰을 조회한다
	 * 
	 * @param fareRuleSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBaseFareRule(FareRuleSearchVo fareRuleSearchVo) throws Exception {

		// 요금 정보
		FareRuleVo fareRuleVo = new FareRuleVo();
		
		// fareId 없이 cityCode와 busClass로 조회
		fareRuleVo = fareRuleRepository.getBaseFareRule(fareRuleSearchVo);

		if (Objects.isNull(fareRuleVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}
		
		// 상세정보 검색을 위해 fareId 입력
		fareRuleSearchVo.setFareId(fareRuleVo.getFareId());
		
		// 일반 - 카드 요금 상세정보 가져오기 (ageId, paymentId 미 설정시 default 1) 
		FareRuleInfoVo fareRuleInfoVo = fareRuleRepository.getFareRuleInfo(fareRuleSearchVo);

		fareRuleVo.setGeneralCardFareRuleInfo(fareRuleInfoVo);

		FareRuleOutputVoWithRouteList fareRuleOutputVoWithRouteList = new FareRuleOutputVoWithRouteList();
		BeanUtils.copyProperties(fareRuleVo, fareRuleOutputVoWithRouteList);

		// 매핑 노선 목록 가져오기
		List<BusRouteListOutputVo> busRouteListOutputVoList = 
			this.selectBusRouteListMappedToRouteFareMapping(fareRuleOutputVoWithRouteList.getFareId());

		if (!Objects.isNull(busRouteListOutputVoList)) {
			fareRuleOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);
			fareRuleOutputVoWithRouteList.setTotalRouteCount(busRouteListOutputVoList.size());
		}

		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectFareRuleSchemaAll();

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareRuleOutputVoWithRouteList);

		return commonResult;
	}

	/**
	 * 예외 요금 룰을 조회한다
	 * 
	 * @param fareRuleSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult getExceptionalFareRule(FareRuleSearchVo fareRuleSearchVo) throws Exception {

		// 요금 정보
		FareRuleVo fareRuleVo = new FareRuleVo();
		
		// 예외 요금 룰의 경우 fareId 로 조회
		fareRuleVo = fareRuleRepository.getExceptionalFareRule(fareRuleSearchVo);

		if (Objects.isNull(fareRuleVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}
		
		// 예외 요금 룰의 경우 이름과 노선 목록이 필요하여 정보를 가져옴
		if(fareRuleSearchVo.getFareId() != null) {
			CommonResult exceptionalFareRuleResult = this.selectExceptionalFareRuleList(fareRuleSearchVo);
			
			@SuppressWarnings("unchecked") 
			List<ExceptionalFareRuleOutputVo> exceptionalFareRuleOutputVoList = (List<ExceptionalFareRuleOutputVo>)exceptionalFareRuleResult.getData();
			
			// 예외 요금 목록 중 현재 조회한 fareId와 일치하는 정보를 가져온다.
			ExceptionalFareRuleOutputVo exceptionalFareListOutputVo = exceptionalFareRuleOutputVoList.stream()
				.filter(o -> o.getFareId() == fareRuleSearchVo.getFareId()).collect(Collectors.toList()).get(0);

			StringBuilder stringBuilder = new StringBuilder(CommonConstant.EXCEPTION_TEXT);
			stringBuilder.append(CommonConstant.BLANK);
			stringBuilder.append(exceptionalFareListOutputVo.getOrder());
			
			fareRuleVo.setExceptionalFareRuleName(stringBuilder.toString());
			fareRuleVo.setRouteNames(exceptionalFareListOutputVo.getRouteNames());
		}
		
		// 상세정보 검색을 위해 fareId 입력
		fareRuleSearchVo.setFareId(fareRuleVo.getFareId());
		
		// 일반 - 카드 요금 상세정보 가져오기 (ageId, paymentId 미 설정시 default 1) 
		FareRuleInfoVo fareRuleInfoVo = fareRuleRepository.getFareRuleInfo(fareRuleSearchVo);

		fareRuleVo.setGeneralCardFareRuleInfo(fareRuleInfoVo);

		FareRuleOutputVoWithRouteList fareRuleOutputVoWithRouteList = new FareRuleOutputVoWithRouteList();
		BeanUtils.copyProperties(fareRuleVo, fareRuleOutputVoWithRouteList);

		// 매핑 노선 목록 가져오기
		List<BusRouteListOutputVo> busRouteListOutputVoList = 
			this.selectBusRouteListMappedToRouteFareMapping(fareRuleOutputVoWithRouteList.getFareId());

		if (!Objects.isNull(busRouteListOutputVoList)) {
			fareRuleOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);
			fareRuleOutputVoWithRouteList.setTotalRouteCount(busRouteListOutputVoList.size());
		}

		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectFareRuleSchemaAll();

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareRuleOutputVoWithRouteList);

		return commonResult;
	}

	/**
	 * 노선-요금 매핑 작업 정보의 fareId에 해당하는 버스 노선 리스트 가져온다
	 * 
	 * @param fareId - 요금 룰 ID
	 * @return
	 */
	public List<BusRouteListOutputVo> selectBusRouteListMappedToRouteFareMapping(int fareId) {

		List<BusRouteListOutputVo> busRouteListOutputVoList = fareRuleRepository.selectBusRouteListMappedToRouteFareRule(fareId);

		return busRouteListOutputVoList;

	}

	/**
	 * 노선-요금 매핑 작업 정보의 taskId에 해당하는 버스 노선 리스트 가져온다.
	 * 
	 * @param taskId - 작업 ID
	 * @return
	 */
	public List<BusRouteListOutputVo> selectBusRouteListMappedToRouteFareMappingTask(long taskId) {

		List<BusRouteListOutputVo> busRouteListOutputVoList = fareRuleRepository.selectBusRouteListMappedToRouteFareRuleTask(taskId);

		return busRouteListOutputVoList;

	}

	/**
	 * 요금 룰 작업 히스토리 요약목록을 가져온다
	 * 
	 * @param fareId   - 요금 룰 ID
	 * @param searchVo - 페이징 Vo
	 * @return
	 * @throws Exception
	 */
	public CommonResult getFareRuleTaskSummaryList(int fareId, SearchVo searchVo) throws Exception {

		CommonResult commonResult = taskService.getTaskSummaryList(fareId, PubTransType.FARE_RULE, searchVo);
		return commonResult;
		
	}

	/**
	 * 예외 요금 룰 목록을 가져온다.
	 * 
	 * @param fareRuleSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult selectExceptionalFareRuleList(FareRuleSearchVo fareRuleSearchVo) throws Exception {

		// 예외 요금 룰 목록
		List<ExceptionalFareRuleOutputVo> exceptionalFareRuleOutputVoList = 
			fareRuleRepository.selectExceptionalFareRuleList(fareRuleSearchVo);

		AtomicInteger index = new AtomicInteger(1);

		// "예외 1) 노선1,노선2" 와 같은 이름을 만들기 위해 요금 룰 순서를 정의하고 이름 생성
		exceptionalFareRuleOutputVoList.stream().forEach(o -> {
		    o.setOrder(index.getAndIncrement());
		    o.setExceptionalFareRuleName();
		});

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(exceptionalFareRuleOutputVoList);
		
		return commonResult;
	}

	/**
	 * 요금 룰 작업정보 조회
	 * 
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	public CommonResult getFareRuleTaskInfo(long taskId) throws Exception {

		// 요금 룰 작업정보
		FareRuleTaskVo fareRuleTaskVo = fareRuleRepository.getFareRuleTask(taskId);
		
		FareRuleSearchVo fareRuleSearchVo = new FareRuleSearchVo();
		fareRuleSearchVo.setTaskId(taskId);
		

		// 일반 - 카드 요금 상세정보  작업정보
		FareRuleInfoVo fareRuleInfoVo = fareRuleRepository.getFareRuleInfoTask(fareRuleSearchVo);

		if (fareRuleInfoVo != null) {
			fareRuleTaskVo.setGeneralCardFareRuleInfo(fareRuleInfoVo);
		}
		
		// 작업 기본 정보
		TaskOutputVo taskOutputVo = taskRepository.getTaskInfo(taskId);

		if (Objects.isNull(fareRuleTaskVo) || Objects.isNull(taskOutputVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

		FareRuleTaskOutputVoWithRouteList fareRuleTaskOutputVoWithRouteList = new FareRuleTaskOutputVoWithRouteList();

		BeanUtils.copyProperties(fareRuleTaskVo, fareRuleTaskOutputVoWithRouteList);
		fareRuleTaskOutputVoWithRouteList.setTaskInfo(taskOutputVo);

		// 관련 노선 목록 가져오기
		List<BusRouteListOutputVo> busRouteListOutputVoList = this.selectBusRouteListMappedToRouteFareMappingTask(taskId);

		if (!Objects.isNull(busRouteListOutputVoList)) {
			fareRuleTaskOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);
		}

		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectFareRuleSchemaAll();

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareRuleTaskOutputVoWithRouteList);

		return commonResult;
	}

	/**
	 * 요금 룰의 전체 스키마 정보를 가져온다
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CommonSchema> selectFareRuleSchemaAll() throws Exception {
		List<CommonSchema> commonSchemaList = new ArrayList<>();

		commonSchemaList.addAll(this.selectFareRuleSchema());
		commonSchemaList.addAll(this.selectFareRuleInfoSchema());
		commonSchemaList.addAll(this.selectRouteFareMappingSchema());
		commonSchemaList.addAll(taskService.selectTaskSchemaMinimal());

		// 동일 컬럼에 대해 중복을 제거
		List<CommonSchema> distinctCommonSchemaVoList = outputFmtUtil.distinctCommonSchemaList(commonSchemaList);

		return distinctCommonSchemaVoList;
	}

	/**
	 * 요금 룰 테이블 스키마
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectFareRuleSchema() throws Exception {

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_FARE_RULE.getName(),
			null, null);

		return commonSchemaList;
	}

	/**
	 * 요금 룰 상세 정보 테이블 스키마
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectFareRuleInfoSchema() throws Exception {

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(
			PubTransTable.TB_FARE_RULE_INFO.getName(),
			null, null);

		return commonSchemaList;
	}

	/**
	 * 노선과 요금 룰 매핑정보 테이블 스키마
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectRouteFareMappingSchema() throws Exception {

		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("route_id");

		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(
			PubTransTable.TB_ROUTE_FARE_MAPPING.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);

		return commonSchemaList;
	}

	/**
	 * 요금 룰 작업정보를 등록한다
	 * @param taskType - 작업 등록구분(컨텐츠 추가, 수정, 삭제)
	 * @param fareRuleTaskInputVo - 요금 룰 입력정보
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public CommonResult registerFareRuleTask(TaskType taskType, FareRuleTaskInputVo fareRuleTaskInputVo) throws Exception {

		// 요금 룰 신규등록
		if (taskType.equals(TaskType.REGISTER)) {

			/**
			 * 요금 룰 임시 ID 설정
			 *
			 * Task 검수 완료 후 배포시 요금 룰 테이블의 auto increment값을 사용하여 ID를 부여한다.
			 *
			 */
			fareRuleTaskInputVo.setFareId(0);
		}

		// Task 기본정보
		TaskInputVo taskInputVo = new TaskInputVo();
		taskInputVo.setTaskType(taskType);
		taskInputVo.setPubTransId(fareRuleTaskInputVo.getFareId());
		taskInputVo.setTaskStatusType(TaskStatusType.CHECKING);
		taskInputVo.setPubTransType(PubTransType.FARE_RULE);
		taskInputVo.setPubTransName(this.getFareRuleDataName(fareRuleTaskInputVo));
		taskInputVo.setTaskComment(fareRuleTaskInputVo.getTaskComment());
		taskInputVo.setCheckUserId(fareRuleTaskInputVo.getCheckUserId());
		taskInputVo.setTaskDataSourceType(fareRuleTaskInputVo.getTaskDataSourceType());

		/*
		 * 등록자, 작업자 정보
		 * 작업 등록시  등록자,작업자는 본인 자신이다.
		 */
		taskService.addTaskMemberInfo(TaskAssignType.REGISTER, taskInputVo);
		taskService.addTaskMemberInfo(TaskAssignType.WORK, taskInputVo);

		// 검수자 정보
		taskService.addTaskMemberInfo(TaskAssignType.CHECK, taskInputVo);

		/**
		 * Task를 구성하는 전체 정보를 저장한다
		 *
		 * 1. Task 기본정보 저장
		 * 2. Task 할당정보 저장
		 * 3. Task 상태변경 히스토리 정보 저장
		 */

		Integer[] startStopIds = fareRuleTaskInputVo.getStartStopIds();
		Integer[] endStopIds = fareRuleTaskInputVo.getEndStopIds();
		List<Long> taskIdList = new ArrayList<Long>();
		
		// 일반 카드 요금 설정 ageId : 1, paymentId : 1
		fareRuleTaskInputVo.setAgeId(1);
		fareRuleTaskInputVo.setPaymentId(1);
		
		// 구간 시작과 종료 데이터가 1개 이상 있다면 각각의 요금 룰을 task로 등록해야 해서 반복문 실행  
		if (ArrayUtils.isNotEmpty(startStopIds) && ArrayUtils.isNotEmpty(endStopIds)) {

			for (int i = 0; i < startStopIds.length; i++) {
				long taskId = taskService.registerTaskInfoAll(taskInputVo);
				fareRuleTaskInputVo.setTaskId(taskId);
				
				fareRuleTaskInputVo.setStartStopId(startStopIds[i]);
				fareRuleTaskInputVo.setEndStopId(endStopIds[i]);
				
				this.insertFareRuleTasks(fareRuleTaskInputVo);

				taskIdList.add(taskId);
			}
		} else {

			long taskId = taskService.registerTaskInfoAll(taskInputVo);
			fareRuleTaskInputVo.setTaskId(taskId);

			this.insertFareRuleTasks(fareRuleTaskInputVo);

			taskIdList.add(taskId);
		}

		// 성공시 작업ID 리턴
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK_LIST, taskIdList);
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		return commonResult;
	}

	/**
	* 요금 룰 작업정보를 수정한다
	* @param fareRuleTaskInputVo - 요금 룰 작업정보
	* @throws Exception
	*/
	@Transactional
	public void modifyFareRuleTask(FareRuleTaskInputVo fareRuleTaskInputVo) throws Exception {
		 
		long taskId = fareRuleTaskInputVo.getTaskId();

		// Task 기본정보
		TaskInputVo taskInputVo = new TaskInputVo();
		taskInputVo.setTaskId(taskId);
		taskInputVo.setTaskStatusType(TaskStatusType.CHECKING);
		taskInputVo.setPubTransName(this.getFareRuleDataName(fareRuleTaskInputVo));
		taskInputVo.setTaskComment(fareRuleTaskInputVo.getTaskComment());
		taskInputVo.setCheckUserId(fareRuleTaskInputVo.getCheckUserId());
		taskInputVo.setTaskDataSourceType(fareRuleTaskInputVo.getTaskDataSourceType());
		taskInputVo.setCityCode(fareRuleTaskInputVo.getCityCode());

		// 등록자 정보
		taskService.addTaskMemberInfo(TaskAssignType.REGISTER, taskInputVo);

		// 검수자 정보
		taskService.addTaskMemberInfo(TaskAssignType.CHECK, taskInputVo);


		/**
		 * Task를 구성하는 전체 정보를 수정한다
		 *
		 * 1. Task 기본정보 저장
		 * 2. Task 할당정보 저장
		 */
		int updateTaskInfoCnt = taskService.modifyTaskInfoAll(taskInputVo);
		
		Integer[] startStopIds = fareRuleTaskInputVo.getStartStopIds();
		Integer[] endStopIds = fareRuleTaskInputVo.getEndStopIds();
		
		// 수정시에는 구간 시작과 종료 ID가 각각 1개씩이므로 startStopId와 endStopID가 존재하면 첫번째 데이터를 저장  
		if (ArrayUtils.isNotEmpty(startStopIds) && ArrayUtils.isNotEmpty(endStopIds)) {
			fareRuleTaskInputVo.setStartStopId(startStopIds[0]);
			fareRuleTaskInputVo.setEndStopId(endStopIds[0]);
		}

		// 일반 카드 요금 설정 ageId : 1, paymentId : 1
		fareRuleTaskInputVo.setAgeId(1);
		fareRuleTaskInputVo.setPaymentId(1);

		// 요금 룰 연관 Task 데이터 업데이트
		int updateFareRuleTaskCnt = fareRuleRepository.updateFareRuleTask(fareRuleTaskInputVo);
		int updateFareRuleInfoTaskCnt = fareRuleRepository.updateFareRuleInfoTask(fareRuleTaskInputVo);

		/**
		 * 노선-요금 룰 매핑정보 업데이트
		 *  - 노선-요금 룰 매핑정보 Task 삭제 후 추가
		 */
		fareRuleRepository.deleteRoutFareMappingTask(taskId);
		this.insertRoutFareMappingTask(fareRuleTaskInputVo);

		// 저장 오류 처리
		if(!(updateTaskInfoCnt == 1 && updateFareRuleTaskCnt == 1 && updateFareRuleInfoTaskCnt == 1)) {
			throw new ApiException(ResultCode.SAVE_FAIL.getApiErrorCode(), ResultCode.SAVE_FAIL.getDisplayMessage());
		}
	}

	/**
	 * 요금 룰 삭제 작업 요청을 진행한다
	 * 
	 * <pre>
	 * 삭제요청 작업(Task)을 생성한다
	 * 이후 검수자 확인 및 배포처리(배포관리 메뉴)를 통해 최종 마스터 테이블에 반영
	 * </pre>
	 * 
	 * @param fareRuleRemoveTaskInputVo - 요금 룰 삭제정보
	 * @return
	 * @throws Exception
	 */
	public CommonResult registerFareRuleRemoveTask(FareRuleRemoveTaskInputVo fareRuleRemoveTaskInputVo) throws Exception {

		int fareId = fareRuleRemoveTaskInputVo.getFareId();
		
		FareRuleSearchVo fareRuleSearchVo = new FareRuleSearchVo(); 
		fareRuleSearchVo.setFareId(fareId);

		// 예외 요금 룰 정보 가져오기
		FareRuleVo fareRuleVo = fareRuleRepository.getExceptionalFareRule(fareRuleSearchVo);

		if(Objects.isNull(fareRuleVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}
		
		// 요금 룰 상세정보 가져오기
		FareRuleInfoVo fareRuleInfoVo = fareRuleRepository.getFareRuleInfo(fareRuleSearchVo);
		
		FareRuleTaskInputVo fareRuleTaskInputVo = new FareRuleTaskInputVo();
		BeanUtils.copyProperties(fareRuleVo, fareRuleTaskInputVo);
		BeanUtils.copyProperties(fareRuleInfoVo, fareRuleTaskInputVo);
		
		// 예외 노선 목록 가져오기
		List<BusRouteListOutputVo> busRouteListOutputVoList = this.selectBusRouteListMappedToRouteFareMapping(fareId);
		Integer[] routeIds = busRouteListOutputVoList.stream().map(o -> o.getRouteId()).toArray(Integer[]::new);
		
		fareRuleTaskInputVo.setRouteIds(routeIds);
		fareRuleTaskInputVo.setTaskDataSourceType(fareRuleRemoveTaskInputVo.getTaskDataSourceType());
		fareRuleTaskInputVo.setTaskComment(fareRuleRemoveTaskInputVo.getTaskComment());
		fareRuleTaskInputVo.setCheckUserId(fareRuleRemoveTaskInputVo.getCheckUserId());

		// 요금 룰 삭제요청 Task 등록
		return this.registerFareRuleTask(TaskType.REMOVE, fareRuleTaskInputVo);

	}
	
	/**
	 * 요금 룰 작업정보들을 등록한다.
	 * 
	 * @param fareRuleTaskInputVo - 요금 룰 작업정보
	 * @throws Exception
	 */
	public void insertFareRuleTasks(FareRuleTaskInputVo fareRuleTaskInputVo) throws Exception{

		fareRuleRepository.insertFareRuleTask(fareRuleTaskInputVo);
		fareRuleRepository.insertFareRuleInfoTask(fareRuleTaskInputVo);
		this.insertRoutFareMappingTask(fareRuleTaskInputVo);
		
	}
	
	/**
	 * 요금 룰과 노선 매핑 작업정보 저장한다.
	 * 
	 * @param fareRuleTaskInputVo - 요금 룰 작업정보
	 * @throws Exception
	 */
	public void insertRoutFareMappingTask(FareRuleTaskInputVo fareRuleTaskInputVo) {

		Integer[] routeIds = fareRuleTaskInputVo.getRouteIds();
		
		// 예외노선 등록을 위한 입력받은 노선 ID for문
		if (ArrayUtils.isNotEmpty(routeIds)) {
			for(Integer routeId : routeIds) {
				fareRuleTaskInputVo.setRouteId(routeId);
				fareRuleRepository.insertRoutFareMappingTask(fareRuleTaskInputVo);
			}
		}
		
	}
	
	/**
	 * 작업리스트 테이블에 저장할 요금 룰 데이터 이름을 생성하여 반환한다.
	 * 
	 * @param fareRuleTaskInputVo - 요금 룰 작업정보
	 * @return
	 * @throws Exception
	 */
	public String getFareRuleDataName(FareRuleTaskInputVo fareRuleTaskInputVo) {
		List<FieldValue> busRouteClassList = commonService.selectBusRouteClassListAll();

		String busClassName = busRouteClassList.stream()
			.filter(o -> (int)o.getValue() == fareRuleTaskInputVo.getBusClass().intValue())
			.map(o -> o.getText()).collect(Collectors.joining());

		List<FieldValue> cityCodeList = commonService.selectCityCodeAll();
		String cityName = cityCodeList.stream()
			.filter(o -> (int)o.getValue() == fareRuleTaskInputVo.getCityCode().intValue())
			.map(o -> o.getText()).collect(Collectors.joining());

		StringBuilder fareRuleDataName = new StringBuilder();

		fareRuleDataName.append(cityName);
		fareRuleDataName.append(CommonConstant.BLANK);
		fareRuleDataName.append(busClassName);
		fareRuleDataName.append(CommonConstant.BLANK);
		fareRuleDataName.append(CommonConstant.FARE_TEXT);
		
		return fareRuleDataName.toString();
	}

}
