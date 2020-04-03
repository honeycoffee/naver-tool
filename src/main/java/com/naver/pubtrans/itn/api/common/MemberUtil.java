package com.naver.pubtrans.itn.api.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.naver.pubtrans.itn.api.auth.JwtAdapter;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.vo.member.output.MemberOutputVo;

/**
 * 사용자와 관련된 메소드를 모아놓은 유틸.
 * @author westwind
 *
 */
@Component
public class MemberUtil {

	private static JwtAdapter jwtAdapter;

	@SuppressWarnings("static-access")
	@Autowired
	MemberUtil(JwtAdapter jwtAdapter) {
		this.jwtAdapter = jwtAdapter;
	}

	/**
	* accessToken 으로 회원 ID를 가져온다.
	* @return
	 */
	public static String getUserIdFromAccessToken() throws Exception {

		String accessToken =  (String)Util.getHttpServletRequest()
			.getAttribute(CommonConstant.ACCESS_TOKEN_KEY);

		MemberOutputVo memberOutputVo = jwtAdapter.extractUserDataFromToken(accessToken);
		return memberOutputVo.getUserId();

	}

	/**
	 * accessToken 으로 회원 정보를 가져온다.
	 * @return
	 */
	public static MemberOutputVo getMemberFromAccessToken() throws Exception {

		String accessToken =  (String)Util.getHttpServletRequest()
			.getAttribute(CommonConstant.ACCESS_TOKEN_KEY);

		MemberOutputVo memberOutputVo = jwtAdapter.extractUserDataFromToken(accessToken);
		return memberOutputVo;

	}

}
