package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskDataType;
import com.naver.pubtrans.itn.api.consts.TaskStatus;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.FareRepository;
import com.naver.pubtrans.itn.api.repository.TaskRepository;
import com.naver.pubtrans.itn.api.vo.bus.route.output.BusRouteListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.fare.FareTaskVo;
import com.naver.pubtrans.itn.api.vo.fare.FareVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareSearchVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.output.FareOutputVoWithRouteList;
import com.naver.pubtrans.itn.api.vo.fare.output.FareTaskOutputVoWithRouteList;
import com.naver.pubtrans.itn.api.vo.fare.output.IgnoredFareListOutputVo;
import com.naver.pubtrans.itn.api.vo.task.input.TaskInputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 네이버 대중교통 내재화 공지사항 서비스
 * 
 * @author westwind
 *
 */
@Service
public class FareService {

	private final OutputFmtUtil outputFmtUtil;

	private final CommonService commonService;

	private final TaskService taskService;

	private final FareRepository fareRepository;

	private final TaskRepository taskRepository;

	@Autowired
	FareService(OutputFmtUtil outputFmtUtil, CommonService commonService, TaskService taskService,
		FareRepository fareRepository, TaskRepository taskRepository) {
		this.outputFmtUtil = outputFmtUtil;
		this.commonService = commonService;
		this.taskService = taskService;
		this.fareRepository = fareRepository;
		this.taskRepository = taskRepository;
	}

	/**
	 * 요금 룰 작업 히스토리 요약목록을 가져온다
	 * @param fareId - 요금 룰 ID
	 * @param searchVo - 페이징 Vo
	 * @return
	 * @throws Exception
	 */
	public CommonResult getFareTaskSummaryList(int fareId, SearchVo searchVo) throws Exception {

		CommonResult commonResult = taskService.getTaskSummaryList(fareId, TaskDataType.STOP.getCode(), searchVo);
		return commonResult;
	}

	/**
	 * 요금 룰을 조회한다
	 * @param fareSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult getFareRule(FareSearchVo fareSearchVo) throws Exception {

		// 요금 정보
		FareVo fareVo = new FareVo();

		// fareId가 없을 경우 기본 요금 룰 조회
		if (fareSearchVo.getFareId() == null) {
			fareVo = fareRepository.getBaseFareRule(fareSearchVo);
		} else {
			fareVo = fareRepository.getIngoredFareRule(fareSearchVo);
		}

		if (Objects.isNull(fareVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

		FareOutputVoWithRouteList fareOutputVoWithRouteList = new FareOutputVoWithRouteList();
		BeanUtils.copyProperties(fareVo, fareOutputVoWithRouteList);

		// 관련 노선 목록 가져오기
		List<BusRouteListOutputVo> busRouteListOutputVoList = this
			.selectBusRouteFareMappingList(fareOutputVoWithRouteList.getFareId());

		if (!Objects.isNull(busRouteListOutputVoList)) {
			fareOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);
		}

		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectFareSchemaAll();

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareOutputVoWithRouteList);

		return commonResult;
	}

	/**
	 * 요금 룰 관련 노선 목록을 가져온다.
	 * @param fareId - 요금 룰 ID
	 * @return
	 */
	public List<BusRouteListOutputVo> selectBusRouteFareMappingList(int fareId) {

		List<BusRouteListOutputVo> busRouteListOutputVoList = fareRepository.selectBusRouteFareMappingList(fareId);

		return busRouteListOutputVoList;

	}

