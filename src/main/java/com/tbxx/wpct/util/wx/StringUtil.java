package com.tbxx.wpct.util.wx;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {
	
	/**
	 * 格式化字符串显示；
	 */
	public final static String format(String value) {
		return format(value, "");
	}

	public final static String format(String value, String defaultValue) {
		if (isEmpty(value))
			return defaultValue;
		else
			return value.trim();
	}


	/**
	 * 格式化整型变量显示；
	 */
	public final static String formatInt(int value, int length) {
		String s = String.valueOf(value);
		length = length - s.length();
		for (int i = 0; i < length; i++) {
			s = "0" + s;
		}
		return s;
	}
	/**
	 * 检测某个字符变量是否为空；<br>
	 * 为空的情况，包括：null，空串或只包含可以被 trim() 的字符；
	 */
	public final static boolean isEmpty(String value) {
		if (value == null || value.trim().length() == 0)
			return true;
		else
			return false;
	}
	/**
	 * 检测变量的值是否为一个整型数据；
	 */
	public final static boolean isInt(String value) {
		if (isEmpty(value))
			return false;

		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}
	/**
	 * 判断变量的值是否为double类型
	 */
	public final static boolean isDouble(String value) {
		if (isEmpty(value))
			return false;
		try {
			Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 解析一个字符串为整数；
	 */
	public final static int parseInt(String value) {
		if (isInt(value))
			return Integer.parseInt(value);
		return 0;
	}

	public final static int parseInt(String value, int defaultValue) {
		if (isInt(value))
			return Integer.parseInt(value);
		return defaultValue;
	}

	/**
	 * 解析一个字符串为double
	 */
	public final static double parseDouble(String value) {
		if (isDouble(value))
			return Double.parseDouble(value);
		return 0;
	}

	public final static double parseDouble(String value, double defaultValue) {
		if (isDouble(value))
			return Double.parseDouble(value);
		return defaultValue;
	}

	/**
	 * 解析日期；
	 */
	public final static Date parseDate(long milliSeconds) {
		return new Date(milliSeconds);
	}

	public final static Date parseDate(String date) {
		return parseDate("yyyy-MM-dd", date);
	}

	public final static Date parseDate(String date, Date defaultValue) {
		if (isEmpty(date))
			return defaultValue;

		return parseDate("yyyy-MM-dd", date);
	}

	public final static Date parseDate(String style, String date) {
		if (isEmpty(style) || isEmpty(date))
			return new Date();

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(style);
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * 格式化日期时间字符串显示；
	 */
	public final static String formatDate(Date date) {
		return formatDateTime("yyyy-MM-dd", date, null);
	}
	
	public final static String formatDate(long milliSeconds) {
		return formatDateTime("yyyy-MM-dd", parseDate(milliSeconds), null);
	}
	
	public final static String formatDate(long seconds,String style) {
		if(isEmpty(style)) {
			style = "yyyy-MM-dd";
		}
		return formatDateTime(style, parseDate(seconds*1000), null);
	}

	public final static String formatDate(Date date, String defaultValue) {
		return formatDateTime("yyyy-MM-dd", date, defaultValue);
	}

	public final static String formatTime(Date date) {
		return formatDateTime("HH:mm:ss", date, null);
	}

	public final static String formatTime(Date date, String defaultValue) {
		return formatDateTime("HH:mm:ss", date, defaultValue);
	}

	public final static String formatDateTime(Date date) {
		return formatDateTime("yyyy-MM-dd HH:mm:ss", date, null);
	}

	public final static String formatDateTime(String style, Date date) {
		return formatDateTime(style, date, null);
	}

	public final static String formatDateTime(String style, Date date,
			String defaultValue) {
		if (isEmpty(style) || date==null)
			return defaultValue;

		SimpleDateFormat sdf = new SimpleDateFormat(style);
		return StringUtil.format(sdf.format(date), defaultValue);
	}

	public final static String format(Date date) {
		return formatDateTime("yyyy-MM-dd HH:mm:ss", date, null);
	}

	public final static String format(Date date, String defaultValue) {
		return formatDateTime("yyyy-MM-dd HH:mm:ss", date, defaultValue);
	}

	public final static String formatDateStyle() {
		Calendar day = Calendar.getInstance();
		// return
		// (day.get(Calendar.YEAR)+day.get(Calendar.MONTH)+day.get(Calendar.DATE)+"").trim();
		String year = String.valueOf(day.get(Calendar.YEAR));
		String month = String.valueOf(day.get(Calendar.MONTH) + 1);
		String date = String.valueOf(day.get(Calendar.DATE));
		return year + month + date;
	}

	/**
	 * 将一个 Date 类型的数据，转换为 Timestamp 类型的数据；
	 */
	public final static Timestamp Timestamp() {
		return Timestamp(new Date());
	}

	public final static Timestamp Timestamp(Date date) {
		if (date==null)
			return null;

		return new Timestamp(date.getTime());
	}

	/**
	 * 将一个 Timestamp 类型的数据，转换为 Date 类型的数据；
	 */
	public final static Date Date() {
		return (new Date());
	}

	public final static Date Date(Timestamp ts) {
		if (ts==null)
			return null;

		return new Date(ts.getTime());
	}
	/**
	 * 按照长度截取字符串，能截取中文字符
	 */
	public static String substrGB(String text, int length) {
		String sRet = "";
		if (isEmpty(text))
			return "";
		if (text.length() <= length)
			sRet = text;
		else
			sRet = text.substring(0, length) + "...";
		return sRet;
	}

	/**
	 * 编码 URL 字符串；
	 */
	public final static String encodeUrl(String url) {
		return encodeUrl(url, "UTF-8");
	}

	public final static String encodeUrl(String url, String charset) {
		if (isEmpty(url))
			return "";

		try {
			url = URLEncoder.encode(url, charset);
		} catch (Exception e) {
		}

		return url;
	}

	/**
	 * 解码 URL 字符串；
	 */
	public final static String decodeUrl(String url) {
		return decodeUrl(url, "UTF-8");
	}

	public final static String decodeUrl(String url, String charset) {
		if (isEmpty(url))
			return "";

		try {
			url = URLDecoder.decode(url, charset);
		} catch (Exception e) {
		}

		return url;
	}

	/**
	 * 判断是否为闰年
	 */
	public static boolean isLeapYear(int year) {
		boolean flag = false;
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			flag = true;
		}
		return flag;
	}

	public static boolean isMobilePhone(String phone) {

		if (StringUtil.isEmpty(phone)) {
			return false;
		}
		return phone.matches("^(13|15|18)\\d{9}$");
	}

	public static Calendar getCalendarOfTime(long milliseconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(milliseconds);
		return cal;
	}

	public static int getMonthOfTime(long milliseconds) {
		Calendar cal = getCalendarOfTime(milliseconds);
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		int dom = cal.get(Calendar.DAY_OF_MONTH);
		int doy = cal.get(Calendar.DAY_OF_YEAR);
		return month;
	}

	public static int getDayOfTime(long milliseconds) {
		Calendar cal = getCalendarOfTime(milliseconds);
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		int dom = cal.get(Calendar.DAY_OF_MONTH);
		int doy = cal.get(Calendar.DAY_OF_YEAR);
		return day;
	}

	public static int[] getDateIntOfTime(long milliseconds) {
		Calendar cal = getCalendarOfTime(milliseconds);
		int[] arr = new int[3];
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		int dom = cal.get(Calendar.DAY_OF_MONTH);
		int doy = cal.get(Calendar.DAY_OF_YEAR);
		arr[0] = year;
		arr[1] = month;
		arr[2] = day;
		return arr;
	}
	public static Calendar stringToCalendar(String dataString) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(dataString));
		return cal;
	}
	public static String generateTime(long time) {
		int totalSeconds = (int) (time / 1000);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
	}
	public static boolean isNumber(String str){
		Pattern pattern = Pattern.compile("[0-9]+"); 
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false; 
		} 
		return true; 
	}
	public static String formatPrice(double price){
		int priceint = (int)price;
		if(price>priceint){
			return String.valueOf(price);
		}else{
			return String.valueOf(priceint);
		}
	}
	/**  
     * 计算两个日期之间相差的天数  
     * @param btime 较大的时间 
     * @param stime  较小的时间 
     * @return 相差天数 
     */    
    public static int daysBetween(long btime,long stime)    
    {    
    	Calendar cal1 = stringToCalendar(formatDate(btime));
    	Calendar cal2 = stringToCalendar(formatDate(stime));
        long time1 = cal1.getTimeInMillis();                 
        long time2 = cal2.getTimeInMillis();         
        int between_days=(int) ((time1-time2)/(1000*3600*24));  
        return between_days;           
    }    
    /**
     * 秒数转hh:mm:ss
     * @param seconds 秒数
     * @return
     */
    public static String formatVideoTime(int seconds){
    	int hh = 0;
    	int mm = 0;
    	int ss = seconds%60;
    	int nn = seconds/60;
    	if(nn>0){
    		if(nn>=60){
    			mm = nn%60;
    			hh = nn/60;
    		}else{
    			mm = nn;
    		}
    	}
    	return (hh<10?"0"+hh:""+hh)+":"+(mm<10?"0"+mm:""+mm)+":"+(ss<10?"0"+ss:""+ss);
    }
    public static long stringToTime(String timeStr) {
    	Date date = parseDate("yyyy-MM-dd HH:mm:ss", timeStr);
    	return date.getTime()/1000;
	}
    public static long stringToTime(String timeStr,String style) {
    	if(isEmpty(style)) {
    		style = "yyyy-MM-dd HH:mm:ss";
    	}
    	Date date = parseDate(style, timeStr);
    	return date.getTime()/1000;
	}
    public static long getDayTime(long seconds) {
    	String ymd = formatDate(seconds * 1000);
    	return stringToTime(ymd,"yyyy-MM-dd");
	}
    /**
     * 字符串转base64
     * @param str
     * @return
     */
    public static String base64Encode(String str) {
    	if(!isEmpty(str)) {
    		try {
				return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
    	}
        return "";
    }

    /**
     * base64字符解码
     * @param str
     * @return
     */
    public static String base64Decode(String str) {
    	if(!isEmpty(str)) {
    		return new String(Base64.getDecoder().decode(str));
    	}
        return "";
    }
    /**
     * 将新的用户ID与旧的ID字串合并
     * @param oldUids
     * @param newUid
     * @param maxSize
     * @return
     */
    public static String mergeUids(String oldUids,int newUid,int maxSize) {
    	String receiveUids = String.valueOf(newUid);
		if(!StringUtil.isEmpty(oldUids)) {
			List<String> uidList = new ArrayList<String>();
			uidList.add(receiveUids);
			int len = 1;
			String[] uidArrStrings = oldUids.split(",");
			for (String uidStr : uidArrStrings) {
				boolean exists = false;
				for (String uidStrL : uidList) {
					if(uidStr.equals(uidStrL)) {
						exists = true;
						break;
					}
				}
				if(!exists) {
					len++;
					uidList.add(uidStr);
					if(len >= maxSize) {
						break;
					}
				}
			}
			receiveUids = String.join(",", uidList);
		}
		return receiveUids;
    }
  
    
    public static Object[] remArr(Object[] objs,int num) {
    	List<Object> list=new ArrayList<Object>();
    	for(int i=0;i<objs.length;i++) {
    		list.add(objs[i]);
    	}
    	list.remove(num);
    	return list.toArray();
    }
    
    public static String arrToString(Object[] objs) {
    	StringBuffer sb=new StringBuffer();
    	for(int i=0;i<objs.length;i++) {
    		if(i==(objs.length)-1) {
    			sb.append(objs[i]);
    		}else {
    			sb.append(objs[i]+",");
    		}
    	}
    	return sb.toString();
    }
}
