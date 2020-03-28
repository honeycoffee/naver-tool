package com.naver.pubtrans.itn.api.vo.bus.stop;

import lombok.Getter;
import lombok.Setter;


/**
 * 네이버 버스 정류장과 BIS 버스 정류장 매핑정보
 * @author adtec10
 *
 */
@Getter
@Setter
public class BusStopMappingVo {

	// 네이버 정류장 ID
	private int stopId;

	// BIS 정류장 ID
	private String localStopId;

	// BIS 지역ID
	private int providerId;

	// BIS 버스정류장 표출 ID
	private String displayId;
}
