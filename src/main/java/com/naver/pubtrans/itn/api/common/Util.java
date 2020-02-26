package com.naver.pubtrans.itn.api.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	 * 빈 문자열인지 확인한다
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}
	
	
	/**
	 * 문자열이 공백으로 되어 있는지 결과 값을 반환한다.
	 *
	 * <pre>
	 * 예 :
	 * isBlank("") = true
	 * isBlank(" ") = true
	 * isBlank("   ") = true
	 * isBlank(" T ") = false
	 * isBlank("T") = false
	 * </pre>
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		int strLen ;
		if(str == null || (strLen = str.length()) == 0){
			return true ;
		}

		for(int i=0; i < strLen; i++){
			if((Character.isWhitespace(str.charAt(i)) == false)){
				return false ;
			}
		}

		return true ;
	}
	
	
	/**
	 * 대상 객체가 Null 인지 확인한다
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		return (obj == null);
	}
	
	/**
	 * 빈 문자열이 아닌지 확인한다
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 대상 객체가 Null이 아닌지 확인한다
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}
	
	
	/**
	 * List가 비어 있는지 확인한다
	 * @param list
	 * @return
	 */
	public static boolean isEmptyList(List<Object> list) {
		
		if(list != null && list.size() > 0) {
			return false ;
		}else {
			return true;
		}
	}
	
	
	/**
	 * 확인하고자 하는 문자열이 비어있는경우 빈값으로 할당한다
	 * @param str
	 * @return
	 */
	public static String nvl(String str) {
		return nvl(str, "");
	}
	
	
	/**
	 * 확인하고자 하는 무자열이 비어있는경우 설정 값을 할당한다
	 * @param target
	 * @param defaultValue
	 * @return
	 */
	public static String nvl(String target, String defaultValue) {
		String rtnStr = "";
		if (isEmpty(target)) {
			rtnStr = defaultValue;
		} else {
			rtnStr = target;
		}
		return rtnStr;
	}
	
	/**
	 * 현재 날짜를 가져온다
	 * @param dateFormat
	 * @return
	 */
	public static String getToday(String dateFormat){

		if("".equals(dateFormat)){
			dateFormat = DATE_FORMAT ;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar cal = Calendar.getInstance();
		return sdf.format(cal.getTime()) ;
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
		
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		return sdf.format(cal.getTime()) ;
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
    	
    	String[] headers_to_try = { "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
    			"HTTP_X_CLUSTER_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "X-FORWARDED-FOR" };
    	
		for (String header : headers_to_try) {
			String ip = req.getHeader(header);
			
			if (ip != null && (ip.length() != 0 && ip.length() >= 20 ) && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		
		return req.getRemoteAddr();
	}
}
