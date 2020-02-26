package com.naver.pubtrans.itn.api.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.common.StructureVo;
import com.naver.pubtrans.itn.api.vo.sample.SampleVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleInputVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleSearchVo;

@Repository
public interface SampleDao {

	public SampleVo getSampleData(SampleSearchVo searchVo) throws DataAccessException ;
	
	public List<StructureVo> getSampleSchema() throws DataAccessException ;
	
	public int getSampleListCnt(SampleSearchVo searchVo) throws DataAccessException ;
	
	public List<SampleVo> getSampleList(SampleSearchVo searchVo) throws DataAccessException ;
	
	public void insSampleData(SampleInputVo v) throws DataAccessException ;
	
	public void updSampleData(SampleInputVo v) throws DataAccessException ;
	
}
