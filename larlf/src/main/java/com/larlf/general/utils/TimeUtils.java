package com.larlf.general.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 时间处理相关的公用方法
 * @author Larlf.Wang
 * @creation 2012-11-5
 */
public class TimeUtils
{
	private static Logger log=Logger.getLogger(TimeUtils.class);
	private static SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 取得秒级的时间戳
	 * @return
	 */
	static public long timeStamp()
	{
		return (new Date()).getTime()/1000;
	}
	
	/**
	 * 取得毫秒级的时间戳
	 * @return
	 */
	static public long msTimeStamp()
	{
		return (new Date()).getTime();
	}
	
	/**
	 * 时间转字符串
	 * @param time
	 * @return
	 */
	static public String timeToStr(long time)
	{
		return fmtDate.format(new Date(time*1000));
	}
	
	/**
	 * 字符串转时间
	 * @param str 日期字符串，支持"yyyy-MM-dd HH:mm:ss""yyyy-MM-dd"
	 * @return
	 */
	static public long strToTime(String str)
	{
		if(StringUtils.isEmpty(str)) return 0;
		if(str.length()==10) str+=" 08:00:00";
		
		try
		{
			Date time=fmtDate.parse(str);
			return time.getTime()/1000;
		}
		catch (ParseException e)
		{
			log.error("Bad date format : "+str, e);
		}
		
		return 0;
	}
	
	/**
	 * 时间转日期字符串
	 * @param time
	 * @return
	 */
	public static String timeToDateStr(long time)
	{
		Date date=new Date(time*1000);
		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");
		return fmtDate.format(date);
	}
	
	/**
	 * 取得表示日期的时间
	 * @param time 秒级时间戳
	 * @return 表示日期的秒级时间戳
	 */
	public static long dateTime(long time)
	{
		return time/24/60/60*24*60*60;
	}
	
	/**
	 * 生成两个日期之间的所有日期
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> makeDateList(String startDate, String endDate)
	{
		List<String> list=new ArrayList<String>();
		
		if(StringUtils.isEmpty(startDate))
			startDate=endDate;
		if(StringUtils.isEmpty(endDate))
			endDate=startDate;
		
		if(!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate))
		{	
			long startTime=strToTime(startDate);
			long endTime=strToTime(endDate);
			
			//检查开始和结束时间是否正确
			if(startTime>endTime)
			{
				long temp=startTime;
				startTime=endTime;
				endTime=temp;
			}
			
			while(startTime<=endTime)
			{
				list.add(timeToDateStr(startTime));
				startTime+=24*60*60;
			}
		}
		
		return list;
	}
}