	/**
	 * 예외 요금 목록을 가져온다.
	 * @param fareSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult selectIgnoredFareRuleList(FareSearchVo fareSearchVo) throws Exception {

		
		System.out.println(fareSearchVo.getBusClass());
		System.out.println(fareSearchVo.getCityCode());
		// 정류장 정보
		List<IgnoredFareListOutputVo> ignoredFareListOutputVoList = fareRepository
			.selectIgnoredFareRuleList(fareSearchVo);
		
		AtomicInteger index = new AtomicInteger(1);   

		ignoredFareListOutputVoList.stream().forEach(o -> o.setOrder(index.getAndIncrement()));
		ignoredFareListOutputVoList.stream().forEach(o -> o.setIgnoredFareName());

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(ignoredFareListOutputVoList);

		return commonResult;
	}

	/**
	 * 정류장 작업정보
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	public CommonResult getFareTaskInfo(long taskId) throws Exception {

		// 요금 룰 작업정보
		FareTaskVo fareTaskVo = fareRepository.getFareRuleTask(taskId);

		// 작업 기본 정보
		TaskOutputVo taskOutputVo = taskRepository.getTaskInfo(taskId);

		if (Objects.isNull(fareTaskVo) || Objects.isNull(taskOutputVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

//		if (StringUtils.isNotEmpty(taskOutputVo.getBisAutoChangeData())) {
//
//			// Json형태의 Text를 vo로 변경한다
//			BusStopChangeVo busStopChangeVo = new ObjectMapper().readValue(taskOutputVo.getBisAutoChangeData(),
//				BusStopChangeVo.class);
//			taskOutputVo.setBisChangeDataInfo(busStopChangeVo);
//		}

		FareTaskOutputVoWithRouteList fareTaskOutputVoWithRouteList = new FareTaskOutputVoWithRouteList();

		BeanUtils.copyProperties(fareTaskVo, fareTaskOutputVoWithRouteList);
		fareTaskOutputVoWithRouteList.setTaskInfo(taskOutputVo);
		
		// 관련 노선 목록 가져오기
		List<BusRouteListOutputVo> busRouteListOutputVoList = this
			.selectBusRouteFareMappingList(fareTaskOutputVoWithRouteList.getFareId());

		if (!Objects.isNull(busRouteListOutputVoList)) {
			fareTaskOutputVoWithRouteList.setBusRouteInfoList(busRouteListOutputVoList);
		}

		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectFareSchemaAll();

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, fareTaskOutputVoWithRouteList);

		return commonResult;
	}

	/**
	 * 버스 정류장 상세정보의 전체 스키마 정보를 가져온다
	 * @return
	 * @throws Exception
	 */
	public List<CommonSchema> selectFareSchemaAll() throws Exception {
		List<CommonSchema> commonSchemaList = new ArrayList<>();

		commonSchemaList.addAll(this.selectFareSchema());
		commonSchemaList.addAll(this.selectRouteFareMappingSchema());

		// 동일 컬럼에 대해 중복을 제거
		List<CommonSchema> distinctCommonSchemaVoList = outputFmtUtil.distinctCommonSchemaList(commonSchemaList);

		return distinctCommonSchemaVoList;
	}

