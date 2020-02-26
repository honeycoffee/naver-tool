package com.naver.pubtrans.itn.api.vo.member.input;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInputVo {
	
	@NotEmpty
	@Size(max=30)
	private String userId ;

	@NotEmpty
	@Size(max=30)
	private String userName ;

	@NotEmpty
	@Size(min=8, max=20)
	private String userPw ;

	@Size(max=50)
	private String company ;

}

