package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.repository.BusStopRepository;
import com.naver.pubtrans.itn.api.vo.bus.graph.BusStopGraphVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureGeometryVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.output.BusStopGraphOutputVo;
import com.naver.pubtrans.itn.api.vo.bus.stop.BusStopVo;

/**
 * 노선 그래프 정보 관리 서비스
 * @author adtec10
 *
 */
@Service
public class BusGraphService {

	private final ObjectMapper objectMapper;

	private final BusStopRepository busStopRepository;

	@Autowired
	BusGraphService(ObjectMapper objectMapper, BusStopRepository busStopRepository){
		this.objectMapper = objectMapper;
		this.busStopRepository = busStopRepository;
	}

	/**
	 * GeoJson형태의 Feature 목록을 만든다
	 * @param busStopGraphVoList - 검색된 그래프 정보 목록
	 * @return
	 * @throws Exception
	 */
	public List<GeoJsonFeatureVo> makeGeoJsonFeatureList(List<BusStopGraphVo> busStopGraphVoList) throws Exception {
		List<GeoJsonFeatureVo> geoJsonFeatureVoList = new ArrayList<>();

		for(BusStopGraphVo busStopGraphVo : busStopGraphVoList) {
			BusStopGraphSearchVo busStopGraphSearchVo = new BusStopGraphSearchVo(busStopGraphVo.getStartStopId(), busStopGraphVo.getEndStopId());
			GeoJsonFeatureVo geoJsonFeatureVo = this.makeGeoJsonFeature(busStopGraphSearchVo, busStopGraphVo);

			geoJsonFeatureVoList.add(geoJsonFeatureVo);
		}

		return geoJsonFeatureVoList;
	}


	/**
	 * GeoJson형태의 Feature 목록을 만든다
	 * @param busStopGraphSearchVoList - 정류장 검색조건 목록
	 * @param busStopGraphVoList - 검색된 그래프 정보 목록
	 * @return
	 * @throws Exception
	 */
	public List<GeoJsonFeatureVo> makeGeoJsonFeatureList(List<BusStopGraphSearchVo> busStopGraphSearchVoList, List<BusStopGraphVo> busStopGraphVoList) throws Exception {
		List<GeoJsonFeatureVo> geoJsonFeatureVoList = new ArrayList<>();

		for(BusStopGraphSearchVo busStopGraphSearchVo : busStopGraphSearchVoList) {
			GeoJsonFeatureVo geoJsonFeatureVo = null ;

			for(BusStopGraphVo busStopGraphVo : busStopGraphVoList) {
				if(busStopGraphSearchVo.getStartStopId() == busStopGraphVo.getStartStopId() && busStopGraphSearchVo.getEndStopId() == busStopGraphVo.getEndStopId()) {
					geoJsonFeatureVo = this.makeGeoJsonFeature(busStopGraphSearchVo, busStopGraphVo);
					break;
				}
			}

			if(Objects.isNull(geoJsonFeatureVo)) {
				geoJsonFeatureVo = this.makeGeoJsonFeature(busStopGraphSearchVo, null);
			}
			geoJsonFeatureVoList.add(geoJsonFeatureVo);

		}

		return geoJsonFeatureVoList;
	}

	/**
	 * 정류장 사이의 그래프 정보를 GeoJson 형태의 Vo로 생성한다
	 * @param busStopGraphSearchVo - 출/도착 정류장 검색 정보
	 * @param busStopGraphVo - 정류장 그래프 정보
	 * @return
	 * @throws Exception
	 */
	private GeoJsonFeatureVo makeGeoJsonFeature(BusStopGraphSearchVo busStopGraphSearchVo, BusStopGraphVo busStopGraphVo) throws Exception {
		GeoJsonFeatureVo geojsonFeatureVo = new GeoJsonFeatureVo();
		GeoJsonFeatureGeometryVo geoJsonFeatureGeometryVo = new GeoJsonFeatureGeometryVo();

		BusStopGraphOutputVo busStopGraphOutputVo = new BusStopGraphOutputVo();
		busStopGraphOutputVo.setStartStopId(busStopGraphSearchVo.getStartStopId());
		busStopGraphOutputVo.setEndStopId(busStopGraphSearchVo.getEndStopId());


		if(!Objects.isNull(busStopGraphVo)) {
			busStopGraphOutputVo.setMatchGraphInfoYn(CommonConstant.Y);
			busStopGraphOutputVo.setGraphId(busStopGraphVo.getGraphId());
			busStopGraphOutputVo.setStartStopName(busStopGraphVo.getStartStopName());
			busStopGraphOutputVo.setEndStopName(busStopGraphVo.getEndStopName());

			// db geometry 타입을  mysql 함수를 이용하여  geojson으로 변경한 텍스트 정보를 VO로 변환한다
			geoJsonFeatureGeometryVo = objectMapper.readValue(busStopGraphVo.getGraphInfoToGeoJson(), GeoJsonFeatureGeometryVo.class);
		}else {

			/**
			 * 정류장간의 그래프 정보가 없는경우 출발~도착 정류장의 두 지점을 일직선으로 하는 LineString을 임의로 생성한다.
			 */

			// 출발 정류장 정보
			BusStopVo startBusStopVo = busStopRepository.getBusStop(busStopGraphSearchVo.getStartStopId());

			// 도착 정류장 정보
			BusStopVo endBusStopVo = busStopRepository.getBusStop(busStopGraphSearchVo.getEndStopId());

			List<Double> startCoordinate = Arrays.asList(startBusStopVo.getLongitude(), startBusStopVo.getLatitude());
			List<Double> endCoordinate = Arrays.asList(endBusStopVo.getLongitude(), endBusStopVo.getLatitude());
			List<List<Double>> coordinates = Arrays.asList(startCoordinate, endCoordinate);

			geoJsonFeatureGeometryVo.setType(CommonConstant.GEOJSON_GEOMETRY_TYPE_LINE_STRING);
			geoJsonFeatureGeometryVo.setCoordinates(coordinates);


			busStopGraphOutputVo.setMatchGraphInfoYn(CommonConstant.N);
			busStopGraphOutputVo.setStartStopName(startBusStopVo.getStopName());
			busStopGraphOutputVo.setEndStopName(endBusStopVo.getStopName());
		}


		geojsonFeatureVo.setType(CommonConstant.GEOJSON_TYPE_FEATURE_TYPE);
		geojsonFeatureVo.setGeometry(geoJsonFeatureGeometryVo);
		geojsonFeatureVo.setProperties(busStopGraphOutputVo);

		return geojsonFeatureVo;
	}
}
