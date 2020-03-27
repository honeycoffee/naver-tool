package com.naver.pubtrans.itn.api.vo.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 버스노선 클래스 정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusRouteClassVo {

	// 버스 노선 클래스 ID
	private int busClassId;

	// 버스 노선타입 명
	private String busClassName;

	// 노선 색상정보
	private String color;

	// 노선 아이콘 명
	private String iconName;

	// 정렬 순서
	private int sortingPriority;

	// 도시코드
	private int cityCode;
}
