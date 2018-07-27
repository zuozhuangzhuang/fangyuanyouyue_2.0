package com.fangyuanyouyue.forum.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * 
 */

@SuppressWarnings("unchecked")
public class DateUtil {
	public static final String DATE_FORMT="yyyy-MM-dd HH:mm:ss";
	private static Calendar calendar = Calendar.getInstance();

	/**
	 * 得到当前的时间，时间格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	
	
	public static Timestamp getTimestamp(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d;
		Timestamp t = null;
		try {

			d = sdf.parse(time);
			long l = d.getTime();
			t = new Timestamp(l);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	
	
	public static long getTimestampLong(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d;
		long l = 0;
		try {

			d = sdf.parse(time);
			l = d.getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return l;
	}
	

	/**
	 * 得到n年后的日期
	 * 
	 * @param year
	 * @return
	 */
	public static String getDateAfter(int year) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(c.YEAR, year);// 属性很多也有月等等，可以操作各种时间日期
		Date temp_date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(temp_date);
	}

	/**
	 * 得到n年后的日期,自定义参数
	 * 
	 * @param year
	 *            format
	 * @return
	 */
	public static String getDateAfter(int year, String format) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(c.YEAR, year);// 属性很多也有月等等，可以操作各种时间日期
		Date temp_date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(temp_date);
	}

	/**
	 * 得到当前的时间，时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * 得到当前的时间,自定义时间格式 y 年 M 月 d 日 H 时 m 分 s 秒
	 * 
	 * @param dateFormat
	 *            输出显示的时间格式
	 * @return
	 */
	public static String getCurrentDate(String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(new Date());
	}

	/**
	 * 日期格式化，默认日期格式yyyyMMdd
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	/**
	 * 日期格式化，自定义输出日期格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatDate(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	/**
	 * 返回当前日期的前一个时间日期，amount为正数 当前时间后的时间 为负数 当前时间前的时间 默认日期格式yyyy-MM-dd
	 * 
	 * @param field
	 *            日历字段 y 年 M 月 d 日 H 时 m 分 s 秒
	 * @param amount
	 *            数量
	 * @return 一个日期
	 */
	public static String getPreDate(String field, int amount, String format) {
		calendar.setTime(new Date());
		if (field != null && !field.equals("")) {
			if (field.equals("y")) {
				calendar.add(calendar.YEAR, amount);
			} else if (field.equals("M")) {
				calendar.add(calendar.MONTH, amount);
			} else if (field.equals("d")) {
				calendar.add(calendar.DAY_OF_MONTH, amount);
			} else if (field.equals("H")) {
				calendar.add(calendar.HOUR, amount);
			}
		} else {
			return null;
		}
		return getFormatDate(calendar.getTime(),format);
	}

	/**
	 * 
	 * @param date
	 * @param field
	 *            y年 M月 d日 H小时
	 * @param amount
	 *            变量
	 * @param format
	 *            格式
	 * @return
	 */
	public static String getPreDate(Date date, String field, int amount,
                                    String format) {
		calendar.setTime(date);
		if (field != null && !field.equals("")) {
			if (field.equals("y")) {
				calendar.add(calendar.YEAR, amount);
			} else if (field.equals("M")) {
				calendar.add(calendar.MONTH, amount);
			} else if (field.equals("d")) {
				calendar.add(calendar.DAY_OF_MONTH, amount);
			} else if (field.equals("H")) {
				calendar.add(calendar.HOUR, amount);
			}
		} else {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(calendar.getTime());
	}

	/**
	 * 某一个日期的前一个日期
	 * 
	 * @param d
	 *            ,某一个日期
	 * @param field
	 *            日历字段 y 年 M 月 d 日 H 时 m 分 s 秒
	 * @param amount
	 *            数量
	 * @return 一个日期
	 */
	public static String getPreDate(Date d, String field, int amount) {
		calendar.setTime(d);
		if (field != null && !field.equals("")) {
			if (field.equals("y")) {
				calendar.add(calendar.YEAR, amount);
			} else if (field.equals("M")) {
				calendar.add(calendar.MONTH, amount);
			} else if (field.equals("d")) {
				calendar.add(calendar.DAY_OF_MONTH, amount);
			} else if (field.equals("H")) {
				calendar.add(calendar.HOUR, amount);
			}
		} else {
			return null;
		}
		return getFormatDate(calendar.getTime());
	}

	/**
	 * 某一个时间的前一个时间
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getPreDate(String date) throws ParseException {
		Date d = new SimpleDateFormat().parse(date);
		String preD = getPreDate(d, "d", 1);
		Date preDate = new SimpleDateFormat().parse(preD);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(preDate);
	}


	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
}