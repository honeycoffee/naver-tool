package com.naver.pubtrans.itn.api.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.StructureVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonMeta;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonStructure;
import com.naver.pubtrans.itn.api.vo.common.output.CommonStructure.FieldValues;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutputFmtUtil {
	
	/**
	 * 페이징이 포함되는 목록 포맷을 생성한다
	 * @param <T>
	 * @param p
	 * @return
	 */
	public <T> CommonResult setCommonListFmt(PagingVo vo, List<T> list) {
		
		CommonMeta meta = new CommonMeta() ;
		meta.setTotalCount(vo.getTotalCount());
		meta.setTotalPageCount(vo.getTotalPageCount());
		meta.setCurrentPage(vo.getCurrentPage());
		meta.setFirstPage(vo.isFirstPage());
		meta.setLastPage(vo.isLastPage());
		meta.setListCountPerPage(vo.getListCntPerPage());
		
		
		CommonResult cmnRs = new CommonResult() ;
		cmnRs.setMeta(meta);
		cmnRs.setData(list);
		
		return cmnRs ;
	}
	
	
	/**
	 * 페이징이 포함되지 않는 데이터 포맷을 생성한다 
	 * @param o
	 * @return
	 */
	public CommonResult setCommonDocFmt(Object o) {
		CommonResult cmnRs = new CommonResult() ;
		cmnRs.setData(o);
		
		return cmnRs ;
	}
	
	
	/**
	 * 스키마만 존재하는 결과 포맷을 생성한다
	 * @param schema
	 * @return
	 */
	public CommonResult setCommonDocFmt(List<CommonStructure> structure) {
		CommonResult cmnRs = new CommonResult() ;
		cmnRs.setStructure(structure);
		
		return cmnRs ;
	}
	
	
	/**
	 * 스키마가 포함된 결과 포맷을 생성한다
	 * @param o
	 * @param schema
	 * @return
	 */
	public CommonResult setCommonDocFmt(Object o, List<CommonStructure> structure) {
		CommonResult cmnRs = new CommonResult() ;
		cmnRs.setData(o);
		cmnRs.setStructure(structure);
		
		return cmnRs ;
	}
	
	
	/**
	 * 테이블 컬럼 스키마 생성
	 * @param columnList - 컬럼 목록
	 * @return
	 */
	public List<CommonStructure> setCommonStructure(List<StructureVo> columnList){
		
		return this.setCommonStructure(columnList, null, null, null) ;
	}
	
	
	/**
	 * 테이블 컬럼 스키마 생성
	 * @param columnList - 컬럼 목록
	 * @param ignoreColumns - 예외 컬럼 목록
	 * @return
	 */
	public List<CommonStructure> setCommonStructure(List<StructureVo> columnList, ArrayList<String> ignoreColumns){
		
		return this.setCommonStructure(columnList, ignoreColumns, null, null) ;
	}
	
	/**
	 * 테이블 컬럼 스키마 생성
	 * @param columnList - 컬럼 목록
	 * @param ignoreColumns - 예외 컬럼 목록
	 * @param readOnlyColumns - 읽기 전용 컬럼 목록
	 * @return
	 */
	public List<CommonStructure> setCommonStructure(List<StructureVo> columnList, ArrayList<String> ignoreColumns, ArrayList<String> readOnlyColumns){
		return this.setCommonStructure(columnList, ignoreColumns, readOnlyColumns, null) ;
	}
	
	/**
	 * 
	 * 테이블 컬럼 스키마 생성
	 * @param columnList - 컬럼 목록
	 * @param ignoreColumns - 예외 컬럼 목록
	 * @param readOnlyColumns - 읽기 전용 컬럼 목록
	 * @param valuesMap - 다중 Value 컬럼 목록 
	 * @return
	 */
	public List<CommonStructure> setCommonStructure(List<StructureVo> columnList, ArrayList<String> ignoreColumns, ArrayList<String> readOnlyColumns, HashMap<String, List<FieldValues>> valuesMap){
		
		List<CommonStructure> list = new ArrayList<>();
		
		for(StructureVo vo : columnList) {
			
			String name = vo.getField() ;
			String label = (StringUtils.isEmpty(vo.getComment()))? name : vo.getComment();
			String type = "" ;
			String length = "" ;
			String nullAble = ("YES".equals(vo.getNull()))? "Y" : "N" ;
			String pk_yn = ("PRI".equals(vo.getKey()))? "Y" : "N" ;
			
			String tmpType = vo.getType() ;
			
			
			int tmpTypeidxOf = tmpType.indexOf("(") ; 
			if(tmpTypeidxOf > -1) {
				type = tmpType.substring(0, tmpTypeidxOf) ;
				length = tmpType.substring(tmpTypeidxOf+1, tmpType.indexOf(")")) ;
			}else {
				type = tmpType ;
			}
			
			
			// 예외컬럼이 아닌경우만 생성
			if(Util.isNull(ignoreColumns) || (Util.isNotNull(ignoreColumns) && !ignoreColumns.contains(name))) {
				
				CommonStructure structure = new CommonStructure();
				structure.setFieldLabel(label);
				structure.setFieldName(JdbcUtils.convertUnderscoreNameToPropertyName(name));	// 카멜케이스 변환
				structure.setFieldType(this.convFieldType(type));
				structure.setFieldLength(length);
				structure.setNullAble(nullAble);
				structure.setPkYn(pk_yn);
				
				// 읽기전용 여부
				if(Util.isNotNull(readOnlyColumns) && readOnlyColumns.contains(name))
					structure.setReadOnlyYn("Y");
				else
					structure.setReadOnlyYn("N");
				
				
				// 다중 value 항목(선택, selectbox, radio 등)
				if(Util.isNotNull(valuesMap) && valuesMap.containsKey(name)) {
					
					List<FieldValues> values = valuesMap.get(name) ;
					structure.setFiledValues(values);
					
					// 데이터 타입을 enum 형식으로 변경
					structure.setFieldType("enum");
				}
				
				
				list.add(structure) ;
				
			}
			
			
		}
		
		return list ;
	}
	
	
	/**
	 * 컬럼 데이터 타입을 변경한다
	 * @param type
	 * @return
	 */
	public String convFieldType(String type) {
		
		// 문자형, 숫자형으로 구성
		switch(type.toLowerCase()) {
			case "varchar":
			case "char":
				type = "string";
				break ;
			case "tinytext":
			case "text":
			case "mediumtext":
			case "longtext":
				type = "text";
				break ;
			case "tinyint":
			case "smallint":
			case "mediumint":
			case "int":
			case "bigint":
			case "float":
			case "decimal":
			case "double":
				type = "number";
				break ;
		}
		
		return type ;
	}
}
