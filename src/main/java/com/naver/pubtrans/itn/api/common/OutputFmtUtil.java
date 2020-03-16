package com.naver.pubtrans.itn.api.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.naver.pubtrans.itn.api.consts.ColumnKeyType;
import com.naver.pubtrans.itn.api.consts.ColumnType;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.PagingVo;
import com.naver.pubtrans.itn.api.vo.common.AliasColumnNameVo;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonMeta;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;

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
	 * 페이징 메타정보를 생성한다
	 * @param pagingVo - 페이징 정보
	 * @return
	 */
	private CommonMeta initCommonMeta(PagingVo pagingVo) {

		CommonMeta commonMeta = new CommonMeta();
		commonMeta.setTotalListCount(pagingVo.getTotalListCount());
		commonMeta.setTotalPageCount(pagingVo.getTotalPageCount());
		commonMeta.setCurrentPage(pagingVo.getCurrentPage());
		commonMeta.setFirstPage(pagingVo.isFirstPage());
		commonMeta.setLastPage(pagingVo.isLastPage());
		commonMeta.setListCountPerPage(pagingVo.getListCntPerPage());

		return commonMeta;
	}

	/**
	 * 페이징 정보, 목록결과, 목록 검색 폼 스키마 정보가 포함되는 데이터 포맷을 생성한다
	 * @param schema - 검색 폼 스키마 정보
	 * @param pagingVo - 페이징 정보
	 * @param list - 목록 데이터
	 * @return
	 */
	public <T> CommonResult setCommonListFmt(List<CommonSchema> schema, PagingVo pagingVo, List<T> list) {

		CommonResult result = new CommonResult();
		result.setSchema(schema);
		result.setMeta(initCommonMeta(pagingVo));
		result.setData(list);

		return result;
	}


	/**
	 * 페이징 정보와 목록결과만 포함되는 데이터 포맷을 생성한다
	 * @param pagingVo - 페이징 정보
	 * @param list - 목록 데이터
	 * @return
	 */
	public <T> CommonResult setCommonListFmt(PagingVo pagingVo, List<T> list) {

		CommonResult result = new CommonResult();
		result.setMeta(initCommonMeta(pagingVo));
		result.setData(list);

		return result;
	}


	/**
	 * 데이터 항목만 존재하는 데이터 포맷을 생성한다
	 * @param data - 출력 데이터
	 * @return
	 */
	public CommonResult setCommonDocFmt(Object data) {
		CommonResult result = new CommonResult();
		result.setData(data);

		return result;
	}


	/**
	 * 스키마 항목만 존재하는 데이터 포맷을 생성한다
	 * @param schema - 입/출력 스키마 목록
	 * @return
	 */
	public CommonResult setCommonDocFmt(List<CommonSchema> schema) {
		CommonResult result = new CommonResult();
		result.setSchema(schema);

		return result;
	}


	/**
	 * 스키마와 결과 데이터가 포함된 데이터 포맷을 생성한다
	 * @param schema - 입/출력 스키마 목록
	 * @param data - 출력 데이터
	 * @return
	 */
	public CommonResult setCommonDocFmt(List<CommonSchema> schema, Object data) {
		CommonResult result = new CommonResult();
		result.setData(data);
		result.setSchema(schema);

		return result;
	}


	/**
	 * 테이블 컬럼 스키마 생성
	 * @param schemaVoList - 컬럼 목록
	 * @return
	 */
	public List<CommonSchema> makeCommonSchema(List<SchemaVo> schemaVoList){

		return this.makeCommonSchema(schemaVoList, null, null);
	}


	/**
	 * 테이블 컬럼 스키마 생성
	 * <pre>
	 * Table 정보 조회를 통해 취득한 컬럼목록을 공통 스키마 데이터 목록 포맷으로 생성한다.
	 * 필요에 따라 수정이 불가능한 읽기전용 컬럼으로 속성을 지정한다.
	 * </pre>
	 * @param schemaVoList - 컬럼 목록
	 * @param readOnlyColumns - 읽기 전용 컬럼 목록
	 * @return
	 */
	public List<CommonSchema> makeCommonSchema(List<SchemaVo> schemaVoList, ArrayList<String> readOnlyColumns){
		return this.makeCommonSchema(schemaVoList, readOnlyColumns, null);
	}

	/**
	 * 테이블 컬럼 스키마 생성
	 * <pre>
	 * Table 정보 조회를 통해 취득한 컬럼목록을 공통 스키마 데이터 목록 포맷으로 생성한다.
	 * 필요에 따라 수정이 불가능한 읽기전용 컬럼으로 속성을 지정한다.
	 * 공통 코드값으로 사용되는 컬럼은 사용 가능한 코드 목록을 함께 정의한다.
	 * </pre>
	 * @param schemaVoList - 컬럼 목록
	 * @param readOnlyColumns - 읽기 전용 컬럼 목록
	 * @param valuesMap - 다중 Value 컬럼 목록
	 * @return
	 */
	public List<CommonSchema> makeCommonSchema(List<SchemaVo> schemaVoList, ArrayList<String> readOnlyColumns, HashMap<String, List<FieldValue>> valuesMap){

		List<CommonSchema> commonSchemaList = new ArrayList<>();


		for(SchemaVo schemaVo : schemaVoList) {
			CommonSchema commonSchema = initCommonSchema(schemaVo);


			// 읽기전용 여부(사용자 지정 컬럼과 Primary Key는 읽기 전용으로 설정)
			if((Objects.nonNull(readOnlyColumns) && readOnlyColumns.contains(commonSchema.getFieldName())) || CommonConstant.Y.equals(commonSchema.getPkYn()))
				commonSchema.setReadOnlyYn(CommonConstant.Y);
			else
				commonSchema.setReadOnlyYn(CommonConstant.N);


			// 다중 value 항목(선택, selectbox, radio 등)
			if(Objects.nonNull(valuesMap) && valuesMap.containsKey(commonSchema.getFieldName())) {

				List<FieldValue> fieldValueList = valuesMap.get(commonSchema.getFieldName());
				commonSchema.setFieldValues(fieldValueList);

				// 데이터 타입을 enum 형식으로 변경
				commonSchema.setFieldType(CommonConstant.ENUM);
			}else {
				// 값이 없을때 빈값으로 생성.
				commonSchema.setFieldValues(new ArrayList<>());
			}

			// 다중 Value 항목의  Key는 카멜케이스 형태가 아니므로 조건 수행후 필드명을 카멜케이스 형태로 변환한다
			commonSchema.setFieldName(Util.snakeCaseToCamelCase(commonSchema.getFieldName()));


			commonSchemaList.add(commonSchema);


		}

		return commonSchemaList;
	}


	/**
	 * 공통 스키마 정보를 초기화 한다
	 * @param schemaVo - 테이블 속성 정보
	 * @return
	 */
	private CommonSchema initCommonSchema(SchemaVo schemaVo) {

		String name = schemaVo.getColumnName();
		String label = (StringUtils.isEmpty(schemaVo.getColumnComment()))? name : schemaVo.getColumnComment();
		String type = "";
		String length = "";
		String nullable = (CommonConstant.YES.equals(schemaVo.getIsNullable()))? CommonConstant.Y : CommonConstant.N;
		String pkYn = (ColumnKeyType.PK.getCode().equals(schemaVo.getColumnKey()))? CommonConstant.Y : CommonConstant.N;

		String tmpType = schemaVo.getColumnType();


		int tmpTypeidxOf = tmpType.indexOf(CommonConstant.BRACKET_START);
		if(tmpTypeidxOf > -1) {
			type = tmpType.substring(0, tmpTypeidxOf);
			length = tmpType.substring(tmpTypeidxOf+1, tmpType.indexOf(CommonConstant.BRACKET_END));
		}else {
			type = tmpType;
		}


		CommonSchema commonSchema = new CommonSchema();
		commonSchema.setFieldLabel(label);
		commonSchema.setFieldName(name);
		commonSchema.setFieldType(this.convertFieldType(type));
		commonSchema.setFieldLength(length);
		commonSchema.setNullable(nullable);
		commonSchema.setPkYn(pkYn);

		return commonSchema;

	}


	/**
	 * 컬럼 데이터 타입을 변경한다
	 * @param type
	 * @return
	 */
	public String convertFieldType(String type) {

		// 문자형, 숫자형으로 구성
		switch(type.toLowerCase()) {
			case ColumnType.VARCHAR :
			case ColumnType.CHAR:
			case ColumnType.TINYTEXT:
			case ColumnType.TEXT:
			case ColumnType.MEDIUMTEXT:
			case ColumnType.LONGTEXT:
				type = CommonConstant.STRING;
				break;
			case ColumnType.TINYINT:
			case ColumnType.SMALLINT:
			case ColumnType.MEDIUMINT:
			case ColumnType.INT:
			case ColumnType.BIGINT:
			case ColumnType.FLOAT:
			case ColumnType.DECIMAL:
			case ColumnType.DOUBLE:
				type = CommonConstant.NUMBER;
				break;
		}

		return type;
	}


	/**
	 * 사용하고자 하는 컬럼 목록을 이용하여 데이터 구조 컬럼 목록을 재정의한다.
	 * @param schemaVoList - 데이터 구조 컬럼 목록
	 * @param usableColumnNameList - 사용하고자 하는 컬럼 목록
	 * @return
	 */
	public List<SchemaVo> refineSchemaVo(List<SchemaVo> schemaVoList, ArrayList<String> usableColumnNameList) {

		List<SchemaVo> refineSchemaVoList = new ArrayList<>();

		schemaVoList.stream().filter(o -> usableColumnNameList.contains(o.getColumnName())).forEach(o -> {
			refineSchemaVoList.add(o);
		});

		return refineSchemaVoList;

	}

	/**
	 * 제외하고자 하는 컬럼 목록을 이용하여 데이터 구조 컬럼 목록을 재정의한다.
	 * @param schemaVoList - 데이터 구조 컬럼 목록
	 * @param ignoreColumnNameList - 제외하고자 하는 컬럼 목록
	 * @return
	 */
	public List<SchemaVo> refineSchemaVoWithIgnoreColumns(List<SchemaVo> schemaVoList, ArrayList<String> ignoreColumnNameList) {

		List<SchemaVo> refinedSchemaVoList = new ArrayList<>();
		schemaVoList.stream().filter(o -> !ignoreColumnNameList.contains(o.getColumnName())).forEach(o -> {
			refinedSchemaVoList.add(o);
		});

		return refinedSchemaVoList;

	}


	/**
	 * 테이블 스키마의 컬럼이름을 Alias 하여 사용하는 경우 컬럼 이름을 변경한다
	 * @param schemaVoList - 데이터 구조 컬럼 목록
	 * @param aliasColumnNameVoList - Alias 컬럼 목록
	 * @return
	 */
	public List<SchemaVo> refineSchemaVoWithAliasColumns(List<SchemaVo> schemaVoList, List<AliasColumnNameVo> aliasColumnNameVoList) {

		List<SchemaVo> refinedSchemaVoList = new ArrayList<>();

		schemaVoList.stream().forEach(s -> {
		    aliasColumnNameVoList.stream()
		        .filter(c -> c.getOriginalColumnName().equals(s.getColumnName())).forEach(c -> {
		            s.setColumnName(c.getAliasColumnName());
		        });

		    	refinedSchemaVoList.add(s);
		});


		return refinedSchemaVoList;
	}

}