	/**
	 * 요금 룰 테이블 스키마
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectFareSchema() throws Exception {

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_FARE_RULE.getName(),
			null, null);

		return commonSchemaList;
	}

	/**
	 * 노선과 요금 룰 매핑정보 테이블 스키마
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
	 * 버스정류장 작업정보를 등록한다
	 * @param taskType - 작업 등록구분(컨텐츠 추가, 수정, 삭제)
	 * @param fareTaskInputVo - 요금 룰 입력정보
	 * @throws Exception
	 */
	@Transactional
	public void registerFareTask(String taskType, FareTaskInputVo fareTaskInputVo) throws Exception {

		// 정류장 신규등록
		if (taskType.equals(TaskType.REGISTER.getCode())) {

			/**
			 * 정류장 임시 ID 설정
			 *
			 * Task 검수 완료 후 배포시 정류장 테이블의 auto increment값을 사용하여 ID를 부여한다.
			 *
			 */
			fareTaskInputVo.setFareId(0);
		}

		// Task 기본정보
		TaskInputVo taskInputVo = new TaskInputVo();
		taskInputVo.setTaskType(taskType);
		taskInputVo.setPubTransId(fareTaskInputVo.getFareId());

		taskInputVo.setTaskStatus(TaskStatus.PROGRESS.getCode());
		taskInputVo.setTaskDataType(TaskDataType.FARE.getCode());
		
		List<FieldValue> cityCodeList = commonService.selectCityCodeAll();
		
		List<FieldValue> busRouteClassList = commonService.selectBusRouteClassListAll();
		
		busRouteClassList.stream().forEach(o -> System.out.println(o.getValue()));
		
//		List<CityCodeVo> citycodeVoList = commonRepository.selectCityCodeList();
//		
//		String fareDataName = 
//
//		taskInputVo.setTaskDataName(String.valueOf(fareTaskInputVo.getCityCode()));
//		taskInputVo.setTaskComment(fareTaskInputVo.getCommand());
//		taskInputVo.setTaskRegisterType(CommonConstant.MANUAL);
//
//		MemberOutputVo memberOutputVo = MemberUtil.getMemberFromToken();
//
//		// TODO 회원정보 파트 개발 완료후 공통에서 ID와 이름등을 가져오는것으로 변경 필요.
//		taskInputVo.setRegUserName(memberOutputVo.getUserName());
//		taskInputVo.setRegUserId(memberOutputVo.getUserId());
//		taskInputVo.setWorkUserName(memberOutputVo.getUserName());
//		taskInputVo.setWorkUserId(memberOutputVo.getUserId());
//
//		// TODO 권환관련 파트 개발 완료후 검수자 ID, 이름 수정
//		taskInputVo.setCheckUserId("kr94666");
//		taskInputVo.setCheckUserName("안경현");
//
//		/**
//		 * Task를 구성하는 전체 정보를 저장한다
//		 *
//		 * 1. Task 기본정보 저장
//		 * 2. Task 할당정보 저장
//		 * 3. Task 상태변경 히스토리 정보 저장
//		 */
//		long taskId = taskService.registerTaskInfoAll(taskInputVo);
//
//		fareTaskInputVo.setTaskId(taskId);
//
//		// 정류장 연관 Task 테이블에 저장
//		fareRepository.insertFareTask(fareTaskInputVo);
//
//		// BIS 매핑정보
//		if (StringUtils.isNotEmpty(fareTaskInputVo.getFareId())) {
//			busStopRepository.insertBusStopMappingTask(fareTaskInputVo);
//		}

	}

//	/**
//	 * 버스정류장 작업정보를 수정한다
//	 * @param fareTaskInputVo - 버스 정류장 작업정보
//	 * @throws Exception
//	 */
//	@Transactional
	public void updateFareTask(FareTaskInputVo fareTaskInputVo) throws Exception {

//		// Task 기본정보
//		TaskInputVo taskInputVo = new TaskInputVo();
//		taskInputVo.setTaskId(fareTaskInputVo.getTaskId());
//		taskInputVo.setProviderId(fareTaskInputVo.getProviderId());
//		taskInputVo.setTaskStatus(TaskStatus.PROGRESS.getCode());
//		taskInputVo.setTaskDataName(fareTaskInputVo.getStopName());
//		taskInputVo.setTaskComment(fareTaskInputVo.getTaskComment());
//
//		// TODO 회원정보 파트 개발 완료후 공통에서 ID와 이름등을 가져오는것으로 변경 필요.
//		taskInputVo.setRegUserName("안경현");
//		taskInputVo.setRegUserId("kr94666");
//
//		// TODO 권환관련 파트 개발 완료후 검수자 ID, 이름 변경 필요
//		taskInputVo.setCheckUserId("kr94666");
//		taskInputVo.setCheckUserName("안경현");
//
//		/**
//		 * Task를 구성하는 전체 정보를 수정한다
//		 *
//		 * 1. Task 기본정보 저장
//		 * 2. Task 할당정보 저장
//		 */
//		int updateTaskInfoCnt = taskService.modifyTaskInfoAll(taskInputVo);
//
//		// 정류장 연관 Task 데이터 업데이트
//		int updateBusStopTaskCnt = busStopRepository.updateBusStopTask(fareTaskInputVo);
//		int updateBusStopSubTaskCnt = busStopRepository.updateBusStopSubInfoTask(fareTaskInputVo);
//
//		// BIS 매핑정보 Task 업데이트
//		busStopRepository.updateBusStopMappingTask(fareTaskInputVo);
//
//		// 저장 오류 처리
//		if (!(updateTaskInfoCnt == 1 && updateBusStopTaskCnt == 1 && updateBusStopSubTaskCnt == 1)) {
//			throw new ApiException(ResultCode.SAVE_FAIL.getApiErrorCode(), ResultCode.SAVE_FAIL.getDisplayMessage());
//		}
	}

	/**
	 * 버스정류장을 삭제요청을 진행한다
	 * <pre>
	 * 삭제요청 작업(Task)을 생성한다
	 * 이후 검수자 확인 및 배포처리(배포관리 메뉴)를 통해 최종 마스터 테이블에 반영
	 * </pre>
	 * @param busStopRemoveTaskInputVo - 버스정류장 삭제정보
	 * @throws Exception
	 */
	public void registerFareRemoveTask(FareRemoveTaskInputVo fareRemoveTaskInputVo) throws Exception {

//		int stopId = busStopRemoveTaskInputVo.getStopId();
//
//		// 기본정보 가져오기
//		BusStopVo busStopVo = busStopRepository.getBusStop(stopId);
//
//		if (Objects.isNull(busStopVo)) {
//			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
//		}
//
//		// 경유노선 정보 가져오기
//		List<BusRouteVo> busRouteVoList = busStopRepository.selectBusRouteList(stopId);
//
//		if (busRouteVoList.size() > 0) {
//			throw new ApiException(ResultCode.STOP_REMOVE_EXISTS_BUS_ROUTE.getApiErrorCode(),
//				ResultCode.STOP_REMOVE_EXISTS_BUS_ROUTE.getDisplayMessage());
//		}
//
//		FareTaskInputVo fareTaskInputVo = new FareTaskInputVo();
//		BeanUtils.copyProperties(busStopVo, fareTaskInputVo);
//
//		fareTaskInputVo.setTaskComment(busStopRemoveTaskInputVo.getTaskComment());
//		fareTaskInputVo.setCheckUserId(busStopRemoveTaskInputVo.getCheckUserId());
//
//		// 정류장 삭제요청 Task 등록
//		this.registerBusStopTask(TaskType.REMOVE.getCode(), fareTaskInputVo);

	}

}
