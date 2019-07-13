package com.larlf.general.db;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * MySQL的数据库配置
 */
public class MySQLDBConfig extends DBConfig
{
	static protected Logger log = Logger.getLogger(MySQLDBConfig.class);

	private String host = "127.0.0.1";
	private int port = 3306;
	private String db = "test";
	private boolean useUnicode = true;
	private String characterEncoding = "UTF8";

	public MySQLDBConfig()
	{

	}

	public MySQLDBConfig(String host, int port, String db, String username, String password)
	{
		this.driver="com.mysql.jdbc.Driver";
		this.host = host;
		this.port = port;
		this.db = db;
		this.setUsername(username);
		this.setPassword(password);
	}

	@Override public void initConn(Connection conn)
	{
		try
		{
			conn.nativeSQL("set names " + this.characterEncoding + ";");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 生成连接地址
	 *
	 * @return jdbc:mysql://localhost:3306/db?useUnicode=true&characterEncoding=UTF-8
	 */
	@Override
	public String getUrl()
	{
		if (!StringUtils.isEmpty(this.url))
			return this.url;

		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:mysql://");
		sb.append(this.host).append(":").append(this.port);
		sb.append("/").append(this.db).append("?");
		sb.append("useUnicode=").append(this.useUnicode).append("&");
		sb.append("characterEncoding=").append(this.characterEncoding);
		log.debug(sb);

		return sb.toString();
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getDb()
	{
		return db;
	}

	public void setDb(String db)
	{
		this.db = db;
	}

	public boolean isUseUnicode()
	{
		return useUnicode;
	}

	public void setUseUnicode(boolean useUnicode)
	{
		this.useUnicode = useUnicode;
	}

	public String getCharacterEncoding()
	{
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding)
	{
		this.characterEncoding = characterEncoding;
	}
}
