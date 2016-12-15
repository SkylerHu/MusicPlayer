package com.anjoyo.musicplayer.util;

import java.text.DecimalFormat;

public class TimeUtil {

	/** 
	 * 将int型时间转换成格式化时间字符串
	 * @param time
	 * @return
	 */
	public static String getTime(int time) {
		int minute = time / 1000 / 60;
		int second = time / 1000 % 60;
		return formateTime(minute) + ":" + formateTime(second);
	}
	
	/**
	 * 格式化数值
	 * @param n 数值
	 * @return
	 */
	private static String formateTime(int n) {
		DecimalFormat format = new DecimalFormat("00");
		return format.format(n);
	}
	
}
