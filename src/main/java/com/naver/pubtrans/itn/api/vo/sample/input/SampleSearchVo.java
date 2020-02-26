package com.naver.pubtrans.itn.api.vo.sample.input;

import com.naver.pubtrans.itn.api.vo.common.SearchVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SampleSearchVo extends SearchVo {

	private int id ;
	
	private String sampleData ;
}
