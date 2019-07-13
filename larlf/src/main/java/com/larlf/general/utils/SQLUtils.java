package com.larlf.general.utils;

public class SQLUtils
{
	public static boolean isNull(Object value)
	{
		if(value==null||"".equals(value+"")) return true;
		return false;
	}
	
	public static boolean notNull(Object value)
	{
		return !isNull(value);
	}
	
	public static boolean isNumber(Object value)
	{
		try
		{
			Float.valueOf(value.toString());
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	public static boolean notNumber(Object value)
	{
		return !isNumber(value);
	}
}
