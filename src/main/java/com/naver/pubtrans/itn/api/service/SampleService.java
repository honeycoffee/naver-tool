package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.repository.SampleRepository;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.sample.SampleVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleInputVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleSearchVo;

import lombok.RequiredArgsConstructor;

/**
 * 네이버 대중교통 내재화 샘플 서비스
 * @author adtec10
 *
 */
@Service
@RequiredArgsConstructor
public class SampleService {

	@Autowired
	private OutputFmtUtil outputFmtUtil ;

	@Autowired
	private SampleRepository sampleRepository ;



	/**
	 * ID를 기반으로 샘플 데이터를 가져온다
	 * @param id - 조회ID
	 * @return
	 */
	public CommonResult getSampleDataById(int id) {
		// 검색조건
		SampleSearchVo searchVo = new SampleSearchVo();
		searchVo.setId(id);

		return getSampleData(searchVo) ;
	}


	/**
	 * 샘플 데이터를 가져온다
	 * @param searchVo - 검색 조건
	 * @return
	 */
	public CommonResult getSampleData(SampleSearchVo searchVo) {



		// 데이터 조회
		SampleVo sampleVo = sampleRepository.selectSampleData(searchVo) ;


		// 공통 결과 포맷 출력
		CommonResult commonResult = new CommonResult(sampleVo) ;

		return commonResult ;
	}


	/**
	 * 샘플 데이터를 등록한다
	 * @param sampleInputVo - 입력값
	 */
	public void insertSampleData(SampleInputVo sampleInputVo) {
		sampleRepository.insertSampleData(sampleInputVo);
	}


	/**
	 * 샘플 데이터를 업데이트 한다
	 * @param sampleInputVo - 입력값
	 */
	public void updateSampleData(SampleInputVo sampleInputVo) {
		sampleRepository.updateSampleData(sampleInputVo);
	}


	/**
	 * 샘플 목록을 가져온다
	 * @param searchVo - 검색조건
	 * @return
	 */
	public CommonResult getSampleList(SampleSearchVo searchVo) {


		// 전체 목록 수 가져오기
		int totalListCnt = sampleRepository.selectSampleListCnt(searchVo) ;


		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalListCnt, searchVo.getPageNo(), searchVo.getListSize()) ;

		// 목록 조회 페이징 정보 set
		searchVo.setStartPageLimit(pagingVo.getStartPageLimit());
		searchVo.setEndPageLimit(pagingVo.getEndPageLimit());

		// 목록 조회
		List<SampleVo> list = sampleRepository.selectSampleList(searchVo) ;


		// 공통포맷 출력
		CommonResult commonResult = outputFmtUtil.setCommonListFmt(pagingVo, list) ;



		return commonResult ;
	}




	/**
	 * 데이터 입/출력 구조를 가져온다
	 * @return
	 */
	public CommonResult getDataSchema() {


		ArrayList<String> ignoreColumnNameList = new ArrayList<>();
		ignoreColumnNameList.add("upd_date") ;
		ignoreColumnNameList.add("reg_date") ;


		List<SchemaVo> columnList = sampleRepository.selectSampleSchema() ;

		// 컬럼목록 재정의
		columnList = outputFmtUtil.refineSchemaVoIngnoreColumns(columnList, ignoreColumnNameList) ;

		List<CommonSchema> schemaList = outputFmtUtil.makeCommonSchema(columnList) ;

		// 문서 공통 포맷으로 포맷
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(schemaList) ;

		return commonResult ;
	}


}
