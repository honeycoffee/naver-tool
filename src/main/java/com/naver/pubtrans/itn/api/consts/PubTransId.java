package com.naver.pubtrans.itn.api.consts;

import lombok.Getter;

/**
 * 네이버 대중교통 ID 범위
 * @author adtec10
 *
 */
@Getter
public enum PubTransId {

	STOP_MIN(55000000, "정류장 ID 최소 범위"),
	STOP_MAX(55999999, "정류장 ID 최대 범위"),
	ROUTE_MIN(11000000, "노선 ID 최소 범위"),
	ROUTE_MAX(11999999, "노선 ID 최대 범위");

	// 아이디
	private int id;

	// 설명
	private String description;

	PubTransId(int id, String description){
		this.id = id;
		this.description = description;
	}
}
