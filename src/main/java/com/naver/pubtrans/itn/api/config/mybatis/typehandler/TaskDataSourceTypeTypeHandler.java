package com.naver.pubtrans.itn.api.config.mybatis.typehandler;

import org.apache.ibatis.type.MappedTypes;

import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;

/**
 * 작업 데이터 출처 구분 타입 핸들러
 * @author adtec10
 *
 */
@MappedTypes(TaskDataSourceType.class)
public class TaskDataSourceTypeTypeHandler extends IntegerCodeEnumTypeHandler<TaskDataSourceType> {

	public TaskDataSourceTypeTypeHandler() {
		super(TaskDataSourceType.class);
	}
}
