package com.naver.pubtrans.itn.api.vo.member.input;

import com.naver.pubtrans.itn.api.vo.common.SearchVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchVo extends SearchVo {

	private String userId;
	
}
