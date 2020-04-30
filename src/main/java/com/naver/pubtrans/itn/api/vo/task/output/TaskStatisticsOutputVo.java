package com.naver.pubtrans.itn.api.vo.task.output;

import lombok.Getter;
import lombok.Setter;

/**
 * 작업 통계 출력정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class TaskStatisticsOutputVo {

	// 전체 개수
	private int totalCnt;

	// 대기상태 개수
	private int waitCnt;

	// 진행중 개수
	private int progressCnt;

	// 검수중 개수
	private int checkingCnt;

	// 검수완료 개수
	private int checkCompletionCnt;

	// 예외완료 개수
	private int exceptionCompletionCnt;

	// 진행률
	private double progressRate = 0.00;

}
