package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.pubtrans.itn.api.common.OutputFmtUtil;
import com.naver.pubtrans.itn.api.consts.CodeType;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.consts.PubTransTable;
import com.naver.pubtrans.itn.api.consts.PubTransType;
import com.naver.pubtrans.itn.api.consts.TaskCheckRequestType;
import com.naver.pubtrans.itn.api.consts.TaskDataSourceType;
import com.naver.pubtrans.itn.api.consts.TaskStatusType;
import com.naver.pubtrans.itn.api.consts.TaskType;
import com.naver.pubtrans.itn.api.repository.CommonRepository;
import com.naver.pubtrans.itn.api.vo.common.AliasColumnNameVo;
import com.naver.pubtrans.itn.api.vo.common.BusProviderVo;
import com.naver.pubtrans.itn.api.vo.common.BusRouteClassVo;
import com.naver.pubtrans.itn.api.vo.common.CityCodeVo;
import com.naver.pubtrans.itn.api.vo.common.FieldValue;
import com.naver.pubtrans.itn.api.vo.common.SchemaVo;
import com.naver.pubtrans.itn.api.vo.common.TransportVo;
import com.naver.pubtrans.itn.api.vo.common.output.CommonSchema;
import com.naver.pubtrans.itn.api.vo.member.AuthorityInfoVo;

/**
 * 공통 서비스
 * @author adtec10
 *
 */
@Service
public class CommonService {

	// 테이블 스키마 정보
	private Hashtable<String, List<SchemaVo>> tableSchemaMap = new Hashtable<>();

	// 공통 코드정보
	private Hashtable<String, List<FieldValue>> commonCodeMap = new Hashtable<>();


	private final OutputFmtUtil outputFmtUtil;

	private final CommonRepository commonRepository;

	@Autowired
	CommonService(OutputFmtUtil outputFmtUtil, CommonRepository commonRepository){
		this.outputFmtUtil = outputFmtUtil;
		this.commonRepository = commonRepository;
	}


	/**
	 * 도시코드 목록 가져오기
	 * @return
	 */
	public List<FieldValue> selectCityCodeAll(){


		// 전체 도시코드 목록
		List<CityCodeVo> citycodeVoList = commonRepository.selectCityCodeList();


		List<FieldValue> fieldValueList = new ArrayList<>();
		for(CityCodeVo citycodeVo : citycodeVoList) {
			fieldValueList.add(new FieldValue(citycodeVo.getCityCode(), citycodeVo.getCityName()));
		}

		return fieldValueList;
	}


	/**
	 * BIS 공급지역 목록 가져오기
	 * @return
	 */
	public List<FieldValue> selectBusProviderListAll(){
		List<BusProviderVo> busProviderVoList = commonRepository.selectBusProviderList();

		List<FieldValue> fieldValueList = new ArrayList<>();
		for(BusProviderVo vo : busProviderVoList) {
			fieldValueList.add(new FieldValue(vo.getProviderId(), vo.getProviderName()));
		}

		return fieldValueList;
	}

	/**
	 * 전체 버스 노선 Class 목록 가져오기
	 * @return
	 */
	public List<FieldValue> selectBusRouteClassListAll(){
		List<BusRouteClassVo> busRouteClassVoList = commonRepository.selectBusRouteClassList();

		List<FieldValue> fieldValueList = new ArrayList<>();
		for(BusRouteClassVo vo : busRouteClassVoList) {
			fieldValueList.add(new FieldValue(vo.getBusClassId(), vo.getBusClassName()));
		}

		return fieldValueList;
	}

	/**
	 * 회원 권한 목록 가져오기
	 * @return
	 */
	public List<FieldValue> selectAuthorityInfoListAll(){
		List<AuthorityInfoVo> authorityInfoVoList = commonRepository.selectAuthorityInfoList();

		List<FieldValue> fieldValueList = new ArrayList<>();
		for(AuthorityInfoVo vo : authorityInfoVoList) {
			fieldValueList.add(new FieldValue(vo.getAuthorityId(), vo.getAuthorityName()));
		}

		return fieldValueList;
	}

