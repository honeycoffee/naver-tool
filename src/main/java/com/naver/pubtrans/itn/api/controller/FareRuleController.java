package com.naver.pubtrans.itn.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.service.FareRuleService;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleSearchVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRuleTaskInputVo;

/**
 * 네이버 대중교통 DB 내재화 요금 룰 컨트롤러
 * 
 * @author westwind
 *
 */
@Slf4j
@RestController
public class FareRuleController {

	private final FareRuleService fareRuleService;

	private final OutputFmtUtil outputFmtUtil;

	@Autowired
	FareRuleController(FareRuleService fareRuleService, OutputFmtUtil outputFmtUtil) {
		this.outputFmtUtil = outputFmtUtil;
		this.fareRuleService = fareRuleService;
	}

	/**
	 * 기본 요금 룰 상세정보를 조회한다
	 * @param fareRuleSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/baseFareRule")
	public CommonOutput infoBaseFareRule(@Valid FareRuleSearchVo fareRuleSearchVo) throws Exception {
		CommonResult commonResult = fareRuleService.getBaseFareRule(fareRuleSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 예외 요금 룰 리스트를 가져온다
	 * @param fareRuleSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/exceptionalFareRule")
	public CommonOutput selectExceptionalFareRuleList(@Valid FareRuleSearchVo fareRuleSearchVo) throws Exception {
		CommonResult commonResult = fareRuleService.selectExceptionalFareRuleList(fareRuleSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 예외 요금 룰 상세정보를 조회한다
	 * @param fareId - 요금 룰 ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/exceptionalFareRule/{fareId}")
	public CommonOutput infoExceptionalFareRule(@PathVariable int fareId) throws Exception {

		FareRuleSearchVo fareRuleSearchVo = new FareRuleSearchVo();
		fareRuleSearchVo.setFareId(fareId);

		CommonResult commonResult = fareRuleService.getExceptionalFareRule(fareRuleSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 특정 요금 룰의 작업 히스토리 요약정보를 가져온다
	 * @param fareId - 요금 룰 ID
	 * @param searchVo - 검색조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/fareRuleTask/summary/{fareId}")
	public CommonOutput listFareRuleTaskSummary(@PathVariable int fareId, SearchVo searchVo) throws Exception {
		CommonResult commonResult = fareRuleService.getFareRuleTaskSummaryList(fareId, searchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 요금 룰 작업정보를 조회한다
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/fareRuleTask/{taskId}")
	public CommonOutput infoFareRuleTask(@PathVariable long taskId) throws Exception {
		CommonResult commonResult = fareRuleService.getFareRuleTaskInfo(taskId);
		return new CommonOutput(commonResult);
	}

	/**
	 * 요금 룰 정보 데이터 구조를 가져온다
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/schema/fareRule")
	public CommonOutput getFareRuleSchema() throws Exception {
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(fareRuleService.selectFareRuleSchemaAll());
		return new CommonOutput(commonResult);
	}

	/**
	 * 요금 룰 생성을 위한 Task를 등록한다
	 * @param fareRuleTaskInputVo - 요금 룰 입력정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/fareRuleTask/addTask")
	public CommonOutput registerFareRuleAddTask(@RequestBody @Valid FareRuleTaskInputVo fareRuleTaskInputVo) throws Exception {

		CommonResult commonResult = fareRuleService.registerFareRuleTask(TaskType.REGISTER, fareRuleTaskInputVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 요금 룰 정보 수정을 위한 Task를 등록한다
	 * @param fareRuleTaskInputVo - 요금 룰 입력정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/fareRuleTask/editTask")
	public CommonOutput registerFareRuleAddTask(@RequestBody @Valid FareRuleTaskInputVo fareRuleTaskInputVo,
		BindingResult bindingResult) throws Exception {

		if (fareRuleTaskInputVo.getFareId() == null || fareRuleTaskInputVo.getFareId() <= 0) {
			bindingResult.addError(new FieldError(CommonConstant.FARE_RULE_TASK_INPUT_VO, CommonConstant.KEY_FARE,
				ResultCode.BINDING_NUMBER_ERROR.getDisplayMessage()));
			throw new MethodArgumentNotValidException(null, bindingResult);
		}

		CommonResult commonResult = fareRuleService.registerFareRuleTask(TaskType.MODIFY, fareRuleTaskInputVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 요금 룰 삭제를 요청하는 작업정보를 등록한다
	 * @param fareRuleRemoveTaskInputVo - 요금 룰 삭제 작업등록 입력 정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/fareRuleTask/removeTask")
	public CommonOutput registerFareRemoveTask(@RequestBody @Valid FareRuleRemoveTaskInputVo fareRuleRemoveTaskInputVo)
		throws Exception {
		CommonResult commonResult = fareRuleService.registerFareRuleRemoveTask(fareRuleRemoveTaskInputVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 요금 룰 작업정보를 수정한다
	 * @param fareRuleTaskInputVo - 요금 룰 입력정보
	 * @param bindingResult - 바인딩 결과
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/v1/ntool/api/modify/fareRuleTask")
	public CommonOutput modifyFareRuleTask(@RequestBody @Valid FareRuleTaskInputVo fareRuleTaskInputVo,
		BindingResult bindingResult) throws Exception {
		if (fareRuleTaskInputVo.getTaskId() <= 0) {
			bindingResult.addError(new FieldError(CommonConstant.FARE_RULE_TASK_INPUT_VO, CommonConstant.KEY_TASK,
				ResultCode.BINDING_NUMBER_ERROR.getDisplayMessage()));
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		fareRuleService.modifyFareRuleTask(fareRuleTaskInputVo);
		return new CommonOutput();
	}

}
