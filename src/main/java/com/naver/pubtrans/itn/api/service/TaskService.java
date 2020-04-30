package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.MemberUtil;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskAssignType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.repository.TaskRepository;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;
import com.naver.pubtrans.itn.api.vo.task.TaskAssignInfoVo;
import com.naver.pubtrans.itn.api.vo.task.TaskStatusInfoVo;
import com.naver.pubtrans.itn.api.vo.task.TaskStatusTypeCntVo;
import com.naver.pubtrans.itn.api.vo.task.input.TaskInputVo;
import com.naver.pubtrans.itn.api.vo.task.input.TaskSearchVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskListOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskStatisticsOutputVo;
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

	private final MemberService memberService;

	@Autowired
	TaskService(OutputFmtUtil outputFmtUtil, TaskRepository taskRepository, CommonService commonService,
		MemberService memberService) {
		this.outputFmtUtil = outputFmtUtil;
		this.taskRepository = taskRepository;
		this.commonService = commonService;
		this.memberService = memberService;
	}

	/**
	 * 작업 요약 목록을 가져온다
	 * @param pubTransId - 대중교통 ID
	 * @param pubTransType - 데이터 구분
	 * @param searchVo - 공통 검색조건
	 * @return
	 */
	public CommonResult getTaskSummaryList(int pubTransId, PubTransType pubTransType, SearchVo searchVo) {

		TaskSearchVo taskSearchVo = new TaskSearchVo();
		taskSearchVo.setPubTransId(pubTransId);
		taskSearchVo.setPubTransType(pubTransType);
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
		usableColumnNameList.add("task_data_source_type");
		usableColumnNameList.add("task_check_request_type");
		usableColumnNameList.add("task_comment");
		usableColumnNameList.add("check_user_id");

		List<CommonSchema> commonSchemaList = commonService.selectCommonSchemaList(
			PubTransTable.TB_Z_SVC_TASK.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);
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
		this.registerTaskAssignInfo(TaskAssignType.CHECK, taskInputVo);

		// Task 상태변경 히스토리 정보 저장(최초 등록시에는 진행중으로 등록)
		this.registerTaskStatusInfo(TaskStatusType.PROGRESS, taskInputVo);

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
		TaskStatusType taskStatusType = taskOutputVo.getTaskStatusType();

		// 작업상태가 완료이거나 예외인경우에는 수정이 안되게끔 한다
		if (taskStatusType.getCode().equals(TaskStatusType.CHECK_COMPLETE.getCode())
			|| taskStatusType.getCode().equals(TaskStatusType.EXCEPTION_COMPLETE.getCode())) {
			throw new ApiException(ResultCode.TASK_STATUS_NOT_MATCH_FAIL.getApiErrorCode(),
				ResultCode.TASK_STATUS_NOT_MATCH_FAIL.getDisplayMessage());
		}

		// Task 수정
		int updateTaskInfoCnt = taskRepository.updateTaskInfo(taskInputVo);

		// Task 할당정보 저장
		this.registerTaskAssignInfo(TaskAssignType.CHECK, taskInputVo);

		return updateTaskInfoCnt;
	}

	/**
	 * Task 할당 히스토리 저장
	 * @param taskAssignType - 할당타입
	 * @param taskInputVo - 작업 등록 정보
	 */
	public void registerTaskAssignInfo(TaskAssignType taskAssignType, TaskInputVo taskInputVo) throws Exception {

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
	 * @param taskStatusType - 작업 진행상태
	 * @param taskInputVo - 작업 등록정보
	 * @throws Exception
	 */
	public void registerTaskStatusInfo(TaskStatusType taskStatusType, TaskInputVo taskInputVo) throws Exception {
		TaskStatusInfoVo taskStatusInfoVo = new TaskStatusInfoVo();
		taskStatusInfoVo.setTaskId(taskInputVo.getTaskId());
		taskStatusInfoVo.setTaskStatusType(taskStatusType); // 수동등록인 경우 상태정보를 진행중으로 지정한다
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
		if (Objects.isNull(taskOutputVo)) {
			throw new ApiException(ResultCode.NOT_MATCH.getApiErrorCode(), ResultCode.NOT_MATCH.getDisplayMessage());
		}

		return taskOutputVo;
	}

	/**
	 * 등록자/작업자/검수자 회원ID와 이름을 추가한다
	 * @param taskAssignType - Task 할당 구분
	 * @param taskInputVo - Task 입력정보
	 * @throws Exception
	 */
	public void addTaskMemberInfo(TaskAssignType taskAssignType, TaskInputVo taskInputVo) throws Exception {
		MemberOutputVo memberOutputVo = MemberUtil.getMemberFromAccessToken();

		if (taskAssignType.equals(TaskAssignType.REGISTER)) {
			taskInputVo.setRegUserName(memberOutputVo.getUserName());
			taskInputVo.setRegUserId(memberOutputVo.getUserId());
		} else if (taskAssignType.equals(TaskAssignType.WORK)) {
			taskInputVo.setWorkUserName(memberOutputVo.getUserName());
			taskInputVo.setWorkUserId(memberOutputVo.getUserId());
		} else if (taskAssignType.equals(TaskAssignType.CHECK)) {
			MemberSearchVo memberSearchVo = new MemberSearchVo();
			memberSearchVo.setUserId(taskInputVo.getCheckUserId());

			MemberOutputVo checkMemberOutputVo = memberService.getMember(memberSearchVo);
			taskInputVo.setCheckUserId(checkMemberOutputVo.getUserId());
			taskInputVo.setCheckUserName(checkMemberOutputVo.getUserName());
		} else {
			throw new ApiException(ResultCode.SAVE_FAIL.getApiErrorCode(), ResultCode.SAVE_FAIL.getDisplayMessage());
		}
	}

	/**
	 * 작업 목록을 가져온다
	 * @param taskSearchVo - 검색조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult getTaskList(TaskSearchVo taskSearchVo) throws Exception {

		/**
		 * 1. 페이지 목록 조회
		 */

		// 전체 목록 수 가져오기
		int totalListCnt = taskRepository.getTaskListTotalCnt(taskSearchVo);

		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalListCnt, taskSearchVo.getPageNo(), taskSearchVo.getListSize());

		// 목록 조회 페이징 정보 set
		taskSearchVo.setStartPageLimit(pagingVo.getStartPageLimit());
		taskSearchVo.setEndPageLimit(pagingVo.getEndPageLimit());

		List<TaskListOutputVo> taskListOutputVoList = taskRepository.selectTaskList(taskSearchVo);

		/**
		 * 2. 검색 폼 데이터 구조 생성
		 */
		// 사용하고자 하는 컬럼 목록
		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("task_id");
		usableColumnNameList.add("provider_id");
		usableColumnNameList.add("pub_trans_id");
		usableColumnNameList.add("task_status_type");
		usableColumnNameList.add("task_data_source_type");
		usableColumnNameList.add("pub_trans_type");
		usableColumnNameList.add("pub_trans_name");
		usableColumnNameList.add("city_code");
		usableColumnNameList.add("reg_user_id");
		usableColumnNameList.add("reg_date");
		usableColumnNameList.add("work_user_id");
		usableColumnNameList.add("work_complete_date");
		usableColumnNameList.add("check_user_id");
		usableColumnNameList.add("check_complete_date");

		List<CommonSchema> tbSvcTaskSchemaList = commonService.selectCommonSchemaList(
			PubTransTable.TB_Z_SVC_TASK.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);

		/**
		 * 3. 공통 출력포맷 생성
		 */
		CommonResult commonResult = outputFmtUtil.setCommonListFmt(tbSvcTaskSchemaList, pagingVo, taskListOutputVoList);
		return commonResult;
	}


	/**
	 * 작업 요약 통계정보를 가져온다
	 * @param taskSearchVo - 검색조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult getTaskSummaryStatistics(TaskSearchVo taskSearchVo) throws Exception {

		TaskStatisticsOutputVo taskStatisticsOutputVo = new TaskStatisticsOutputVo();
		List<TaskStatusTypeCntVo> taskStatusTypeCntVoList = taskRepository.selectTaskStatusTypeCnt(taskSearchVo);
		if(taskStatusTypeCntVoList != null) {

			int totalTaskStatusCnt = 0;
			int checkCompletionCnt = 0;
			int exceptionCompletionCnt = 0;

			for(TaskStatusTypeCntVo taskStatusTypeCntVo : taskStatusTypeCntVoList) {

				int taskStatusCnt = taskStatusTypeCntVo.getTaskStatusTypeCnt();

				if(TaskStatusType.WAIT == taskStatusTypeCntVo.getTaskStatusType()) {
					taskStatisticsOutputVo.setWaitCnt(taskStatusCnt);
				}else if(TaskStatusType.PROGRESS == taskStatusTypeCntVo.getTaskStatusType()) {
					taskStatisticsOutputVo.setProgressCnt(taskStatusCnt);
				}else if(TaskStatusType.CHECKING == taskStatusTypeCntVo.getTaskStatusType()) {
					taskStatisticsOutputVo.setCheckingCnt(taskStatusCnt);
				}else if(TaskStatusType.CHECK_COMPLETE == taskStatusTypeCntVo.getTaskStatusType()) {
					checkCompletionCnt = taskStatusCnt;
					taskStatisticsOutputVo.setCheckCompletionCnt(taskStatusCnt);
				}else if(TaskStatusType.EXCEPTION_COMPLETE == taskStatusTypeCntVo.getTaskStatusType()) {
					exceptionCompletionCnt = taskStatusCnt;
					taskStatisticsOutputVo.setExceptionCompletionCnt(taskStatusCnt);
				}

				totalTaskStatusCnt += taskStatusCnt;
			}

			// 전체 개수 set
			taskStatisticsOutputVo.setTotalCnt(totalTaskStatusCnt);

			// 진행률 set
			if(checkCompletionCnt + exceptionCompletionCnt > 0) {
				// 진행률 : 전체 작업수 / (검수완료 + 예외완료)
				double progressRate = totalTaskStatusCnt / (checkCompletionCnt + exceptionCompletionCnt);
				taskStatisticsOutputVo.setProgressRate(Math.round(progressRate*CommonConstant.NUMBER_ONE_HUNDRED)/CommonConstant.DECIMAL_ONE_HUNDRED);	//소수점 2자리
			}
		}

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(taskStatisticsOutputVo);
		return commonResult ;
	}

	/**
	 * 스키마 정보가 포한된 작업 상세정보를 가져온다
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	public CommonResult getTaskInfoWithSchema(long taskId) throws Exception {
		TaskOutputVo taskOutputVo = this.getTaskInfo(taskId);

		// 사용하고자 하는 컬럼 목록
		ArrayList<String> usableColumnNameList = new ArrayList<>();

		usableColumnNameList.add("task_id");
		usableColumnNameList.add("provider_id");
		usableColumnNameList.add("pub_trans_id");
		usableColumnNameList.add("task_status_type");
		usableColumnNameList.add("task_data_source_type");
		usableColumnNameList.add("task_check_request_type");
		usableColumnNameList.add("pub_trans_type");
		usableColumnNameList.add("pub_trans_name");

		List<CommonSchema> tbSvcTaskSchemaList = commonService.selectCommonSchemaList(
			PubTransTable.TB_Z_SVC_TASK.getName(), CommonConstant.USABLE_COLUMN, usableColumnNameList);

		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(tbSvcTaskSchemaList, taskOutputVo);
		return commonResult;
	}

}
