package com.naver.pubtrans.itn.api.config.mybatis.typehandler;

import org.apache.ibatis.type.MappedTypes;

import com.naver.pubtrans.itn.api.consts.PubTransType;

/**
 * 작업데이터 구분 타입 핸들러
 * @author adtec10
 *
 */
@MappedTypes(PubTransType.class)
public class PubTransTypeTypeHandler extends StringCodeEnumTypeHandler<PubTransType> {

	public PubTransTypeTypeHandler() {
		super(PubTransType.class);
	}

}
