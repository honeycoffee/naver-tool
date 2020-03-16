package com.naver.pubtrans.itn.api.vo.common;


import lombok.Getter;
import lombok.Setter;

/**
 * 테이블 스키마 구조를 정의하는 VO
 * @author adtec10
 *
 */
@Getter
@Setter
public class SchemaVo {

	// 컬럼 이름
    private String columnName;

    // 컬럼 코멘트
    private String columnComment;

    // Null 허용여부
    private String isNullable;

    // 컬럼 Key
    private String columnKey;

    // 컬럼 타입
    private String columnType;

}
