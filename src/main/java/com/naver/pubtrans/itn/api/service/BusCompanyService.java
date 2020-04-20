package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskAssignType;
import com.naver.pubtrans.itn.api.consts.TaskDataType;
import com.naver.pubtrans.itn.api.consts.TaskStatus;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.BusCompanyRepository;
import com.naver.pubtrans.itn.api.repository.TaskRepository;
import com.naver.pubtrans.itn.api.vo.bus.company.BusCompanyVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanyRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanySearchVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanyTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.company.output.BusCompanyListOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.company.output.BusCompanyTaskOutputVo;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.task.input.TaskInputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 버스 운수사 관리 서비스
 *
 * @author westwind
 *
 */

@Service
public class BusCompanyService {

	private final OutputFmtUtil outputFmtUtil;

	private final CommonService commonService;

	private final TaskService taskService;

	private final BusCompanyRepository busCompanyRepository;

	private final TaskRepository taskRepository;

	@Autowired
	BusCompanyService(OutputFmtUtil outputFmtUtil, CommonService commonService, TaskService taskService, BusCompanyRepository busCompanyRepository, TaskRepository taskRepository){
		this.outputFmtUtil = outputFmtUtil;
		this.commonService = commonService;
		this.taskService = taskService;
		this.busCompanyRepository = busCompanyRepository;
		this.taskRepository = taskRepository;
	}



	/**
	 * 버스 운수사 목록을 가져온다
	 * @param busCompanySearchVo - 목록 검색 조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusCompanyList(BusCompanySearchVo busCompanySearchVo) throws Exception {

		/**
		 * 1. 페이지 목록 조회
		 */

