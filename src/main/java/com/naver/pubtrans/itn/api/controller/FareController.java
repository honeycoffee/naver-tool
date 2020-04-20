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
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.service.FareService;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.fare.input.FareRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareSearchVo;
import com.naver.pubtrans.itn.api.vo.fare.input.FareTaskInputVo;

/**
 * 네이버 대중교통 DB 내재화 공지사항 컨트롤러
 * 
 * @author westwind
 *
 */
@Slf4j
@RestController
public class FareController {

	private final FareService fareService;

	private final OutputFmtUtil outputFmtUtil;

	@Autowired
	FareController(FareService fareService, OutputFmtUtil outputFmtUtil) {
		this.outputFmtUtil = outputFmtUtil;
		this.fareService = fareService;
	}

	/**
	 * 기본 요금 룰을 조회한다
	 * @param fareSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/fare")
	public CommonOutput getBaseFareRule(@Valid FareSearchVo fareSearchVo) throws Exception {
		CommonResult commonResult = fareService.getFareRule(fareSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 예외 요금 룰 리스트를 가져온다
	 * @param fareSearchVo - 요금 룰 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/ignoredFare")
	public CommonOutput selectIgnoredFareRuleList(@Valid FareSearchVo fareSearchVo) throws Exception {
		CommonResult commonResult = fareService.selectIgnoredFareRuleList(fareSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 예외 요금 룰을 조회한다
	 * @param fareId - 요금 룰 ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/fare/{fareId}")
	public CommonOutput getIgnoredFareRule(@PathVariable int fareId) throws Exception {
		
		FareSearchVo fareSearchVo = new FareSearchVo();
		fareSearchVo.setFareId(fareId);
		
		CommonResult commonResult = fareService.getFareRule(fareSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 요금 룰 작업정보를 조회한다
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/fareTask/{taskId}")
	public CommonOutput getFareTask(@PathVariable long taskId) throws Exception {
		CommonResult commonResult = fareService.getFareTaskInfo(taskId);
		return new CommonOutput(commonResult);
	}

	/**
	 * 요금 룰 정보 데이터 구조를 가져온다
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/schema/fare")
	public CommonOutput getFareSchema() throws Exception {
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(fareService.selectFareSchemaAll());
		return new CommonOutput(commonResult);
	}

	/**
	 * 요금 룰 생성을 위한 Task를 등록한다
	 * @param busStopTaskInputVo - 정류장 입력정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/fareTask/addTask")
	public CommonOutput registerFareAddTask(@RequestBody FareTaskInputVo fareTaskInputVo) throws Exception {
			
		fareService.registerFareRuleTask(TaskType.REGISTER.getCode(), fareTaskInputVo);
		return new CommonOutput();
	}

	/**
	 * 요금 룰 정보 수정을 위한 Task를 등록한다
	 * @param busStopTaskInputVo - 정류장 입력정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/fareTask/editTask")
	public CommonOutput registerFareAddTask(@RequestBody @Valid FareTaskInputVo fareTaskInputVo,
		BindingResult bindingResult) throws Exception {

		if (fareTaskInputVo.getFareId() <= 0) {
			bindingResult.addError(new FieldError(CommonConstant.FARE_TASK_INPUT_VO, CommonConstant.KEY_STOP,
				ResultCode.BINDING_NUMBER_ERROR.getDisplayMessage()));
			throw new MethodArgumentNotValidException(null, bindingResult);
		}

		fareService.registerFareRuleTask(TaskType.MODIFY.getCode(), fareTaskInputVo);
		return new CommonOutput();
	}

	/**
	 * 버스정류장 작업정보를 수정한다
	 * @param busStopTaskInputVo - 정류장 입력정보
	 * @param bindingResult - 바인딩 결과
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/v1/ntool/api/fareTask")
	public CommonOutput updateFareTask(@RequestBody @Valid FareTaskInputVo fareTaskInputVo,
		BindingResult bindingResult) throws Exception {
		fareService.updateFareTask(fareTaskInputVo);
		return new CommonOutput();
	}

	/**
	 * 버스 정류장 삭제를 요청하는 작업정보를 등록한다
	 * @param busStopRemoveTaskInputVo - 버스정류장 삭제 정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/fareTask/removeTask")
	public CommonOutput registerBusStopRemoveTask(@RequestBody @Valid FareRemoveTaskInputVo fareRemoveTaskInputVo)
		throws Exception {
//		fareService.registerFareRemoveTask(fareRemoveTaskInputVo);
		return new CommonOutput();
	}

}
