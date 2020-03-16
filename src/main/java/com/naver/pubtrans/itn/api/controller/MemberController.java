package com.naver.pubtrans.itn.api.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.naver.pubtrans.itn.api.common.MemberUtil;
import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.exception.ApiException;
import com.naver.pubtrans.itn.api.service.MemberService;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;
import com.naver.pubtrans.itn.api.vo.common.output.CommonResult;
import com.naver.pubtrans.itn.api.vo.member.input.MemberInputVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberParameterVo;
import com.naver.pubtrans.itn.api.vo.member.input.MemberSearchVo;

/**
 * 네이버 대중교통 DB 내재화 사용자관리 컨트롤러
 * 
 * @author westwind
 *
 */
@Slf4j
@RestController
public class MemberController {

	private final MemberService memberService;

	private final OutputFmtUtil outputFmtUtil;

	private final MemberUtil memberUtil;

	@Autowired
	MemberController(MemberService memberService, OutputFmtUtil outputFmtUtil, MemberUtil memberUtil) {
		this.outputFmtUtil = outputFmtUtil;
		this.memberService = memberService;
		this.memberUtil = memberUtil;
	}

	/**
	 * 회원 정보를 등록한다.
	 * <pre>
	 * Valid를 이용하여 유효성 검사를 진행한다
	 * </pre>
	 * @param memberInputVo - 입력값
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/v1/ntool/api/member")
	public CommonOutput insertMember(@RequestBody @Valid MemberInputVo memberInputVo) throws Exception {

		// 데이터 저장 서비스
		memberService.insertMember(memberInputVo);

		return new CommonOutput();

	}

	/**
	 * 회원 ID 중복 체크
	 * @param userId - 체크할 회원 ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/v1/ntool/api/duplicate/member")
	public CommonOutput checkDuplicate(@RequestParam(name = "userId", required = true) String userId) throws Exception {

		// ID 중복 체크
		CommonResult commonResult = memberService.checkDuplicate(userId);

		return new CommonOutput(commonResult);

	}

	/**
	 * 회원 비밀번호 검증
	 * @param memberParameterVo - 회원 파라미터 Vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/v1/ntool/api/verify/password")
	public CommonOutput verifyPassword(@RequestBody @Valid MemberParameterVo memberParameterVo) throws Exception {

		if (StringUtils.isEmpty(memberParameterVo.getUserPw())) {
			throw new ApiException(ResultCode.PARAMETER_ERROR.getApiErrorCode(),
				ResultCode.PARAMETER_ERROR.getDisplayMessage());
		}

		// TODO : Spring Security AccessToken 적용은 추후에 작업 예정으로 test를 위해 만료기한 3개월의 accessToken 임시 사용 (userId : test, userPw : qwer1234)
		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RfdXNlciIsImV4cCI6MTU5MTgzMTM2NCwidXNlcklkIjoidGVzdCJ9.J3jWR6IDJU6Ly_okU-T3F8lSQXC9tpgbX6TSH7R8hHo";

		String userId = memberUtil.getUserIdFromToken(accessToken);
		memberParameterVo.setUserId(userId);

		CommonResult commonResult = memberService.verifyPassword(memberParameterVo);

		return new CommonOutput(commonResult);

	}

	/**
	 * 자신의 정보를 조회한다.
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/v1/ntool/api/me")
	public CommonOutput getMe() throws Exception {

		// TODO : Spring Security AccessToken 적용은 추후에 작업 예정으로 test를 위해 만료기한 3개월의 accessToken 임시 사용 (userId : test, userPw : qwer1234)
		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3RfdXNlciIsImV4cCI6MTU5MTgzMTM2NCwidXNlcklkIjoidGVzdCJ9.J3jWR6IDJU6Ly_okU-T3F8lSQXC9tpgbX6TSH7R8hHo";

		String userId = memberUtil.getUserIdFromToken(accessToken);

		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId(userId);

		CommonResult commonResult = memberService.getMemberDataWithSchema(memberSearchVo);

		return new CommonOutput(commonResult);

	}

	/**
	 * 자신의 정보를 수정한다.
	 * <pre>
	 * Valid를 이용하여 유효성 검사를 진행한다
	 * </pre>
	 * @param memberInputVo - 회원 정보 입력값
	 * @return
	 * @throws Exception
	 */
	@PutMapping(value = "/v1/ntool/api/me")
	public CommonOutput updateMe(@RequestBody @Valid MemberInputVo memberInputVo) throws Exception {

		memberService.updateMe(memberInputVo);

		return new CommonOutput();

	}

	/**
	 * 권한관리에서 회원 정보를 조회한다
	 * @param userId - 체크할 회원 ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/v1/ntool/api/member/{userId}")
	public CommonOutput getMember(@PathVariable String userId) throws Exception {

		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId(userId);

		// 회원 데이터 및 테이블 상세 구조 조회
		CommonResult commonResult = memberService.getMemberDataWithSchema(memberSearchVo);

		return new CommonOutput(commonResult);

	}

	/**
	 * 권한관리에서 회원 정보를 수정한다.
	 * <pre>
	 * Valid를 이용하여 유효성 검사를 진행한다
	 * </pre>
	 * @param memberInputVo - 회원 정보 입력 값
	 * @return
	 * @throws Exception
	 */
	@PutMapping(value = "/v1/ntool/api/member")
	public CommonOutput updateMember(@RequestBody @Valid MemberInputVo memberInputVo) throws Exception {

		memberService.updateMember(memberInputVo);

		return new CommonOutput();

	}

	/**
	 * 회원 정보를 삭제한다.
	 * @param memberParameterVo - 회원 파라미터 Vo
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping(value = "/v1/ntool/api/member")
	public CommonOutput deleteMember(@RequestBody MemberParameterVo memberParameterVo) throws Exception {

		if (StringUtils.isEmpty(memberParameterVo.getUserId())) {
			throw new ApiException(ResultCode.PARAMETER_ERROR.getApiErrorCode(),
				ResultCode.PARAMETER_ERROR.getDisplayMessage());
		}

		MemberSearchVo memberSearchVo = new MemberSearchVo();
		memberSearchVo.setUserId(memberParameterVo.getUserId());

		memberService.deleteMember(memberSearchVo);

		return new CommonOutput();

	}

	/**
	 * 회원 목록 조회
	 * @param memberSearchVo - 검색 조건
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v1/ntool/api/list/member")
	public CommonOutput listMember(MemberSearchVo memberSearchVo) throws Exception {
		CommonResult commonResult = memberService.selectMemberList(memberSearchVo);
		return new CommonOutput(commonResult);
	}

	/**
	 * 회원 데이터 상세 구조를 조회한다
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/v1/ntool/api/schema/member")
	public CommonOutput selectMemberSchema() throws Exception {
		CommonResult commonResult = outputFmtUtil.setCommonDocFmt(memberService.selectMemberSchema());
		return new CommonOutput(commonResult);
	}

}
