package com.naver.pubtrans.itn.api.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.InitBinder;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.service.TaskService;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.task.input.TaskSearchVo;
import com.naver.pubtrans.itn.api.vo.task.output.TaskOutputVo;

/**
 * 작업관리 컨트롤러
 * @author adtec10
 *
 */
@RestController
public class TaskController {

	private final TaskService taskService;
	private final OutputFmtUtil outputFmtUtil;

	@Autowired
	TaskController(TaskService taskService, OutputFmtUtil outputFmtUtil){
		this.taskService = taskService;
		this.outputFmtUtil = outputFmtUtil;
	}


	/**
	 * 작업 목록 조회
	 * @param taskSearchVo - 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/task")
	public CommonOutput listTask(TaskSearchVo taskSearchVo) throws Exception {
		CommonResult commonResult = taskService.getTaskList(taskSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 작업 요약 통게정보 조회
	 * @param taskSearchVo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/task/summaryStatistics")
	public CommonOutput getTaskSummaryStatistics(TaskSearchVo taskSearchVo) throws Exception {
		CommonResult commonResult = taskService.getTaskSummaryStatistics(taskSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 작업정보 조회
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/task/{taskId}")
	public CommonOutput infoTask(@PathVariable long taskId) throws Exception {
		CommonResult commonResult = taskService.getTaskInfoWithSchema(taskId);
		return new CommonOutput(commonResult);
	}
}
