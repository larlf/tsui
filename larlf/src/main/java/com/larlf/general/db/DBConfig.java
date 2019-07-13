package com.larlf.general.db;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;

/**
 * 数据库相关的配置信息
 *
 * @author Larlf.Wang
 * @creation 2012-11-3
 */
public class DBConfig
{
	protected String driver;              // 数据库驱动
	protected String url;                 // 数据库url
	protected String username;            // 数据库用户名
	protected String password;            // 数据库密码

	public DBConfig()
	{
	}

	public DBConfig(String name, String driver, String url, String username, String password)
	{
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	/**
	 * 连接初始化进行的操作
	 * @param conn
	 */
	public void initConn(Connection conn)
	{
	}

	public String getID()
	{
		//如果没有配置名称，就用类的HashCode
		return this.getClass().getName() + "@" + this.hashCode();
	}

	public String getDriver()
	{
		return driver;
	}

	public void setDriver(String driver)
	{
		this.driver = driver;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("[").append(this.getID()).append("]").append(this.url);
		return str.toString();
	}
}
