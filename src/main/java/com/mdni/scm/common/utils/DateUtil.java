package com.mdni.scm.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.google.common.collect.Lists;

public final class DateUtil {
	/**
	 * 定义常量
	 **/
	public static final String YYYYMM_PATTERN = "yyyyMM";
	public static final String YYYY_MM_PATTERN = "yyyy-MM";
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String YYMMDDHHMMSS_PATTERN = "yyMMddHHmmss";

	/** 锁对象 */
	private static final Object lockObj = new Object();

	/** SimpleDateFormat非线程安全, 存放不同的日期模板格式的sdf的Map */
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	/**
	 * 使用预设格式提取字符串日期
	 * 
	 * @param dateTime 日期字符串 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date parseToDateTime(String dateTime) {
		return parse(dateTime, DATE_TIME_PATTERN);
	}

	/**
	 * 使用预设格式提取字符串日期
	 * 
	 * @param date 日期字符串 yyyy-MM-dd
	 * @return
	 */
	public static Date parseToDate(String date) {
		return parse(date, DATE_PATTERN);
	}

	/**
	 * 使用用户格式提取字符串日期
	 * 
	 * @param strDate 日期字符串
	 * @param pattern 日期格式
	 * @return
	 */
	public static Date parse(String strDate, String pattern) {
		SimpleDateFormat df = getSdf(pattern);
		try {
			return df.parse(strDate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 相差天数
	 */
	public static int getIntervalDays(Date startDate, Date endDate) {
		long intervalMills = Math.abs(endDate.getTime() - startDate.getTime());
		return (int) Math.round(intervalMills * 1.0 / DateUtils.MILLIS_PER_DAY);
	}

	/**
	 * @param date 格式化成 yyyy-MM-dd
	 */
	public static String formatDate(Date date) {
		if (date == null)
			return null;
		return getSdf(DATE_PATTERN).format(date);
	}

	/**
	 * 格式化成 yyyy-MM
	 */
	public static String formatToYearMonth(Date date) {
		if (date == null)
			return null;
		return getSdf(YYYY_MM_PATTERN).format(date);
	}

	/**
	 * @param date
	 */
	public static String formatDate(Date date, String pattern) {
		return getSdf(pattern).format(date);
	}

	//获得本周 周五的日期 yyyy-MM-dd
	public static String getFridayOfThisWeek(Date valueDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(valueDate);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 4);
		return formatDate(cal.getTime());
	}

	//获得上周五 日期 格式： yyyy-MM-dd
	public static String getLastFridayDate(Date valueDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(valueDate);
		cal.add(Calendar.WEEK_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		return formatDate(cal.getTime());
	}

	//获得日期valueDate是星期几
	public static int getDayOfWeek(Date valueDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(valueDate);
		//星期几
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}

	/**
	 * 例如 参数yearMonth 是2016-01 则返回2015-01
	 * 
	 * @param yearMonth 年月 格式：yyyy-MM
	 * @return 向前推1年的 yyyy-MM
	 */
	public static String reduceOneYear(String yearMonth) {
		if (yearMonth.length() < 6)
			return StringUtils.EMPTY;
		String result = null;
		String yearString = yearMonth.substring(0, 4);
		int year = NumberUtils.toInt(yearString);
		year--;
		String month = yearMonth.substring(yearString.length());
		result = year + month;
		return result;
	}

	//计算两个日期间隔  月的个数 四舍五入
	public static int getMonthSpace(Date startDate, Date endDate) throws ParseException {
		if (endDate.before(startDate))
			return 0;
		long intervalMills = endDate.getTime() - startDate.getTime();
		long oneMonth = DateUtils.MILLIS_PER_DAY * 30;
		return (int) Math.round(intervalMills * 1.0 / oneMonth);
	}

	/**
	 * 获取上月 格式yyyy-MM
	 * 
	 * @return date
	 */
	public static String getLastYearMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, -1);
		return formatDate(cal.getTime(), YYYY_MM_PATTERN);
	}

	/**
	 * 获取上个月的第一天
	 * 
	 * @return
	 */
	public static String getFirstDayOfLastMonth() {
		return getLastYearMonth() + "-01";
	}

	/**
	 * 获取上个月的最后一天
	 * 
	 * @return
	 */
	public static String getLastDayOfLastMonth() {
		Calendar cal = Calendar.getInstance();
		//设置月份
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		return formatDate(cal.getTime());
	}

	/**
	 * 获得给定日期或当前日期 往前negValue或往后negValue年的日期
	 * 
	 * @param date 2013-07-22
	 * @param negValue -1 或+1 是负数是往前 正数是往后 如:-2 则是2011-07-22, +2 则是2015-07-22
	 * @return
	 */
	public static Date nearYearDate(Date date, int negValue) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date == null ? new Date() : date);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + negValue);
		return cal.getTime();
	}

	/**
	 * 两个日期相差的 年的个数
	 * 
	 * @param start
	 * @param end
	 */
	public static long getYearSpace(Date start, Date end) {
		if (end.before(start))
			return 0;
		long interval = end.getTime() - start.getTime();
		return (interval) / (365 * DateUtils.MILLIS_PER_DAY);
	}

	/**
	 * 某个日期上加上或减去n天,如果date为空,为当前日期
	 * 
	 * @param date
	 * @param n 可正可负 如:1 或-3
	 * @return
	 */
	public static Date addNDate(Date date, int n) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date == null ? new Date() : date);
		c1.add(Calendar.DATE, n);
		return c1.getTime();
	}

	/**
	 * 获取指定月份的上一个月
	 * 
	 * @param yearMonth 月份 yyyy-MM
	 * @return 上一个月 yyyy-MM
	 */
	public static String getLastYearMonth(String yearMonth) {
		Date date = parse(yearMonth, YYYY_MM_PATTERN);
		return getLastYearMonth(date);
	}

	/**
	 * 获取指定日期的上一个月
	 * 
	 * @param date 日期
	 * @return 上一个月 yyyy-MM
	 */
	public static String getLastYearMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		return formatToYearMonth(cal.getTime());
	}

	/**
	 * 获得指定日期的 下一个年月
	 * 
	 * @param date
	 * @return 下一个月 yyyy-MM
	 */
	public static String getNextYearMonth(Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		return formatToYearMonth(cal.getTime());
	}

	/**
	 * 获取某个月份 最后一天 例如传入 2014-03 则返回 2014-03-31
	 * 
	 * @param yearMonth 月份 yyyy-MM
	 * @return
	 */
	public static String getLastDayOfMonth(String yearMonth) {
		Date date = parse(yearMonth, YYYY_MM_PATTERN);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDayOfMonth = cal.getTime();
		return formatDate(lastDayOfMonth);
	}

	/**
	 * 判断参数date是否为月末
	 * 
	 * @return
	 */
	public static boolean isLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 判断参数date 日期是否为周五
	 * 
	 * @param date 日期
	 */
	public static boolean isFriday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
	}

	/**
	 * 获取now之前最近的周五
	 * 
	 * @param now
	 * @return
	 */
	public static Date getNearFriday(Date now) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);//获取最近的周五
		Date nearFriday = cal.getTime();
		//如果最近的周五比现在还大  先减去
		if (nearFriday.getTime() > now.getTime()) {
			nearFriday = DateUtil.addNDate(nearFriday, -7);
		}
		return nearFriday;
	}

	/**
	 * 获取两个日期之间的所有的 周五或月末的日期列表 [startDate,endDate]
	 * 
	 * @param startDate included
	 * @param endDate included
	 */
	public static List<Date> getFridayOrLastDayOfMonthListBetween(Date startDate, Date endDate) {

		List<Date> resultDateList = new ArrayList<Date>();

		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		while (!(cal.getTime().after(endDate))) {
			//如果 startDate<=endDate  都返回
			Date date = cal.getTime();
			if (isFriday(date) || isLastDayOfMonth(date)) {
				resultDateList.add(date);
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		return resultDateList;
	}

	/**
	 * 获取指定日期的 年份
	 * 
	 * @param date 日期
	 */
	public static int getYear(Date date) {
		if (date == null)
			return 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获得从本月开始向后延12个月 年月数据 <br/>
	 * 返回[2016-01,2016-02,....2016-12]
	 */
	public static List<String> getTwelveMonthString() {
		List<String> result = Lists.newArrayList();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		for (int i = 0; i < 12; i++) {
			result.add(DateUtil.formatToYearMonth(calendar.getTime()));
			calendar.add(Calendar.MONTH, +1);
		}
		return result;
	}

	/**
	 * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
	 */
	private static SimpleDateFormat getSdf(final String pattern) {
		ThreadLocal<SimpleDateFormat> sdfHolder = sdfMap.get(pattern);
		// 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
		if (sdfHolder == null) {
			synchronized (lockObj) {
				sdfHolder = sdfMap.get(pattern);
				if (sdfHolder == null) {
					// 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
					// 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
					sdfHolder = new ThreadLocal<SimpleDateFormat>() {
						@Override
						protected SimpleDateFormat initialValue() {
							return new SimpleDateFormat(pattern);
						}
					};
					sdfMap.put(pattern, sdfHolder);
				}
			}
		}
		return sdfHolder.get();
	}

	public static void main(String[] args) {
		Date startDate = parseToDate("2016-06-26");
		System.out.println(getFridayOfThisWeek(startDate));

	}
}
