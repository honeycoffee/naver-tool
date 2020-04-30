package com.naver.pubtrans.itn.api.config.converter;

import org.springframework.core.convert.converter.Converter;

import com.naver.pubtrans.itn.api.consts.TaskType;

/**
 * Task 작업 구분 컨버터
 * @author adtec10
 *
 */
public class TaskTypeConverter implements Converter<String, TaskType> {

	@Override
	public TaskType convert(String source) {
		try {
			return TaskType.getTaskType(source);
		} catch (Exception e) {
			return null;
		}
	}

}
