package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.task.TaskAssignInfoVo;
import com.naver.pubtrans.itn.api.vo.task.TaskStatusInfoVo;
import com.naver.pubtrans.itn.api.vo.task.input.TaskInputVo;
import com.naver.pubtrans.itn.api.vo.task.input.TaskSearchVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskSummaryOutputVo;

/**
 * 작업관리 Repository
 * @author adtec10
 *
 */
@Repository
public interface TaskRepository {

	/**
	 * 작업 요약목록 수를 가져온다
	 * @param taskSearchVo - 작업목록 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	int getTaskSummaryListTotalCnt(TaskSearchVo taskSearchVo) throws DataAccessException;

	/**
	 * 작업 요약목록을 가져온다
	 * @param taskSearchVo - 작업목록 검색조건
	 * @return
	 * @throws DataAccessException
	 */
	List<TaskSummaryOutputVo> selectTaskSummaryList(TaskSearchVo taskSearchVo) throws DataAccessException;


	/**
	 * 작업 상세 기본정보를 가져온다
	 * @param taskId - 작업ID
	 * @return
	 * @throws DataAccessException
	 */
	TaskOutputVo getTaskInfo(long taskId) throws DataAccessException;


	/**
	 * 작업 기본정보를 저장한다
	 * @param taskInputVo - 작업 등록정보
	 * @return
	 * @throws DataAccessException
	 */
	void insertTaskInfo(TaskInputVo taskInputVo) throws DataAccessException;

	/**
	 * 작업 할당정보를 저장한다
	 * @param taskAssignInfoVo - 작업 할당정보
	 * @return
	 * @throws DataAccessException
	 */
	void insertTaskAssignInfo(TaskAssignInfoVo taskAssignInfoVo) throws DataAccessException;

	/**
	 * 작업 상태정보를 저장한다
	 * @param taskStatusInfoVo - 작업 상태정보
	 * @return
	 * @throws DataAccessException
	 */
	void insertTaskStatusInfo(TaskStatusInfoVo taskStatusInfoVo) throws DataAccessException;


	/**
	 * 작업 기본정보를 수정한다
	 * @param taskInputVo - 작업 등록정보
	 * @return
	 * @throws DataAccessException
	 */
	int updateTaskInfo(TaskInputVo taskInputVo) throws DataAccessException;

}