	/**
	 * 대중교통 수단 목록 가져오기
	 * @return
	 */
	public List<FieldValue> selectTransportInfoListAll() {
		List<TransportVo> transportVoList = commonRepository.selectTransportList();

		List<FieldValue> fieldValueList = new ArrayList<>();
		for(TransportVo vo : transportVoList) {
			fieldValueList.add(new FieldValue(vo.getTranportId(), vo.getName()));
		}

		return fieldValueList;
	}


	/**
	 * 작업 데이터 출처 코드목록 전체 가져오기
	 * @return
	 */
	public List<FieldValue> selectTaskDataSourceTypeListAll() {
		List<FieldValue> fieldValueList = new ArrayList<>();
		for(TaskDataSourceType taskDataSourceType : TaskDataSourceType.values()) {

			fieldValueList.add(new FieldValue(taskDataSourceType.getCode(), taskDataSourceType.getCodeName()));
		}
		return fieldValueList;
	}

	/**
	 * 작업 검수요청 구분 코드목록 전체 가져오기
	 * @return
	 */
	public List<FieldValue> selectTaskCheckRequestTypeListAll() {
		List<FieldValue> fieldValueList = new ArrayList<>();
		for(TaskCheckRequestType taskCheckRequestType : TaskCheckRequestType.values()) {
			fieldValueList.add(new FieldValue(taskCheckRequestType.getCode(), taskCheckRequestType.getDescription()));
		}
		return fieldValueList;
	}

	/**
	 * 작업 진행상태 구분 코드목록 전체 가져오기
	 * @return
	 */
	public List<FieldValue> selectTaskStatusTypeListAll() {
		List<FieldValue> fieldValueList = new ArrayList<>();
		for(TaskStatusType taskStatusType : TaskStatusType.values()) {
			fieldValueList.add(new FieldValue(taskStatusType.getCode(), taskStatusType.getDescription()));
		}
		return fieldValueList;
	}

	/**
	 * 대중교통 구분 코드목록 전체 가져오기
	 * @return
	 */
	public List<FieldValue> selectPubTransTypeListAll() {
		List<FieldValue> fieldValueList = new ArrayList<>();
		for(PubTransType pubTransType : PubTransType.values()) {
			fieldValueList.add(new FieldValue(pubTransType.getCode(), pubTransType.getDescription()));
		}
		return fieldValueList;
	}

	/**
	 * 작업 구분 코드목록 전체 가져오기
	 * @return
	 */
	public List<FieldValue> selectTaskTypeListAll() {
		List<FieldValue> fieldValueList = new ArrayList<>();
		for(TaskType taskType : TaskType.values()) {
			fieldValueList.add(new FieldValue(taskType.getCode(), taskType.getDescription()));
		}
		return fieldValueList;
	}


	/**
	 * 공통 코드 목록을 가져온다
	 * @param codeName - 코드타입(CodType.class 참조)
	 * @return
	 */
	public List<FieldValue> selectCommonCode(String codeName) throws Exception {

		if(!commonCodeMap.containsKey(codeName)) {
			this.initCommonCode();
		}

		return commonCodeMap.get(codeName);
	}


