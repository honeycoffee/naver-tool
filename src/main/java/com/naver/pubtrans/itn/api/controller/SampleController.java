package com.naver.pubtrans.itn.api.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.naver.pubtrans.itn.api.consts.URI;
import com.naver.pubtrans.itn.api.service.SampleService;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleInputVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleSearchVo;

import lombok.extern.slf4j.Slf4j;


/**
 * 네이버 대중교통 DB 내제화 샘플 컨트롤러
 * 
 * @author adtec10
 *
 */

@Slf4j
@RestController
public class SampleController {
	
	@Autowired
	private SampleService sampleSvc ;
	
    
	/**
	 * 데이터 조회 샘플
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = URI.SAMPLE_DETAIL)
    public CommonOutput sampleDetail(@PathVariable int id) throws Exception {

		// Get Data
		CommonResult rs = sampleSvc.getSampleDataById(id) ;
		log.info("로깅 테스트");

		
		return new CommonOutput(rs) ;
    }
    

    
    
    /**
     * 데이터 입력 샘플
     * <pre>
     * Valid를 이용하여 유효성 검사를 진행한다
     * </pre>
     * @param sampleInputVo
     * @return
     * @throws Exception
     */
    @PostMapping(value = URI.SAMPLE_REGISTER)
    public CommonOutput sampleInsert(@RequestBody @Valid SampleInputVo sampleInputVo) throws Exception {
    	
    	// 데이터 저장 서비스
    	sampleSvc.setSampleData(sampleInputVo, "ins");
    	
    	return new CommonOutput() ;
    }
    
    
    /**
     * 데이터 수정 샘플
     * @param sampleInputVo
     * @return
     * @throws Exception
     */
    @PostMapping(value = URI.SAMPLE_EDIT)
    public CommonOutput sampleUpdate(@RequestBody @Valid SampleInputVo sampleInputVo, BindingResult bindingResult) throws Exception {
    	
    	// Custom Valid Error
    	if(sampleInputVo.getId() <= 0) {
    		bindingResult.addError(new FieldError("SampleInputVo", "id", "반드시 값이 존재하고 길이 혹은 크기가 0보다 커야 합니다.."));
    		throw new MethodArgumentNotValidException(null, bindingResult) ;
    	}
		
    	sampleSvc.setSampleData(sampleInputVo, "upd");
		
		return new CommonOutput() ;
    }
	
    
    /**
     * 샘플데이터 목록
     * @param search
     * @return
     * @throws Exception
     */
    @GetMapping(value = URI.SAMPLE)
    public CommonOutput sampleList(SampleSearchVo searchVo) throws Exception {

		
		// Get Data
		CommonResult rs = sampleSvc.getSampleList(searchVo) ;

		
		return new CommonOutput(rs) ;
    }
	
    
    
    /**
     * 샘플 데이터 상세 구조를 조회한다
     * @return
     * @throws Exception
     */
    @GetMapping(value = URI.SAMPLE_DATA_STRUCTURE)
    public CommonOutput sampleDataStructure() throws Exception {
    	
    	// 데이터 저장 서비스
    	CommonResult rs = sampleSvc.getDataStructure() ;
    	
    	return new CommonOutput(rs) ;
    }
  
}
