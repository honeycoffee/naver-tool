package com.naver.pubtrans.itn.api.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;


/**
 * 공통 유틸
 * @author adtec10
 *
 */
public class Util {

	private final static String DATE_FORMAT = "yyyy-MM-dd" ;
	private final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss" ;



	/**
	 * 현재 날짜를 가져온다
	 * @param dateFormat
	 * @return
	 */
	public static String getToday(String dateFormat){

		if("".equals(dateFormat)){
			dateFormat = DATE_FORMAT ;
		}

		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat)) ;
	}


	/**
	 * Timestamp 형을 문자열로 반환한다
	 * @param ts
	 * @return
	 */
	public static String timestampToString(Timestamp ts) {
		return timestampToString(ts, DATE_TIME_FORMAT) ;
	}

	/**
	 * Timestamp 형을 문자열로 반환한다
	 * @param ts
	 * @param fmt
	 * @return
	 */
	public static String timestampToString(Timestamp ts, String fmt) {
		Date date = new Date(ts.getTime()) ;
		return dateToString(date, fmt) ;
	}

	/**
	 * Date 형을 문자열로 반환한다
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return dateToString(date, DATE_TIME_FORMAT) ;
	}

	/**
	 * Date 형을 문자열로 반환한다
	 * @param date
	 * @param fmt
	 * @return
	 */
	public static String dateToString(Date date, String fmt) {

		LocalDateTime localDateTime = dateToLocalDateTime(date) ;

		return localDateTime.format(DateTimeFormatter.ofPattern(fmt)) ;
	}

	/**
	 * 특정포맷의 날짜형식 문자열을 기본 Date 문자열로 반환한다
	 * @param date
	 * @param fmt - 문자열 포맷
	 * @return
	 */
	public static String stringTodateFormat(String date, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dateToString(d, DATE_TIME_FORMAT) ;
	}

	/**
	 * 특정포맷을 갖는 날짜형식의 문자열을 원하틑 Date 문자열로 반환한다
	 * @param date
	 * @param fmt - 문자열 포맷
	 * @param cvtFmt - 변경할 문자열 포뱃
	 * @return
	 */
	public static String stringTodateFormat(String date, String fmt, String cvtFmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dateToString(d, cvtFmt) ;
	}


	/**
     * 클라이언트 IP를 가져온다
     * @param req
     * @return
     */
    public static String getClientIpAddress(HttpServletRequest req) {

    	String ip = req.getHeader("X-Forwarded-For");

    	 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	     ip = req.getHeader("Proxy-Client-IP");
    	 }
    	 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	     ip = req.getHeader("WL-Proxy-Client-IP");
    	 }
    	 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	     ip = req.getHeader("HTTP_CLIENT_IP");
    	 }
    	 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	     ip = req.getHeader("HTTP_X_FORWARDED_FOR");
    	 }
    	 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	     ip = req.getRemoteAddr();
    	 }

		return ip;
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
     * Date형을 LocalDateTime형으로 변경한다
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
    	return Instant
    			.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault())
    			.toLocalDateTime();
    }
}
