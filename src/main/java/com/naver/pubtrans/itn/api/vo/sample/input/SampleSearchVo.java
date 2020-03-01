package com.naver.pubtrans.itn.api.vo.sample.input;

import com.naver.pubtrans.itn.api.vo.common.SearchVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 샘플 목록 검색 파라미터
 * @author adtec10
 *
 */
@Getter
@Setter
public class SampleSearchVo extends SearchVo {

	private int id ;

	private String sampleData ;
}
