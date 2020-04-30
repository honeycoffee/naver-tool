package com.naver.pubtrans.itn.api.vo.fare.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;

/**
 * 요금 룰 삭제 작업등록 입력 정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareRuleRemoveTaskInputVo {

	/**
	 * 요금 룰 ID
	 */
	@NotNull
	private int fareId;

	/**
	 * 데이터 출처 - 기본값 : 7(기타)
	 */
	private TaskDataSourceType taskDataSourceType = TaskDataSourceType.ETC;

	/**
	 * 작업내용
	 */
	@NotBlank
	private String taskComment;

	/**
	 * 검수자 ID
	 */
	@NotBlank
	private String checkUserId;

}
