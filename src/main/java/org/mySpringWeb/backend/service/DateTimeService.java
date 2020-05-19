package org.mySpringWeb.backend.service;

import java.util.Date;

/**
 * 时间格式化相关服务封装
 * @author limingyang
 *
 */
public class DateTimeService {
	/**
	 * 获取年月日字符串
	 * @return
	 */
	public String date() {
		Date date = new Date();
		return String.format("%tY-%tm-%td", date,date,date);
		
	}
	
	/**
	 * 获取时分秒字符串
	 * @return
	 */
	public String time() {
		Date date = new Date();
		return String.format("%tH:%tM:%tS", date,date,date);
	}
	
	/**
	 * 获取年月日 时分秒字符串
	 * @return
	 */
	public String dateTime() {
		Date date = new Date();
		return String.format("%tY-%tm-%td %tH:%tM:%tS", date,date,date,date,date,date);
	}
	
	/**
	 * 以字符串形式返回时间戳
	 * @return
	 */
	public String timeStampAsString() {
		return Long.toString(System.currentTimeMillis());
	}
	
	/**
	 * 以long形式返回时间戳
	 * @return
	 */
	public long timeStampAsLong() {
		return System.currentTimeMillis();
	}

}
