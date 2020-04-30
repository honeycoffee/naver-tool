package com.naver.pubtrans.itn.api.config.converter;

import org.springframework.core.convert.converter.Converter;

import com.naver.pubtrans.itn.api.consts.PubTransType;

/**
 * 대중교통 구분 컨버터
 * @author adtec10
 *
 */
public class PubTransTypeConverter implements Converter<String, PubTransType> {

	@Override
	public PubTransType convert(String source) {
		try {
			return PubTransType.getPubTransType(source);
		} catch (Exception e) {
			return null;
		}
	}

}
