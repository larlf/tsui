package com.larlf.general.sql;

public class SqlUtils
{
	/**
	 * 生成SQL中字段的格式
	 * @param str
	 * @return
	 */
	static public String column(String str)
	{
		//不处理空字符串，*和函数
		if (str.length() < 1 
				|| str.equals("*") 
				|| str.indexOf("(")>=0)
			return str;
		
		return "`" + str + "`";
	}

	/**
	 * 生成SQL中值的格式
	 * @param value
	 * @return
	 */
	static public String value(Object value)
	{
		if (value != null)
		{
			if (value != null && value.getClass().equals(String.class))
			{
				String str = value.toString();
				str = str.replaceAll("'", "\\'");
				return "'" + str + "'";
			}
			else
				return value + "";
		}
	
		return "''";
	}

}
