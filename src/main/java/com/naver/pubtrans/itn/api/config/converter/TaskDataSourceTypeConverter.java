package com.naver.pubtrans.itn.api.config.converter;

import org.springframework.core.convert.converter.Converter;

import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;

/**
 * 작업 데이터 출처 구분 컨버터
 * @author adtec10
 *
 */
public class TaskDataSourceTypeConverter implements Converter<String, TaskDataSourceType> {

	@Override
	public TaskDataSourceType convert(String source) {
		try {
			return TaskDataSourceType.getTaskDataSourceType(Integer.parseInt(source));
		} catch (Exception e) {
			return null;
		}
	}

}
