package com.naver.pubtrans.itn.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.naver.pubtrans.itn.api.common.CoordinateTrans;
import com.naver.pubtrans.itn.api.common.Util;
import com.naver.pubtrans.itn.api.consts.CommonConstant;
import com.naver.pubtrans.itn.api.repository.BusStopRepository;
import com.naver.pubtrans.itn.api.vo.bus.graph.BusStopGraphVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureGeometryVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.GeoJsonFeatureVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.BusStopGraphSearchVo;
import com.naver.pubtrans.itn.api.vo.bus.graph.input.GeoJsonFeatureInputVo;
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

	/**
	 * GeoJson feature 정보로부터 구간 거리와 DB에 저장될 geometry 텍스트를 구한다
	 * @param geoJsonFeatureInputVo - 구간 정보
	 * @return
	 * @throws Exception
	 */
	public BusStopGraphVo getDistanceAndLineStringFromGeoJsonFeature(GeoJsonFeatureInputVo geoJsonFeatureInputVo) throws Exception {

		// 거리계산
		GeoJsonFeatureGeometryVo geojsonFeatureGeometryVo = geoJsonFeatureInputVo.getGeometry();
		List<List<Double>> coordinates = geojsonFeatureGeometryVo.getCoordinates();


		StringBuilder coordinateText = new StringBuilder();
		List<Double> distanceList = new ArrayList<>();


		IntStream.range(0, coordinates.size()-1).forEach(i -> {

			double sLongitude = coordinates.get(i).get(0);
			double sLatitude = coordinates.get(i).get(1);
			double eLongitude = coordinates.get(i+1).get(0);
			double eLatitude = coordinates.get(i+1).get(1);

			if(i == 0) {
				coordinateText.append(String.valueOf(sLongitude));
				coordinateText.append(CommonConstant.BLANK);
				coordinateText.append(String.valueOf(sLatitude));
			}

			coordinateText.append(CommonConstant.COMMA);
			coordinateText.append(String.valueOf(eLongitude));
			coordinateText.append(CommonConstant.BLANK);
			coordinateText.append(String.valueOf(eLatitude));

			// 거리
			double distance = Util.calculateDistance(sLongitude, sLatitude, eLongitude, eLatitude);
			distanceList.add(distance);
		});

		// 전체 거리
		double totalDistance = distanceList.stream().mapToDouble(Double::doubleValue).sum();


		String graphInfo = this.makeLineStringFullText(coordinateText);

		BusStopGraphVo busStopGraphVo = new BusStopGraphVo();
		busStopGraphVo.setDistance((int)Math.round(totalDistance));
		busStopGraphVo.setGraphInfo(graphInfo);

		return busStopGraphVo;
	}

	/**
	 * 배열에 담겨 있는 좌표정보를 LINESTRING 문자열로 변환한다
	 * @param coordinates
	 * @return
	 */
	public String convertLineString(List<List<Double>> coordinates) {
		StringBuilder coordinateText = new StringBuilder();

		IntStream.range(0, coordinates.size()-1).forEach(i -> {

			double sLongitude = coordinates.get(i).get(0);
			double sLatitude = coordinates.get(i).get(1);
			double eLongitude = coordinates.get(i+1).get(0);
			double eLatitude = coordinates.get(i+1).get(1);

			if(i == 0) {
				coordinateText.append(String.valueOf(sLongitude));
				coordinateText.append(CommonConstant.BLANK);
				coordinateText.append(String.valueOf(sLatitude));
			}

			coordinateText.append(CommonConstant.COMMA);
			coordinateText.append(String.valueOf(eLongitude));
			coordinateText.append(CommonConstant.BLANK);
			coordinateText.append(String.valueOf(eLatitude));

		});

		String graphInfo = this.makeLineStringFullText(coordinateText) ;

		return graphInfo;
	}

	/**
	 * DB에서 사용될 LINESTRING 전체 문자열을 생성한다
	 * @param coordinateText - 좌표 텍스트 정보
	 * @return
	 */
	private String makeLineStringFullText(StringBuilder coordinateText) {
		return CommonConstant.GEOJSON_GEOMETRY_TYPE_LINE_STRING.toUpperCase() + CommonConstant.BRACKET_START + coordinateText.toString() + CommonConstant.BRACKET_END;
	}


	/**
	 * Mysql linestring 구문으로부터 마지막 좌표를 가져온다
	 * @param graphInfo - 그래프 정보
	 * @return
	 */
	public List<Double> selectLastCoordinates(String graphInfo) {
		graphInfo = graphInfo.replace(CommonConstant.GEOJSON_GEOMETRY_TYPE_LINE_STRING.toUpperCase(), "");
		graphInfo = graphInfo.replace(CommonConstant.BRACKET_START, "");
		graphInfo = graphInfo.replace(CommonConstant.BRACKET_END, "");

		String coordinatesList[] = graphInfo.split(CommonConstant.COMMA);
		String lastCoordinates[] = coordinatesList[coordinatesList.length-1].split(CommonConstant.BLANK);

		List<Double> coordinateList = Arrays.asList(Double.parseDouble(lastCoordinates[0]), Double.parseDouble(lastCoordinates[1]));

		return coordinateList;
	}
}
