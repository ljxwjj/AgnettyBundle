/*
 * ========================================================
 * Copyright(c) 2012 杭州龙骞科技-版权所有
 * ========================================================
 * 本软件由杭州龙骞科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.hzdracom.com/
 * 
 * ========================================================
 */

package com.android.agnetty.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.format.DateFormat;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public class TimeUtil {
	private int mYear;
	private int mMonth;
	private int mDay;
	 
	private boolean mLeap;
	private Calendar mCalendar;
	 
	final static String CHINESE_NUMBER[] = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
	final static SimpleDateFormat mChineseDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
	 
	final static long[] mLunarInfo = new long[]{
		0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,	 
		0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 
		0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,	 
		0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,	 
		0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,	 
		0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,	 
		0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,	 
		0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6, 
		0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, 
		0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 
		0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, 
		0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 
		0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
		0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
		0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0};
	 
	public TimeUtil(Calendar cal) {	
		mCalendar = cal;
		int yearCyl = 0;
		int monCyl = 0;
		int dayCyl = 0;
		int leapMonth = 0; 
		Date baseDate = null;
		 
		try {
			baseDate = mChineseDateFormat.parse("1900年1月31日");
		} catch (ParseException ex) {
			ex.printStackTrace(); 
		}
		 
		//求出和1900年1月31日相差的天数		 
		int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);		 
		dayCyl = offset + 40;		 
		monCyl = 14;
		 
		//用offset减去每农历年的天数		 
		// 计算当天是农历第几天		 
		//i最终结果是农历的年份		 
		//offset是当年的第几天		 
		int iYear, daysOfYear = 0;		 
		for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {		 
			daysOfYear = yearDays(iYear);		 
			offset -= daysOfYear;		 
			monCyl += 12;		 
		}
		 
		if (offset < 0) {		 
			offset += daysOfYear;		 
			iYear--;		 
			monCyl -= 12;		 
		}
		 
		//农历年份		 
		mYear = iYear;		 		
		yearCyl = iYear - 1864;		 
		leapMonth = leapMonth(iYear); //闰哪个月,1-12		 
		mLeap = false;
		 
		//用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天		 
		int iMonth, daysOfMonth = 0;
		 
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {		 
			//闰月		 
			if (leapMonth > 0 && iMonth == (leapMonth + 1) && !mLeap) {		 
				--iMonth;		 
				mLeap = true;		 
				daysOfMonth = leapDays(mYear);		 
			} else
				daysOfMonth = monthDays(mYear, iMonth);
			
			offset -= daysOfMonth;		 
			//解除闰月		 
			if (mLeap && iMonth == (leapMonth + 1)) mLeap = false;		 
			if (!mLeap) monCyl++;		 
		}
		 
		//offset为0时，并且刚才计算的月份是闰月，要校正		 
		if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {		 
			if (mLeap) {		 
				mLeap = false;		 
			} else {		 
				mLeap = true;		 
				--iMonth;		 
				--monCyl;		 
			}
		}
		 
		//offset小于0时，也要校正
		if (offset < 0) {
			offset += daysOfMonth;
			--iMonth;
			--monCyl;
		}
		 
		mMonth = iMonth;
		mDay = offset + 1;
	}
	
	
	//====== 传回农历 year年的总天数
	final private static int yearDays(int year) {
		int i, sum = 348;
	 
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((mLunarInfo[year - 1900] & i) != 0) sum += 1;
		}
	 
		return (sum + leapDays(year));
	}
	 
	
	//====== 传回农历 year年闰月的天数
	final private static int leapDays(int year) {
		if (leapMonth(year) != 0) {
			if ((mLunarInfo[year - 1900] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
	 
		return 0;
	}
	 
	
	//====== 传回农历 year年闰哪个月 1-12 , 没闰传回 0
	final private static int leapMonth(int year) {
		return (int) (mLunarInfo[year - 1900] & 0xf);
	}
	 
	
	//====== 传回农历 year年month月的总天数
	final private static int monthDays(int year, int month) {
		if ((mLunarInfo[year - 1900] & (0x10000 >> month)) == 0)
			return 29;
		else
			return 30;
	}
	 
	
	
	//====== 传入 月日的offset 传回干支, 0=甲子
	final private static String cyclicalm(int num) {
		final String[] Gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
		final String[] Zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
		return (Gan[num % 10] + Zhi[num % 12]);
	}
	
	
	/**
	 * 获取农历 年的生肖
	 * @return
	 */
	final public String animalsYear() {
		final String[] Animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
		return Animals[(mYear - 4) % 12];
	}	
		
	/**
	 * 获取干支
	 * @return
	 */
	final public String cyclical() {
		int num = mYear - 1900 + 36;
		return (cyclicalm(num));
	}
	
	
	/**
	 * 获取中国星期
	 * @param calendar
	 * @return
	 */
	public String getChineseWeek() {
		final String weeks[] = {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int week = mCalendar.get(Calendar.DAY_OF_WEEK);
        return weeks[week-1];
	}
	
	
	/**
	 * 获取中国日期
	 * @param day
	 * @return
	 */
	public String getChineseDay() {
		String[] CHINESE_TEN = {"初", "十", "廿", "三"};
		
		if (mDay > 30) {
			return "";		 
		} else if (mDay == 10){	 
			return "初十";		 
		}else{		
			int n = mDay % 10 == 0 ? 9 : mDay % 10 - 1;	 
			return CHINESE_TEN[mDay/10] + CHINESE_NUMBER[n];
		}
	}
	
	/**
	 * 格式化时间
	 * @param calendar
	 * @param format
	 * @return
	 */
	public String formatDate(String format) {
		return DateFormat.format(format, mCalendar.getTimeInMillis()).toString();
	}
	 
	/**
	 * 
	 */
	public String toString() {
		return new StringBuilder()
			.append(mYear)
			.append("年")
			.append(mLeap ? "闰" : "")
			.append(CHINESE_NUMBER[mMonth - 1])
			.append("月")
			.append(getChineseDay()).toString();
	}
 
}