	/**
	 * 공통 스미카 목록을 가져온다
	 * @param tableName - 테이블명
	 * @param refineType - 스키마 재정의 타입
	 * @param columnNameList - 재정의 대상 컬럼 목록
	 * @return
	 * @throws Exception
	 */
	public List<CommonSchema> selectCommonSchemaList(String tableName, String refineType, ArrayList<String> columnNameList) throws Exception {


		// 테이블 조회
		if(!tableSchemaMap.containsKey(tableName)) {
			this.initTableSchema();
		}

		List<SchemaVo> schemaVoList = tableSchemaMap.get(tableName);

		// 별칭 컬럼목록 재정의
		List<AliasColumnNameVo> aliasColumnNameVoList = this.selectAliasColumnNameList();
		schemaVoList = outputFmtUtil.refineSchemaVoWithAliasColumns(schemaVoList, aliasColumnNameVoList);


		// 사용/미사용 컬럼을 통한 스키마 재정의
		if(Objects.nonNull(columnNameList)) {
			if(refineType.equals(CommonConstant.USABLE_COLUMN)) {
				schemaVoList = outputFmtUtil.refineSchemaVo(schemaVoList, columnNameList);
			}else if(refineType.equals(CommonConstant.EXCEPTION_COLUMN)) {
				schemaVoList = outputFmtUtil.refineSchemaVoWithExceptionColumns(schemaVoList, columnNameList);
			}
		}


		// 코드목록 정의
		HashMap<String, List<FieldValue>> fieldValuesMap = this.getFieldValueListMap();


		List<CommonSchema> commonSchemaList = outputFmtUtil.makeCommonSchema(schemaVoList, null, fieldValuesMap);

		return commonSchemaList;
	}


	/**
	 * 테이블 컬럼명을 사용하지 않고 별칭을 이용하는 목록을 정의한다
	 * @return
	 * @throws Exception
	 */
	public List<AliasColumnNameVo> selectAliasColumnNameList() throws Exception {

		// Alias 컬럼 이름 재정의
		List<AliasColumnNameVo> aliasColumnNameVoList = new ArrayList<>();
		aliasColumnNameVoList.add(new AliasColumnNameVo("x", "longitude"));			// tb_stops
		aliasColumnNameVoList.add(new AliasColumnNameVo("y", "latitude"));				// tb_stops

		aliasColumnNameVoList.add(new AliasColumnNameVo("do", "sido"));				// tb_bus_stop_info

		aliasColumnNameVoList.add(new AliasColumnNameVo("bypass_start_date", "bypass_start_date_time"));		// tb_bus_routes_info
		aliasColumnNameVoList.add(new AliasColumnNameVo("bypass_end_date", "bypass_end_date_time"));			// tb_bus_routes_info


		return aliasColumnNameVoList;
	}


