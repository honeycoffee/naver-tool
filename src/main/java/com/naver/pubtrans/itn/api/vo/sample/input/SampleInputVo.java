package com.naver.pubtrans.itn.api.vo.sample.input;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SampleInputVo {
	
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private int id ;
	
	@NotEmpty
	@Size(max=100)
	private String sampleData ;

}

