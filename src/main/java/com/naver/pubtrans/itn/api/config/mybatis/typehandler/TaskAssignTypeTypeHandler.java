package com.naver.pubtrans.itn.api.config.mybatis.typehandler;

import org.apache.ibatis.type.MappedTypes;

import com.naver.pubtrans.itn.api.consts.TaskAssignType;

/**
 * Task 할당 구분 타입 핸들러
 * @author adtec10
 *
 */
@MappedTypes(TaskAssignType.class)
public class TaskAssignTypeTypeHandler extends StringCodeEnumTypeHandler<TaskAssignType>  {

	public TaskAssignTypeTypeHandler() {
		super(TaskAssignType.class);
	}

}