	/**
	 * 공통 코드 목록
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, List<FieldValue>> getFieldValueListMap() throws Exception {

		HashMap<String, List<FieldValue>> fieldValuesMap = new HashMap<>();

		/*
		 * 정류장 부가 정보
		 */
		fieldValuesMap.put("nonstop_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		fieldValuesMap.put("virtual_stop_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		fieldValuesMap.put("center_stop_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		fieldValuesMap.put("city_code", this.selectCommonCode(CodeType.CITYCODE.getCodeName()));
		fieldValuesMap.put("provider_id", this.selectCommonCode(CodeType.BUS_PROVIDER.getCodeName()));
		fieldValuesMap.put("transport_id", this.selectCommonCode(CodeType.TRANSPORT_ID.getCodeName()));
		/*
		 * 버스 노선 부가정보
		 */
		fieldValuesMap.put("bus_class", this.selectCommonCode(CodeType.BUS_ROUTE_CLASS.getCodeName()));
		fieldValuesMap.put("nonstep_bus_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		fieldValuesMap.put("bypass_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		/*
		 * 서비스 날짜 정보
		 */
		fieldValuesMap.put("monday_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		fieldValuesMap.put("tuesday_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		fieldValuesMap.put("wednesday_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		fieldValuesMap.put("thursday_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		fieldValuesMap.put("friday_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		fieldValuesMap.put("saturday_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		fieldValuesMap.put("sunday_yn", this.selectCommonCode(CodeType.Y_N.getCodeName()));
		/*
		 * 회원 권한 정보
		 */
		fieldValuesMap.put("authority_id", this.selectCommonCode(CodeType.AUTHORITY_ID.getCodeName()));
		/**
		 * Task 코드 정보
		 */
		fieldValuesMap.put("task_data_source_type", this.selectCommonCode(CodeType.TASK_DATA_SOURCE_TYPE.getCodeName()));
		fieldValuesMap.put("task_check_request_type", this.selectCommonCode(CodeType.TASK_CHECK_REQUEST_TYPE.getCodeName()));
		fieldValuesMap.put("pub_trans_type", this.selectCommonCode(CodeType.PUB_TRANS_TYPE.getCodeName()));
		fieldValuesMap.put("task_status_type", this.selectCommonCode(CodeType.TASK_STATUS_TYPE.getCodeName()));
		fieldValuesMap.put("task_type", this.selectCommonCode(CodeType.TASK_TYPE.getCodeName()));


		return fieldValuesMap;
	}

	/**
	 * 공통코드 전체를 로드한다
	 * @throws Exception
	 */
	@PostConstruct
	public void initCommonCode() throws Exception {

		for(CodeType codeType : CodeType.values()) {

			List<FieldValue> fieldValueList = new ArrayList<>();

			if(codeType.getCodeName().equals(CodeType.CITYCODE.getCodeName())) {				// 도시코드
				fieldValueList = this.selectCityCodeAll();
			}else if(codeType.getCodeName().equals(CodeType.BUS_PROVIDER.getCodeName())) {		// BIS 공급지역
				fieldValueList = this.selectBusProviderListAll();
			}else if(codeType.getCodeName().equals(CodeType.BUS_ROUTE_CLASS.getCodeName())) {	// 버스 노선 클래스
				fieldValueList = this.selectBusRouteClassListAll();
			}else if(codeType.getCodeName().equals(CodeType.AUTHORITY_ID.getCodeName())) {		// 회원 권한 ID
				fieldValueList = this.selectAuthorityInfoListAll();
			}else if(codeType.getCodeName().equals(CodeType.TRANSPORT_ID.getCodeName())) {		// 대중교통 구분 ID
				fieldValueList = this.selectTransportInfoListAll();
			}else if(codeType.getCodeName().equals(CodeType.Y_N.getCodeName())) {				// Y/N 선택
				FieldValue fY = new FieldValue(CommonConstant.Y, CommonConstant.Y);
				FieldValue fN = new FieldValue(CommonConstant.N, CommonConstant.N);
				fieldValueList.add(fY);
				fieldValueList.add(fN);
			}else if(codeType.getCodeName().equals(CodeType.TASK_DATA_SOURCE_TYPE.getCodeName())) {	// 작업 데이터 출처
				fieldValueList = this.selectTaskDataSourceTypeListAll();
			}else if(codeType.getCodeName().equals(CodeType.TASK_CHECK_REQUEST_TYPE.getCodeName())) {	// 작업 검수요청 구분
				fieldValueList = this.selectTaskCheckRequestTypeListAll();
			}else if(codeType.getCodeName().equals(CodeType.TASK_STATUS_TYPE.getCodeName())) {		// 작업 진행상태
				fieldValueList = this.selectTaskStatusTypeListAll();
			}else if(codeType.getCodeName().equals(CodeType.PUB_TRANS_TYPE.getCodeName())) {		// 대중교통 데이터 구분
				fieldValueList = this.selectPubTransTypeListAll();
			}else if(codeType.getCodeName().equals(CodeType.TASK_TYPE.getCodeName())) {				// 작업 구분
				fieldValueList = this.selectTaskTypeListAll();
			}

			commonCodeMap.put(codeType.getCodeName(), fieldValueList);

		}


	}

	/**
	 * 테이블 스키마 전체를 로드한다
	 * @throws Exception
	 */
	@PostConstruct
	public void initTableSchema() throws Exception {

		// Table 전체 로드
		for(PubTransTable pubTransTable : PubTransTable.values()) {
			tableSchemaMap.put(pubTransTable.getName(), commonRepository.selectTableSchema(pubTransTable.getName()));
		}


	}

}
