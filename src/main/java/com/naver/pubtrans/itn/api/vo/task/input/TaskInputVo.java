package com.naver.pubtrans.itn.api.vo.task.input;


import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.consts.TaskType;

/**
 * 작업 입력 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class TaskInputVo {

	// Task ID
	private long taskId;

	// 도시코드
	private int cityCode;

	// BIS 지자체 ID
	private Integer providerId;

	// 작업구분
	private TaskType taskType;

	// 진행상태
	private TaskStatusType taskStatusType;

	// 검수요청 구분
	private TaskCheckRequestType taskCheckRequestType = TaskCheckRequestType.CHECK_REQUEST;

	// 데이터 출처
	private TaskDataSourceType taskDataSourceType = TaskDataSourceType.ETC;

	// 대중교통 ID
	private int pubTransId;

	// 데이터 구분
	private PubTransType pubTransType;

	// 데이터 이름
	private String pubTransName;

	// 작업내용
	private String taskComment;

	// BIS 자동 변경내용
	private String bisAutoChangeData;

	//등록자명
	private String regUserName;

	// 등록자 ID
	private String regUserId;

	// 작업자명
	private String workUserName;

	// 작업자 ID
	private String workUserId;

	// 검수자명
	private String checkUserName;

	// 검수자 ID
	private String checkUserId;

}
