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

	private final JwtAdapter jwtAdapter;

	@Autowired
	MemberUtil(JwtAdapter jwtAdapter) {
		this.jwtAdapter = jwtAdapter;
	}

	/**
	 * AccessToken 으로 회원 ID를 가져온다.
	 * accessToken : 회원정보를 추출할 토큰
	 * @return
	 */
	public String getUserIdFromToken(String accessToken) throws Exception {

		MemberOutputVo memberOutputVo = jwtAdapter.extractUserDataFromToken(accessToken);
		return memberOutputVo.getUserId();

	}

}