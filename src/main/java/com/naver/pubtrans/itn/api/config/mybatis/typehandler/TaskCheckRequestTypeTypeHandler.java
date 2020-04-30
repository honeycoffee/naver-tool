package com.naver.pubtrans.itn.api.config.mybatis.typehandler;

import org.apache.ibatis.type.MappedTypes;

import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;

/**
 * 작업 검수요청 구분 타입 핸들러
 * @author adtec10
 *
 */
@MappedTypes(TaskCheckRequestType.class)
public class TaskCheckRequestTypeTypeHandler extends StringCodeEnumTypeHandler<TaskCheckRequestType> {

	public TaskCheckRequestTypeTypeHandler() {
		super(TaskCheckRequestType.class);
	}

}
