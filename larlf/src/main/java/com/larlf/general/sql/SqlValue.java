package com.larlf.general.sql;

/**
 * SQL中的键值对
 * 
 * @author wangwen
 * 
 */
public class SqlValue
{
	public String column = null;
	public Object value = null;

	public SqlValue(String column, Object value)
	{
		this.column = column;
		this.value = value;
	}

}
