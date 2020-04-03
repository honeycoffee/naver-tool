package com.naver.pubtrans.itn.api.consts;

import lombok.Getter;
/**
 * 버스 진행방향 기준 상/하행을 정의한다
 * @author adtec10
 *
 */
@Getter
public enum BusDirection {

	UP("U", "상행"),
	DOWN("D", "하행");

	// 코드
	private String code;

	// 설명
	private String descrition;

	BusDirection(String code, String description){
		this.code = code;
		this.descrition = description;
	}
}
