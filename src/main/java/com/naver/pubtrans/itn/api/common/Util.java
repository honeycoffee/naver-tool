package com.naver.pubtrans.itn.api.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.naver.pubtrans.itn.api.consts.CommonConstant;

/**
 * 공통 유틸
 * @author adtec10
 *
 */
public class Util {

	private final static String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 현재 날짜를 가져온다
	 * @param dateFormat
	 * @return
	 */
	public static String getToday(String dateFormat) {

		if ("".equals(dateFormat)) {
			dateFormat = DATE_FORMAT;
		}

		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat));
	}

	/**
	 * 클라이언트 IP를 가져온다
	 * @param req
	 * @return
	 */
	public static String getClientIpAddress(HttpServletRequest req) {
		return req.getRemoteAddr();
	}

	/**
	 * LocalDateTime 형을 Date 형으로 변경한다
	 * @param localDateTime
	 * @return
	 */
	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
		return Date
			.from(localDateTime.atZone(ZoneId.systemDefault())
				.toInstant());
	}

	/**
	 * 카멜케이스 문자열을 스네이크 케이스형태로 변경한다.
	 * <pre>
	 * memberId -> member_id
	 * </pre>
	 * @param str - 변경할 문자열
	 * @return
	 */
	public static String camelCaseToSnakeCase(String str) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1_$2";

		return str.replaceAll(regex, replacement).toLowerCase();
	}

	/**
	 * 스네이크케이스 문자열을 카멜케이스 형태로 변경한다.
	 * <pre>
	 * member_id -> memberId
	 * </pre>
	 * @param str - 변경할 문자열
	 * @return
	 */
	public static String snakeCaseToCamelCase(String str) {

		if (str.indexOf(CommonConstant.UNDERSCORE) > -1) {
			return JdbcUtils.convertUnderscoreNameToPropertyName(str);
		}

		return str;
	}

	/**
	 * 특정 키를 기준으로 중복제거를 수행한다
	 * @param keyExtractor
	 * @return
	 */
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * 두 지점간의 거리를 계산한다
	 * @param sLongitude - 출발지 경도
	 * @param sLatitude - 출발지 위도
	 * @param eLongitude - 도착지 경도
	 * @param eLatitude - 도착지 위도
	 * @return meter
	 */
	public static double calculateDistance(double sLongitude, double sLatitude, double eLongitude, double eLatitude) {

        double katechSx;
        double katechSy;
        double katechEx;
        double katechEy;

        CoordinateTrans cvt = new CoordinateTrans();
        cvt.SetGeoEllipsSystem(CoordinateTrans.Ellipse.WGS84, CoordinateTrans.CoordSystem.GEOGRAPHIC, CoordinateTrans.Ellipse.BESSEL, CoordinateTrans.CoordSystem.KATECH);


        cvt.ConvCoord(sLongitude, sLatitude);

        katechSx = cvt.GetConvX();
        katechSy = cvt.GetConvY();

        cvt.ConvCoord(eLongitude, eLatitude);
        katechEx = cvt.GetConvX();
        katechEy = cvt.GetConvY();

        double dist = Math.sqrt(Math.pow((katechSx-katechEx)/CommonConstant.DECIMAL_ONE_HUNDRED, CommonConstant.EXPONENT_TWO)
        	+ Math.pow((katechSy-katechEy)/CommonConstant.DECIMAL_ONE_HUNDRED, CommonConstant.EXPONENT_TWO)) * CommonConstant.NUMBER_ONE_HUNDRED;

        return dist;
    }

	/**
	 * HttpServletRequest를 가져온다.
	 * @return
	 */
	public static HttpServletRequest getHttpServletRequest() {
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes)RequestContextHolder
			.currentRequestAttributes();

		return servletRequestAttribute.getRequest();
	}

}
