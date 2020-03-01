package com.naver.pubtrans.itn.api.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.sample.SampleVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleInputVo;
import com.naver.pubtrans.itn.api.vo.sample.input.SampleSearchVo;

/**
 * 네이버 대중교통 내재화 샘플 Repository
 * @author adtec10
 *
 */
@Repository
public interface SampleRepository {

	public SampleVo selectSampleData(SampleSearchVo searchVo) throws DataAccessException ;

	public List<SchemaVo> selectSampleSchema() throws DataAccessException ;

	public int selectSampleListCnt(SampleSearchVo searchVo) throws DataAccessException ;

	public List<SampleVo> selectSampleList(SampleSearchVo searchVo) throws DataAccessException ;

	public void insertSampleData(SampleInputVo v) throws DataAccessException ;

	public void updateSampleData(SampleInputVo v) throws DataAccessException ;

}
