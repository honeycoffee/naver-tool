package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskAssignType;
import com.naver.pubtrans.itn.api.consts.TaskStatus;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.TaskRepository;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.task.TaskAssignInfoVo;
import com.naver.pubtrans.itn.api.vo.task.TaskStatusInfoVo;
import com.naver.pubtrans.itn.api.vo.task.input.TaskInputVo;
import com.naver.pubtrans.itn.api.vo.task.input.TaskSearchVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskSummaryOutputVo;

/**
 * 내재화 툴 작업관리 서비스
 * @author adtec10
 *
 */
@Service
public class TaskService {


	private final OutputFmtUtil outputFmtUtil;

	private final TaskRepository taskRepository;

	private final CommonService commonService;


	@Autowired
	TaskService(OutputFmtUtil outputFmtUtil, TaskRepository taskRepository, CommonService commonService) {
		this.outputFmtUtil = outputFmtUtil;
		this.taskRepository = taskRepository;
		this.commonService = commonService;
	}


	/**
	 * 작업 요약 목록을 가져온다
	 * @param pubTransId - 대중교통 ID
	 * @param taskDataType - 데이터 구분
	 * @param searchVo - 공통 검색조건
	 * @return
	 */
	public CommonResult getTaskSummaryList(int pubTransId, String taskDataType, SearchVo searchVo){

		TaskSearchVo taskSearchVo = new TaskSearchVo();
		taskSearchVo.setPubTransId(pubTransId);
		taskSearchVo.setTaskDataType(taskDataType);
		taskSearchVo.setPageNo(searchVo.getPageNo());
		taskSearchVo.setListSize(searchVo.getListSize());


		// 목록수
		int totalListCnt = taskRepository.getTaskSummaryListTotalCnt(taskSearchVo);

		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalListCnt, taskSearchVo.getPageNo(), taskSearchVo.getListSize());


		// 목록 조회 페이징 정보 set
		taskSearchVo.setStartPageLimit(pagingVo.getStartPageLimit());
		taskSearchVo.setEndPageLimit(pagingVo.getEndPageLimit());


		// 목록 조회
		List<TaskSummaryOutputVo> taskSummaryOutputVoList = taskRepository.selectTaskSummaryList(taskSearchVo);


		CommonResult commonResult = outputFmtUtil.setCommonListFmt(pagingVo, taskSummaryOutputVoList);

		return commonResult;
	}


	/**
	 * 작업정보 테이블 최소 스키마
	 * @return
	 * @throws Exception
	 */
	public List<CommonSchema> selectTaskSchemaMinimal() throws Exception {
		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("task_id");
		usableColumnNameList.add("task_comment");
		usableColumnNameList.add("check_user_id");

		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(PubTransTable.TB_Z_SVC_TASK.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);
		return commonSchemaList;
	}


	/**
	 * 작업정보를 등록한다
	 * @param taskInputVo - 작업 등록 정보
	 * @return
	 * @throws Exception
	 */
	public long registerTaskInfoAll(TaskInputVo taskInputVo) throws Exception {


		// Task 기본정보 삽입
		taskRepository.insertTaskInfo(taskInputVo);

		// Task 할당정보 저장
		this.registerTaskAssignInfo(TaskAssignType.CHECK.getCode(), taskInputVo);

		// Task 상태변경 히스토리 정보 저장(최초 등록시에는 진행중으로 등록)
		this.registerTaskStatusInfo(TaskStatus.PROGRESS.getCode(), taskInputVo);


		return taskInputVo.getTaskId();
	}


	/**
	 * 작업정보를 수정한다
	 * <pre>
	 * 작업상태가 '대기', '진행' 인 경우에만 수정이 가능하다.
	 * </pre>
	 * @param taskInputVo - 작업 수정 정보
	 * @return
	 * @throws Exception
	 */
	public int modifyTaskInfoAll(TaskInputVo taskInputVo) throws Exception {


		TaskOutputVo taskOutputVo = this.getTaskInfo(taskInputVo.getTaskId());
		String taskStatus = taskOutputVo.getTaskStatus();

		// 작업상태가 완료이거나 예외인경우에는 수정이 안되게끔 한다
		if(taskStatus.equals(TaskStatus.COMPLETE.getCode()) || taskStatus.equals(TaskStatus.IGNORE.getCode())) {
			throw new ApiException(ResultCode.TASK_STATUS_NOT_MATCH_FAIL.getApiErrorCode(), ResultCode.TASK_STATUS_NOT_MATCH_FAIL.getDisplayMessage());
		}


		// Task 수정
		int updateTaskInfoCnt = taskRepository.updateTaskInfo(taskInputVo);


		// Task 할당정보 저장
		this.registerTaskAssignInfo(TaskAssignType.CHECK.getCode(), taskInputVo);

		return updateTaskInfoCnt;
	}


	/**
	 * Task 할당 히스토리 저장
	 * @param taskAssignType - 할당타입
	 * @param taskInputVo - 작업 등록 정보
	 */
	public void registerTaskAssignInfo(String taskAssignType, TaskInputVo taskInputVo) throws Exception {

		TaskAssignInfoVo taskAssignInfoVo = new TaskAssignInfoVo();
		taskAssignInfoVo.setTaskId(taskInputVo.getTaskId());
		taskAssignInfoVo.setTaskAssignType(taskAssignType);
		taskAssignInfoVo.setAssigneeUserId(taskInputVo.getCheckUserId());
		taskAssignInfoVo.setAssigneeUserName(taskInputVo.getCheckUserName());
		taskAssignInfoVo.setRegUserId(taskInputVo.getRegUserId());
		taskAssignInfoVo.setRegUserName(taskInputVo.getRegUserName());

		// 할당정보 저장
		taskRepository.insertTaskAssignInfo(taskAssignInfoVo);
	}


	/**
	 * Task 상태 변경 히스토리 저장
	 * @param taskStatus - 작업 진행상태
	 * @param taskInputVo - 작업 등록정보
	 * @throws Exception
	 */
	public void registerTaskStatusInfo(String taskStatus, TaskInputVo taskInputVo) throws Exception {
		TaskStatusInfoVo taskStatusInfoVo = new TaskStatusInfoVo();
		taskStatusInfoVo.setTaskId(taskInputVo.getTaskId());
		taskStatusInfoVo.setTastStatus(taskStatus);		// 수동등록인 경우 상태정보를 진행중으로 지정한다
		taskStatusInfoVo.setRegUserId(taskInputVo.getRegUserId());
		taskStatusInfoVo.setRegUserName(taskInputVo.getRegUserName());

		// 히스토리 정보 저장
		taskRepository.insertTaskStatusInfo(taskStatusInfoVo);
	}


	/**
	 * Task 기본정보를 가져온다
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	public TaskOutputVo getTaskInfo(long taskId) throws Exception {

		TaskOutputVo taskOutputVo = taskRepository.getTaskInfo(taskId);

		// 일치하는 작업정보가 없는경우 오류처리
		if(Objects.isNull(taskOutputVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}


		return taskOutputVo;
	}

}
