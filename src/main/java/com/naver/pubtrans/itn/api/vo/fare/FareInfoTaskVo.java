package com.naver.pubtrans.itn.api.vo.fare;

import lombok.Getter;
import lombok.Setter;

/**
 * 요금 룰 상세정보 작업정보
 * @author westwind
 *
 */
@Getter
@Setter
public class FareInfoTaskVo extends FareInfoVo {

	// 작업ID
	private long taskId;

}
