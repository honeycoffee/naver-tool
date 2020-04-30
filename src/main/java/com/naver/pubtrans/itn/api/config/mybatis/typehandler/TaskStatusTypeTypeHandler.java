package com.naver.pubtrans.itn.api.config.mybatis.typehandler;

import org.apache.ibatis.type.MappedTypes;

import com.naver.pubtrans.itn.api.consts.TaskStatusType;

/**
 * Task 진행 상태 타입 핸들러
 * @author adtec10
 *
 */
@MappedTypes(TaskStatusType.class)
public class TaskStatusTypeTypeHandler extends StringCodeEnumTypeHandler<TaskStatusType> {

	public TaskStatusTypeTypeHandler() {
		super(TaskStatusType.class);
	}

}
