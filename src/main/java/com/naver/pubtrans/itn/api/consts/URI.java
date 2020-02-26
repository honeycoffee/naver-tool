package com.naver.pubtrans.itn.api.consts;

public interface URI {

	String ROOT_PATH = "/ntool/api" ;
	
	
	/**
	 * Sample URI
	 */
	String SAMPLE = "/samples" ;
	String SAMPLE_DETAIL = SAMPLE + "/{id}" ;
	String SAMPLE_REGISTER = SAMPLE + "/register" ;
	String SAMPLE_EDIT = SAMPLE + "/edit" ;
	String SAMPLE_DATA_STRUCTURE = SAMPLE + "/structure" ;
	
	
	/**
	 * 인증
	 */
	String AUTH = "/auth" ;
	String AUTH_LOGIN = AUTH + "/login" ;
	String AUTH_REFRESH_TOKEN = AUTH + "/token" ;
	
	
	/**
	 * 회원
	 */
	String MEMBER = ROOT_PATH + "/members" ;
	String MEMBER_DETAIL = MEMBER + "/{id}" ;
	String MEMBER_REGISTER = MEMBER + "/register" ;
	String MEMBER_EDIT = MEMBER + "/edit" ;
	String MEMBER_DATA_STRUCTURE = MEMBER + "/structure" ;
	String MEMBER_DUPLICATE = MEMBER + "/duplicate" ;
	
	
	
	/**
	 * 버스
	 */
	String BUS = ROOT_PATH + "/bus" ;
	
	
	/**
	 * 버스 정류장
	 */
	String STATION = BUS + "/stations" ;
	String STATION_DETAIL = STATION + "/{id}" ;
	String STATION_REGISTER = STATION + "/register" ;
	String STATION_EDIT = STATION + "/edit" ;
}
