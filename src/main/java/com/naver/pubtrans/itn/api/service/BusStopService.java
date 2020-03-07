package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CodeType;
import com.naver.pubtrans.itn.api.repository.BusStopRepository;
import com.naver.pubtrans.itn.api.vo.bus.stop.input.BusStopSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.output.BusStopListOutputVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;

/**
 * 버스정류장 관리 서비스
 *
 * @author adtec10
 *
 */

@Service
public class BusStopService {

	@Autowired
	private OutputFmtUtil outputFmtUtil ;

	@Autowired
	private CommonService commonService ;

	@Autowired
	private BusStopRepository busStopRepository ;


	/**
	 * 버스정류장 목록을 가져온다
	 * @param busStopSearchVo - 목록 검색 조건
	 * @return
	 * @throws Exception
	 */
	public CommonResult getBusStopList(BusStopSearchVo busStopSearchVo) throws Exception {

		/**
		 * 1. 페이지 목록 조회
		 */

		// 전체 목록 수 가져오기
		int totalListCnt = busStopRepository.selectBusStopListTotalCnt(busStopSearchVo) ;


		// 페이징 정보
		PagingVo pagingVo = new PagingVo(totalListCnt, busStopSearchVo.getPageNo(), busStopSearchVo.getListSize()) ;

		// 목록 조회 페이징 정보 set
		busStopSearchVo.setStartPageLimit(pagingVo.getStartPageLimit());
		busStopSearchVo.setEndPageLimit(pagingVo.getEndPageLimit());


		// 목록 조회
		List<BusStopListOutputVo> busStopListOutputVoList = busStopRepository.selectBusStopList(busStopSearchVo) ;


		/**
		 * 2. 검색 폼 데이터 구조 생성
		 */

		// 커스텀 검색조건 데이터 구조 생성
		String tableName = "tb_stops" ;
		List<SchemaVo> schemaVoList = commonService.getTableSchema(tableName) ;

		// 사용하고자 하는 컬럼 목록
		ArrayList<String> usableColumnNameList = new ArrayList<>();
		usableColumnNameList.add("stop_id") ;
		usableColumnNameList.add("stop_name") ;
		usableColumnNameList.add("citycode") ;


		// 컬럼목록 재정의
		schemaVoList = outputFmtUtil.refineSchemaVo(schemaVoList, usableColumnNameList) ;


		// 도시코드 목록
		HashMap<String, List<FieldValue>> valuesMap = new HashMap<>() ;
		valuesMap.put("citycode", commonService.getCommonCode(CodeType.CITYCODE.getCodeName())) ;


		// 검색 폼 데이터 구조
		List<CommonSchema> schemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, valuesMap) ;



		/**
		 * 3. 공통 출력포맷 생성
		 */
		CommonResult commonResult = outputFmtUtil.setCommonListFmt(schemaList, pagingVo, busStopListOutputVoList) ;

		return commonResult ;
	}


}
