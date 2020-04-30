package com.naver.pubtrans.itn.api.config.mybatis.typehandler;

import org.apache.ibatis.type.MappedTypes;

import com.naver.pubtrans.itn.api.consts.TaskType;

/**
 * Task 작업 구분 타입 핸들러
 * @author adtec10
 *
 */
@MappedTypes(TaskType.class)
public class TaskTypeTypeHandler extends StringCodeEnumTypeHandler<TaskType> {

	public TaskTypeTypeHandler() {
		super(TaskType.class);
	}

}