		// 전체 목록 수 가져오기
		int totalListCnt = busCompanyRepository.getBusCompanyListTotalCnt(busCompanySearchVo);


		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalListCnt, busCompanySearchVo.getPageNo(), busCompanySearchVo.getListSize());

		// 목록 조회 페이징 정보 set
		busCompanySearchVo.setStartPageLimit(pagingVo.getStartPageLimit());
		busCompanySearchVo.setEndPageLimit(pagingVo.getEndPageLimit());


		// 목록 조회
		List<BusCompanyListOutputVo> busCompanyListOutputVoList = busCompanyRepository.selectBusCompanyList(busCompanySearchVo);


		/**
		 * 2. 검색 폼 데이터 구조 생성
		 */

		// 사용하고자 하는 컬럼 목록
		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("company_id");
		usableColumnNameList.add("company_name");
		usableColumnNameList.add("city_code");
		usableColumnNameList.add("tel");


		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_BUS_COMPANY.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);



		/**
		 * 3. 공통 출력포맷 생성
		 */
		CommonResult commonResult = outputFmtUtil.setCommonListFmt(commonSchemaList, pagingVo, busCompanyListOutputVoList);

		return commonResult;
	}


	/**
	 * 버스 운수사 작업 히스토리 요약목록을 가져온다
	 * @param companyId - 운수사 ID
	 * @param searchVo - 페이징 Vo
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusCompanyTaskSummaryList(int companyId, SearchVo searchVo) throws Exception {

		CommonResult commonResult = taskService.getTaskSummaryList(companyId, TaskDataType.COMPANY.getCode(), searchVo);
		return commonResult;
	}


	/**
	 * 버스 운수사 정보를 조회한다
	 * @param companyId - 운수사 ID
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusCompanyInfo(int companyId) throws Exception {

		// 버스 운수사 정보
		BusCompanyVo busCompanyVo = busCompanyRepository.getBusCompany(companyId);

		if(Objects.isNull(busCompanyVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectBusCompanySchemaAll();

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, busCompanyVo);


		return commonResult;
	}


	/**
	 * 버스 운수사 작업정보
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusCompanyTaskInfo(long taskId) throws Exception {

		// 버스 운수사 작업정보
		BusCompanyTaskOutputVo busCompanyTaskOutputVo = busCompanyRepository.getBusCompanyTask(taskId);

		// 작업 기본 정보
		TaskOutputVo taskOutputVo = taskRepository.getTaskInfo(taskId);

		if(Objects.isNull(busCompanyTaskOutputVo) || Objects.isNull(taskOutputVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}
		
		busCompanyTaskOutputVo.setTaskInfo(taskOutputVo);

		// 데이터 스키마 조회
		List<CommonSchema> commonSchemaList = this.selectBusCompanySchemaAll();

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(commonSchemaList, busCompanyTaskOutputVo);

		return commonResult;
	}



	/**
	 * 버스 운수사 상세정보의 전체 스키마 정보를 가져온다
	 * @return
	 * @throws Exception
	 */
	public List<CommonSchema> selectBusCompanySchemaAll() throws Exception {
		List<CommonSchema> commonSchemaList = new ArrayList<>();

		commonSchemaList.addAll(this.selectBusCompanySchema());
		commonSchemaList.addAll(taskService.selectTaskSchemaMinimal());

		// 동일 컬럼에 대해 중복을 제거
		List<CommonSchema> distinctCommonSchemaVoList = outputFmtUtil.distinctCommonSchemaList(commonSchemaList);

		return distinctCommonSchemaVoList;
	}


	/**
	 * 버스 운수사 기본정보 테이블 스키마
	 * @return
	 * @throws Exception
	 */
	private List<CommonSchema> selectBusCompanySchema() throws Exception {

		// 검색 폼 데이터 구조
		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_BUS_COMPANY.getName(), null, null);

		return commonSchemaList;
	}


	/**
	 * 버스 운수사 작업정보를 등록한다
	 * @param taskType - 작업 등록구분(컨텐츠 추가, 수정, 삭제)
	 * @param busCompanyTaskInputVo - 운수사 작업정보
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public CommonResult registerBusCompanyTask(String taskType, BusCompanyTaskInputVo busCompanyTaskInputVo) throws Exception {


		// 운수사 신규등록
		if(taskType.equals(TaskType.REGISTER.getCode())) {

			/**
			 * 운수사 임시 ID 설정
			 *
			 * Task 검수 완료 후 배포시 운수사 테이블의 auto increment값을 사용하여 ID를 부여한다.
			 *
			 */
			busCompanyTaskInputVo.setCompanyId(0);
		}



		// Task 기본정보
		TaskInputVo taskInputVo = new TaskInputVo();
		taskInputVo.setTaskType(taskType);
		taskInputVo.setPubTransId(busCompanyTaskInputVo.getCompanyId());

		taskInputVo.setTaskStatus(TaskStatus.PROGRESS.getCode());
		taskInputVo.setTaskDataType(TaskDataType.COMPANY.getCode());
		taskInputVo.setTaskDataName(busCompanyTaskInputVo.getCompanyName());
		taskInputVo.setTaskComment(busCompanyTaskInputVo.getTaskComment());
		taskInputVo.setTaskRegisterType(CommonConstant.MANUAL);
		taskInputVo.setCheckUserId(busCompanyTaskInputVo.getCheckUserId());

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

		busCompanyTaskInputVo.setTaskId(taskId);


		// 운수사 연관 Task 테이블에 저장
		busCompanyRepository.insertBusCompanyTask(busCompanyTaskInputVo);


		// 성공시 작업ID 리턴
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(CommonConstant.KEY_TASK, taskId);
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(resultMap);

		return commonResult;
	}


	/**
	 * 버스 운수사 작업정보를 수정한다
	 * @param busCompanyTaskInputVo - 운수사 작업정보
	 * @throws Exception
	 */
	@Transactional
	public void modifyBusCompanyTask(BusCompanyTaskInputVo busCompanyTaskInputVo) throws Exception {

		// Task 기본정보
		TaskInputVo taskInputVo = new TaskInputVo();
		taskInputVo.setTaskId(busCompanyTaskInputVo.getTaskId());
		taskInputVo.setTaskStatus(TaskStatus.PROGRESS.getCode());
		taskInputVo.setTaskDataName(busCompanyTaskInputVo.getCompanyName());
		taskInputVo.setTaskComment(busCompanyTaskInputVo.getTaskComment());
		taskInputVo.setCheckUserId(busCompanyTaskInputVo.getCheckUserId());

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

		// 운수사 연관 Task 데이터 업데이트
		int updateBusCompanyTaskCnt = busCompanyRepository.updateBusCompanyTask(busCompanyTaskInputVo);


		// 저장 오류 처리
		if(updateTaskInfoCnt != 1 && updateBusCompanyTaskCnt != 1) {
			throw new ApiException(ResultCode.SAVE_FAIL.getApiErrorCode(), ResultCode.SAVE_FAIL.getDisplayMessage());
		}
	}


	/**
	 * 버스 운수사 삭제요청을 진행한다
	 * <pre>
	 * 삭제요청 작업(Task)을 생성한다
	 * 이후 검수자 확인 및 배포처리(배포관리 메뉴)를 통해 최종 마스터 테이블에 반영
	 * </pre>
	 * @param busCompanyRemoveTaskInputVo - 운수사 삭제정보
	 * @return
	 * @throws Exception
	 */
	public CommonResult registerBusCompanyRemoveTask(BusCompanyRemoveTaskInputVo busCompanyRemoveTaskInputVo) throws Exception {


		int companyId = busCompanyRemoveTaskInputVo.getCompanyId();

		// 기본정보 가져오기
		BusCompanyVo busCompanyVo = busCompanyRepository.getBusCompany(companyId);

		if(Objects.isNull(busCompanyVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

		BusCompanyTaskInputVo busCompanyTaskInputVo = new BusCompanyTaskInputVo();
		BeanUtils.copyProperties(busCompanyVo, busCompanyTaskInputVo);

		busCompanyTaskInputVo.setTaskComment(busCompanyRemoveTaskInputVo.getTaskComment());
		busCompanyTaskInputVo.setCheckUserId(busCompanyRemoveTaskInputVo.getCheckUserId());


		// 운수사 삭제요청 Task 등록
		return this.registerBusCompanyTask(TaskType.REMOVE.getCode(), busCompanyTaskInputVo);

	}

}
