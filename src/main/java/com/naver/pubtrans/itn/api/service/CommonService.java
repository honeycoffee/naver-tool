package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.consts.CodeType;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.repository.CommonRepository;
import com.naver.pubtrans.itn.api.vo.common.BusProviderVo;
import com.naver.pubtrans.itn.api.vo.common.CityCodeVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;

/**
 * 공통 서비스
 * @author adtec10
 *
 */
@Service
public class CommonService {

	@Autowired
	CommonRepository commonRepository ;


	/**
	 * 데이틀 속성정보를 가져온다
	 * @param tableName - 테이블명
	 * @return
	 */
	public List<SchemaVo> getTableSchema(String tableName){
		return commonRepository.selectTableSchema(tableName) ;
	}

	/**
	 * 도시코드 목록 가져오기
	 * @return
	 */
	public List<FieldValue> getCitycodeAll(){


		// 전체 도시코드 목록
		List<CityCodeVo> citycodeVoList = commonRepository.selectCityCodeList() ;


		List<FieldValue> fieldValueList = new ArrayList<>();
		for(CityCodeVo citycodeVo : citycodeVoList) {
			fieldValueList.add(new FieldValue(String.valueOf(citycodeVo.getCityCode()), citycodeVo.getCityName())) ;
		}

		return fieldValueList ;
	}


	/**
	 * BIS 공급지역 목록 가져오기
	 * @return
	 */
	public List<FieldValue> getBusProviderListAll(){
		List<BusProviderVo> busProviderVoList = commonRepository.selectBusProviderList() ;

		List<FieldValue> fieldValueList = new ArrayList<>();
		for(BusProviderVo vo : busProviderVoList) {
			fieldValueList.add(new FieldValue(String.valueOf(vo.getProviderId()), vo.getProviderName())) ;
		}

		return fieldValueList ;
	}


	/**
	 * 공통 코드 목록을 가져온다
	 * @param codeName - 코드타입(CodType.class 참조)
	 * @return
	 */
	public List<FieldValue> getCommonCode(String codeName){

		List<FieldValue> fieldValueList = new ArrayList<>();

		if(codeName.equals(CodeType.CITYCODE.getCodeName())) {					// 도시코드
			fieldValueList = this.getCitycodeAll() ;
		}else if(codeName.equals(CodeType.BUS_PROVIDER.getCodeName())) {		// BIS 공급지역
			fieldValueList = this.getBusProviderListAll() ;
		}else if(codeName.equals(CodeType.Y_N.getCodeName())) {					// Y/N 선택
			FieldValue fY = new FieldValue(CommonConstant.Y, CommonConstant.Y) ;
			FieldValue fN = new FieldValue(CommonConstant.N, CommonConstant.N) ;
			fieldValueList.add(fY);
			fieldValueList.add(fN);
		}

		return fieldValueList ;
	}
}
