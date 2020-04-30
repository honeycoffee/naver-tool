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

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.service.BusCompanyService;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanySearchVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanyRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.company.input.BusCompanyTaskInputVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;


/**
 * 버스 운수사 관리 컨트롤러
 *
 * @author westwind
 *
 */

@RestController
public class BusCompanyController {


	private final BusCompanyService busCompanyService;

	private final OutputFmtUtil outputFmtUtil;


	@Autowired
	public BusCompanyController(BusCompanyService busCompanyService, OutputFmtUtil outputFmtUtil) {
		this.busCompanyService = busCompanyService;
		this.outputFmtUtil = outputFmtUtil;
	}



	/**
	 * 버스 운수사 목록 조회
	 * @param busCompanySearchVo - 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/busCompany")
	public CommonOutput listBusCompany(BusCompanySearchVo busCompanySearchVo) throws Exception {
		CommonResult commonResult = busCompanyService.getBusCompanyList(busCompanySearchVo);
		return new CommonOutput(commonResult);
	}


	/**
	 * 특정 버스 운수사의 작업 히스토리 요약정보를 가져온다
	 * @param companyId - 운수사 ID
	 * @param searchVo - 검색조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/busCompanyTask/summary/{companyId}")
	public CommonOutput listBusCompanyTaskSummary(@PathVariable int companyId, SearchVo searchVo) throws Exception {
		CommonResult commonResult = busCompanyService.getBusCompanyTaskSummaryList(companyId, searchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 버스 운수사 상세정보를 조회한다
	 * @param companyId - 운수사 ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/busCompany/{companyId}")
	public CommonOutput infoBusCompany(@PathVariable int companyId) throws Exception {
		CommonResult commonResult = busCompanyService.getBusCompanyInfo(companyId);
		return new CommonOutput(commonResult);
	}

	/**
	 * 버스 운수사 상세 작업정보를 조회한다
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/busCompanyTask/{taskId}")
	public CommonOutput infoBusCompanyTask(@PathVariable long taskId) throws Exception {
		CommonResult commonResult = busCompanyService.getBusCompanyTaskInfo(taskId);
		return new CommonOutput(commonResult);
	}

	/**
	 * 버스 운수사 정보 데이터 구조를 가져온다
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/schema/busCompany")
	public CommonOutput getBusCompanySchema() throws Exception {
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(busCompanyService.selectBusCompanySchemaAll());
		return new CommonOutput(commonResult);
	}




	/**
	 * 버스 운수사 생성을 위한 Task를 등록한다
	 * @param busCompanyTaskInputVo - 운수사 작업정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/busCompanyTask/addTask")
	public CommonOutput registerBusCompanyAddTask(@RequestBody @Valid BusCompanyTaskInputVo busCompanyTaskInputVo) throws Exception {
		CommonResult commonResult = busCompanyService.registerBusCompanyTask(TaskType.REGISTER, busCompanyTaskInputVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 버스 운수사 정보 수정을 위한 Task를 등록한다
	 * @param busCompanyTaskInputVo - 운수사 작업정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/busCompanyTask/editTask")
	public CommonOutput registerBusCompanyEditTask(@RequestBody @Valid BusCompanyTaskInputVo busCompanyTaskInputVo, BindingResult bindingResult) throws Exception {
		if(busCompanyTaskInputVo.getCompanyId() <= 0) {
			bindingResult.addError(new FieldError(CommonConstant.BUS_COMPANY_TASK_INPUT_VO, CommonConstant.KEY_COMPANY, ResultCode.BINDING_NUMBER_ERROR.getDisplayMessage()));
    		throw new MethodArgumentNotValidException(null, bindingResult);
		}

		CommonResult commonResult = busCompanyService.registerBusCompanyTask(TaskType.MODIFY, busCompanyTaskInputVo);
		return new CommonOutput(commonResult);
	}


	/**
	 * 버스 운수사 삭제를 요청하는 작업정보를 등록한다
	 * @param busCompanyRemoveTaskInputVo - 운수사 삭제 정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/busCompanyTask/removeTask")
	public CommonOutput registerBusCompanyRemoveTask(@RequestBody @Valid BusCompanyRemoveTaskInputVo busCompanyRemoveTaskInputVo) throws Exception {
		CommonResult commonResult = busCompanyService.registerBusCompanyRemoveTask(busCompanyRemoveTaskInputVo);
		return new CommonOutput(commonResult);
	}


	/**
	 * 버스 운수사 작업정보를 수정한다
	 * @param busCompanyTaskInputVo - 운수사 작업정보
	 * @param bindingResult - 바인딩 결과
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/v1/ntool/api/modify/busCompanyTask")
	public CommonOutput modifyBusCompanyTask(@RequestBody @Valid BusCompanyTaskInputVo busCompanyTaskInputVo, BindingResult bindingResult) throws Exception {
		if(busCompanyTaskInputVo.getTaskId() <= 0) {
			bindingResult.addError(new FieldError(CommonConstant.BUS_COMPANY_TASK_INPUT_VO, CommonConstant.KEY_TASK, ResultCode.BINDING_NUMBER_ERROR.getDisplayMessage()));
    		throw new MethodArgumentNotValidException(null, bindingResult);
		}
		busCompanyService.modifyBusCompanyTask(busCompanyTaskInputVo);
		return new CommonOutput();
	}

}
