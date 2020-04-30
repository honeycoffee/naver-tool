package com.naver.pubtrans.itn.api.config.converter;

import org.springframework.core.convert.converter.Converter;

import com.naver.pubtrans.itn.api.consts.TaskStatusType;

/**
 * Task 진행 상태 구분 컨버터
 * @author adtec10
 *
 */
public class TaskStatusTypeConverter implements Converter<String, TaskStatusType> {

	@Override
	public TaskStatusType convert(String source) {
		try {
			return TaskStatusType.getTaskStatusType(source);
		} catch (Exception e) {
			return null;
		}
	}

}
