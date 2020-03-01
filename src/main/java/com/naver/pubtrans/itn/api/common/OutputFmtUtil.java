package com.naver.pubtrans.itn.api.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonMeta;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema.FieldValue;

import lombok.RequiredArgsConstructor;

/**
 * 공통 출력 데이터 포맷을 생성하는 유틸.
 * @author adtec10
 *
 */
@Component
@RequiredArgsConstructor
public class OutputFmtUtil {

	/**
	 * 페이징 정보와 목록결과만 포함되는 데이터 포맷을 생성한다
	 * @param vo - 페이징 정보
	 * @param list - 목록 데이터
	 * @return
	 */
	public <T> CommonResult setCommonListFmt(PagingVo vo, List<T> list) {

		CommonMeta meta = new CommonMeta() ;
		meta.setTotalListCount(vo.getTotalListCount());
		meta.setTotalPageCount(vo.getTotalPageCount());
		meta.setCurrentPage(vo.getCurrentPage());
		meta.setFirstPage(vo.isFirstPage());
		meta.setLastPage(vo.isLastPage());
		meta.setListCountPerPage(vo.getListCntPerPage());


		CommonResult result = new CommonResult() ;
		result.setMeta(meta);
		result.setData(list);

		return result ;
	}


	/**
	 * 데이터 항목만 존재하는 데이터 포맷을 생성한다
	 * @param data - 출력 데이터
	 * @return
	 */
	public CommonResult setCommonDocFmt(Object data) {
		CommonResult result = new CommonResult() ;
		result.setData(data);

		return result ;
	}


	/**
	 * 스키마 항목만 존재하는 데이터 포맷을 생성한다
	 * @param schema - 입/출력 스키마 목록
	 * @return
	 */
	public CommonResult setCommonDocFmt(List<CommonSchema> schema) {
		CommonResult result = new CommonResult() ;
		result.setSchema(schema);

		return result ;
	}


	/**
	 * 스키마와 결과 데이터가 포함된 데이터 포맷을 생성한다
	 * @param schema - 입/출력 스키마 목록
	 * @param data - 출력 데이터
	 * @return
	 */
	public CommonResult setCommonDocFmt(List<CommonSchema> schema, Object data) {
		CommonResult result = new CommonResult() ;
		result.setData(data);
		result.setSchema(schema);

		return result ;
	}


	/**
	 * 테이블 컬럼 스키마 생성
	 * @param columnList - 컬럼 목록
	 * @return
	 */
	public List<CommonSchema> makeCommonSchema(List<SchemaVo> columnList){

		return this.makeCommonSchema(columnList, null, null, null) ;
	}


	/**
	 * 테이블 컬럼 스키마 생성
	 * <pre>
	 * Table 정보 조회를 통해 취득한 컬럼목록을 공통 스키마 데이터 목록 포맷으로 생성한다.
	 * 제외하고 싶은 컬럶을 지정하여 데이터 목록에 포함되지 않도록 한다
	 * <pre>
	 * @param columnList - 컬럼 목록
	 * @param ignoreColumns - 예외 컬럼 목록
	 * @return
	 */
	public List<CommonSchema> makeCommonSchema(List<SchemaVo> columnList, ArrayList<String> ignoreColumns){

		return this.makeCommonSchema(columnList, ignoreColumns, null, null) ;
	}

	/**
	 * 테이블 컬럼 스키마 생성
	 * <pre>
	 * Table 정보 조회를 통해 취득한 컬럼목록을 공통 스키마 데이터 목록 포맷으로 생성한다.
	 * 필요에 따라 특정 컬럼을 제외하고, 수정이 불가능한 읽기전용 컬럼으로 속성을 지정한다.
	 * </pre>
	 * @param columnList - 컬럼 목록
	 * @param ignoreColumns - 예외 컬럼 목록
	 * @param readOnlyColumns - 읽기 전용 컬럼 목록
	 * @return
	 */
	public List<CommonSchema> makeCommonSchema(List<SchemaVo> columnList, ArrayList<String> ignoreColumns, ArrayList<String> readOnlyColumns){
		return this.makeCommonSchema(columnList, ignoreColumns, readOnlyColumns, null) ;
	}

	/**
	 * 테이블 컬럼 스키마 생성
	 * <pre>
	 * Table 정보 조회를 통해 취득한 컬럼목록을 공통 스키마 데이터 목록 포맷으로 생성한다.
	 * 필요에 따라 특정 컬럼을 제외하고, 수정이 불가능한 읽기전용 컬럼으로 속성을 지정한다.
	 * 공통 코드값으로 사용되는 컬럼은 사용 가능한 코드 목록을 함께 정의한다.
	 * </pre>
	 * @param columnList - 컬럼 목록
	 * @param ignoreColumns - 예외 컬럼 목록
	 * @param readOnlyColumns - 읽기 전용 컬럼 목록
	 * @param valuesMap - 다중 Value 컬럼 목록
	 * @return
	 */
	public List<CommonSchema> makeCommonSchema(List<SchemaVo> columnList, ArrayList<String> ignoreColumns, ArrayList<String> readOnlyColumns, HashMap<String, List<FieldValue>> valuesMap){

		List<CommonSchema> list = new ArrayList<>();

		for(SchemaVo vo : columnList) {

			String name = vo.getColumnName() ;
			String label = (StringUtils.isEmpty(vo.getColumnComment()))? name : vo.getColumnComment();
			String type = "" ;
			String length = "" ;
			String nullable = ("YES".equals(vo.getIsNullable()))? "Y" : "N" ;
			String pkYn = ("PRI".equals(vo.getColumnKey()))? "Y" : "N" ;

			String tmpType = vo.getColumnType() ;


			int tmpTypeidxOf = tmpType.indexOf("(") ;
			if(tmpTypeidxOf > -1) {
				type = tmpType.substring(0, tmpTypeidxOf) ;
				length = tmpType.substring(tmpTypeidxOf+1, tmpType.indexOf(")")) ;
			}else {
				type = tmpType ;
			}


			// 예외컬럼이 아닌경우만 생성
			if(Objects.isNull(ignoreColumns) || (Objects.nonNull(ignoreColumns) && !ignoreColumns.contains(name))) {

				CommonSchema schema = new CommonSchema();
				schema.setFieldLabel(label);
				schema.setFieldName(JdbcUtils.convertUnderscoreNameToPropertyName(name));	// 카멜케이스 변환
				schema.setFieldType(this.convertFieldType(type));
				schema.setFieldLength(length);
				schema.setNullable(nullable);
				schema.setPkYn(pkYn);

				// 읽기전용 여부
				if(Objects.nonNull(readOnlyColumns) && readOnlyColumns.contains(name))
					schema.setReadOnlyYn("Y");
				else
					schema.setReadOnlyYn("N");


				// 다중 value 항목(선택, selectbox, radio 등)
				if(Objects.nonNull(valuesMap) && valuesMap.containsKey(name)) {

					List<FieldValue> values = valuesMap.get(name) ;
					schema.setFieldValues(values);

					// 데이터 타입을 enum 형식으로 변경
					schema.setFieldType("enum");
				}


				list.add(schema) ;

			}


		}

		return list ;
	}


	/**
	 * 컬럼 데이터 타입을 변경한다
	 * @param type
	 * @return
	 */
	public String convertFieldType(String type) {

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
