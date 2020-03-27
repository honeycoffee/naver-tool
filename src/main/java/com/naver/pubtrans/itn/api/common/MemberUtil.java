package com.naver.pubtrans.itn.api.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

/**
 * 사용자와 관련된 메소드를 모아놓은 유틸.
 * @author westwind
 *
 */
@Component
public class MemberUtil {

	private static JwtAdapter jwtAdapter;

	// API 호출 시 Filter에서 header에 있는 ACCESS_TOKEN 값이 입력됨.
	public static String ACCESS_TOKEN = "";

	@SuppressWarnings("static-access")
	@Autowired
	MemberUtil(JwtAdapter jwtAdapter) {
		this.jwtAdapter = jwtAdapter;
	}

	/**
	* accessToken 으로 회원 ID를 가져온다.
	* @return
	 */
	public static String getUserIdFromToken() throws Exception {
		MemberOutputVo memberOutputVo = jwtAdapter.extractUserDataFromToken(ACCESS_TOKEN);
		return memberOutputVo.getUserId();

	}

	/**
	 * accessToken 으로 회원 정보를 가져온다.
	 * @return
	 */
	public static MemberOutputVo getMemberFromToken() throws Exception {

		MemberOutputVo memberOutputVo = jwtAdapter.extractUserDataFromToken(ACCESS_TOKEN);
		return memberOutputVo;

	}

}
