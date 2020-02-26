package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.dao.SampleDao;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.StructureVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonStructure;
import com.naver.pubtrans.itn.api.vo.sample.SampleVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleInputVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleSearchVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleService {
	
	@Autowired
	private OutputFmtUtil outputFmtUtil ;

	@Autowired
	private SampleDao sampleDao ; 
	
	
	public CommonResult getSampleDataTest() {
		// 검색조건
		SampleSearchVo searchVo = new SampleSearchVo();
		searchVo.setId(3);
		
		return getSampleData(searchVo) ;
	}
	
	public CommonResult getSampleDataById(int id) {
		// 검색조건
		SampleSearchVo searchVo = new SampleSearchVo();
		searchVo.setId(id);
		
		return getSampleData(searchVo) ;
	}
	
	
	/**
	 * 프로젝트 샘플 데이터 반환
	 * 
	 * @return	공통 결과포맷
	 */
	public CommonResult getSampleData(SampleSearchVo searchVo) {
		
		
		
		// 데이터 조회
		SampleVo sampleVo = sampleDao.getSampleData(searchVo) ;
		
		
		// 공통 결과 포맷 출력
		CommonResult cmnRs = new CommonResult(sampleVo) ;
		
		return cmnRs ;
	}
	
	
	/**
	 * 샘플 데이터 입력/수정
	 * @param sampleInputVo
	 * @param type
	 */
	public void setSampleData(SampleInputVo sampleInputVo, String type) {
		
		if("ins".equals(type)) {
			sampleDao.insSampleData(sampleInputVo);
		}else if("upd".equals(type)) {
			sampleDao.updSampleData(sampleInputVo);
		}
	}
	
	/**
	 * 페이징 처리 샘플
	 * @param pageable
	 * @return
	 */
	public CommonResult getSampleList(SampleSearchVo searchVo) {
		
		
		// 전체 목록 수 가져오기
		int totalCnt = sampleDao.getSampleListCnt(searchVo) ;
		
		
		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalCnt, searchVo.getPageNo(), searchVo.getListSize()) ;
		
		// 목록 조회 페이징 정보 set
		searchVo.setStartLimit(pagingVo.getStartPageLimit());
		searchVo.setEndLimit(pagingVo.getEndPageLimit());
		
		// 목록 조회
		List<SampleVo> list = sampleDao.getSampleList(searchVo) ;
		
		
		// 공통포맷 출력
		CommonResult cmnRs = outputFmtUtil.setCommonListFmt(pagingVo, list) ;
		
		
		
		return cmnRs ;
	}
	
	
	
	
	/**
	 * 데이터 구조 가져오기
	 * @return
	 */
	public CommonResult getDataStructure() {
		
		
		ArrayList<String> ignoreColumns = new ArrayList<>();
		ignoreColumns.add("upd_date") ;
		ignoreColumns.add("reg_date") ;
		
		
		List<StructureVo> columnList = sampleDao.getSampleSchema() ;

		List<CommonStructure> schemaList = outputFmtUtil.setCommonStructure(columnList, ignoreColumns) ; 
		
		// 문서 공통 포맷으로 포맷
		CommonResult cmnRs = outputFmtUtil.setCommonDocFmt(schemaList) ;
		
		return cmnRs ;
	}
	
	
}
