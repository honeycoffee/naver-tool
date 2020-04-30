package com.naver.pubtrans.itn.api.controller;


import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.service.BusStopService;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopRemoveTaskInputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.common.input.SearchVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;


/**
 * 버스 정류장 관리 컨트롤러
 *
 * @author adtec10
 *
 */

@RestController
public class BusStopController {


	private final BusStopService busStopService;

	private final OutputFmtUtil outputFmtUtil;


	@Autowired
	public BusStopController(BusStopService busStopService, OutputFmtUtil outputFmtUtil) {
		this.busStopService = busStopService;
		this.outputFmtUtil = outputFmtUtil;
	}



	/**
	 * 버스정류장 목록 조회
	 * @param busStopSearchVo - 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/busStop")
	public CommonOutput listBusStop(BusStopSearchVo busStopSearchVo) throws Exception {
		CommonResult commonResult = busStopService.getBusStopList(busStopSearchVo);
		return new CommonOutput(commonResult);
	}


	/**
	 * 특정 버스정류장의 작업 히스토리 요약정보를 가져온다
	 * @param busStopId - 정류장 ID
	 * @param searchVo - 검색조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/busStopTask/summary/{busStopId}")
	public CommonOutput listBusStopTaskSummary(@PathVariable int busStopId, SearchVo searchVo) throws Exception {
		CommonResult commonResult = busStopService.getBusStopTaskSummaryList(busStopId, searchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 정류장 상세정보를 조회한다
	 * @param stopId - 정류장 ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/busStop/{stopId}")
	public CommonOutput infoBusStop(@PathVariable int stopId) throws Exception {
		CommonResult commonResult = busStopService.getBusStopInfo(stopId);
		return new CommonOutput(commonResult);
	}

	/**
	 * 정류장 상세 작업정보를 조회한다
	 * @param taskId - 작업ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/info/busStopTask/{taskId}")
	public CommonOutput infoBusStopTask(@PathVariable long taskId) throws Exception {
		CommonResult commonResult = busStopService.getBusStopTaskInfo(taskId);
		return new CommonOutput(commonResult);
	}

	/**
	 * 정류장 정보 데이터 구조를 가져온다
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/schema/busStop")
	public CommonOutput getBusStopSchema() throws Exception {
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(busStopService.selectBusStopSchemaAll());
		return new CommonOutput(commonResult);
	}


	/**
	 * 버스정류장 생성을 위한 Task를 등록한다
	 * @param busStopTaskInputVo - 정류장 입력정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/busStopTask/addTask")
	public CommonOutput registerBusStopAddTask(@RequestBody @Valid BusStopTaskInputVo busStopTaskInputVo) throws Exception {
		CommonResult commonResult = busStopService.registerBusStopTask(TaskType.REGISTER, busStopTaskInputVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 버스정류장 정보 수정을 위한 Task를 등록한다
	 * @param busStopTaskInputVo - 정류장 입력정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/busStopTask/editTask")
	public CommonOutput registerBusStopEditTask(@RequestBody @Valid BusStopTaskInputVo busStopTaskInputVo, BindingResult bindingResult) throws Exception {

		if(busStopTaskInputVo.getStopId() <= 0) {
			bindingResult.addError(new FieldError(CommonConstant.BUS_STOP_TASK_INPUT_VO, CommonConstant.KEY_STOP, ResultCode.BINDING_NUMBER_ERROR.getDisplayMessage()));
    		throw new MethodArgumentNotValidException(null, bindingResult);
		}

		CommonResult commonResult = busStopService.registerBusStopTask(TaskType.MODIFY, busStopTaskInputVo);
		return new CommonOutput(commonResult);
	}


	/**
	 * 버스정류장 작업정보를 수정한다
	 * @param busStopTaskInputVo - 정류장 입력정보
	 * @param bindingResult - 바인딩 결과
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/v1/ntool/api/modify/busStopTask")
	public CommonOutput modifyBusStopTask(@RequestBody @Valid BusStopTaskInputVo busStopTaskInputVo, BindingResult bindingResult) throws Exception {
		busStopService.modifyBusStopTask(busStopTaskInputVo);
		return new CommonOutput();
	}


	/**
	 * 버스 정류장 삭제를 요청하는 작업정보를 등록한다
	 * @param busStopRemoveTaskInputVo - 버스정류장 삭제 정보
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/ntool/api/busStopTask/removeTask")
	public CommonOutput registerBusStopRemoveTask(@RequestBody @Valid BusStopRemoveTaskInputVo busStopRemoveTaskInputVo) throws Exception {
		CommonResult commonResult = busStopService.registerBusStopRemoveTask(busStopRemoveTaskInputVo);
		return new CommonOutput(commonResult);
	}


	/**
	 * 지도영역에 속하는 버스정류장 목록을 가져온다
	 * @param busStopSearchVo - 검색조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/busStopFromMapBounds")
	public CommonOutput listBusStopFromMapBounds(BusStopSearchVo busStopSearchVo) throws Exception {

		if(Objects.isNull(busStopSearchVo) || Objects.isNull(busStopSearchVo.getRightTopCoordinates()) || Objects.isNull(busStopSearchVo.getLeftBottomCoordinates())) {
			throw new MissingServletRequestParameterException(String.valueOf(ResultCode.PARAMETER_ERROR.getApiErrorCode()),
				ResultCode.PARAMETER_ERROR.getDisplayMessage());
		}

		CommonResult commonResult = busStopService.getBusStopListFromMapBounds(busStopSearchVo);
		return new CommonOutput(commonResult);
	}

}
