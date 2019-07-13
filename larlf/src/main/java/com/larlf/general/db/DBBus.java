package com.larlf.general.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

/**
 * 数据库连接的管理类
 *
 * @author Larlf.Wang
 * @creation 2012-11-3
 */
public class DBBus
{
	static public String DEFAULT_DB = "DB";        // 默认的数据连接名称

	static private Logger log = Logger.getLogger(DBBus.class);
	static protected Map<String, DBConfig> dbs = new ConcurrentHashMap<String, DBConfig>();        // 所有DB的配置信息
	static protected Map<Thread, Map<String, Connection>> threads = new ConcurrentHashMap<Thread, Map<String, Connection>>();

	/**
	 * 添加数据库配置
	 *
	 * @param name
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 */
	static public void addDBCfg(String name, String driver, String url, String username, String password)
	{
		DBConfig cfg = new DBConfig();
		cfg.setDriver(driver);
		cfg.setUrl(url);
		cfg.setUsername(username);
		cfg.setPassword(password);
		addDBCfg(name, cfg);
	}

	/**
	 * 添加数据库配置
	 *
	 * @param cfg
	 */
	synchronized static public void addDBCfg(String name, DBConfig cfg)
	{
		if (dbs.containsKey(name))
			log.info("Refresh DB Config : " + cfg);
		else
			log.info("Add DB Config : " + cfg);

		dbs.put(name, cfg);
	}

	/**
	 * 查找数据库定义
	 *
	 * @param name
	 * @return
	 */
	static public DBConfig findDBCfg(String name)
	{
		if (dbs.containsKey(name))
			return dbs.get(name);
		return null;
	}

	static public Connection getConn(DBConfig cfg)
	{
		if(cfg==null)
		{
			log.error("DBConfig is null");
			return null;
		}

		Thread t = Thread.currentThread();

		// 取出线程中所有的连接
		Map<String, Connection> conns = threads.get(t);
		if (conns == null)
		{
			conns = new HashMap<String, Connection>();
			threads.put(t, conns);
		}

		Connection conn = null;

		// 在现有的连接中进行查询
		if (conns.containsKey(cfg.getID()))
		{
			conn = conns.get(cfg.getID());

			try
			{
				if (conn.isClosed())
				{
					DbUtils.closeQuietly(conn);
					conn = null;
				}
			}
			catch (SQLException e)
			{
				log.error(e);
				conn = null;
			}
		}

		// 生成新的数据连接
		if (conn == null)
		{
			DbUtils.loadDriver(cfg.getDriver());
			try
			{
				conn = DriverManager.getConnection(cfg.getUrl(), cfg.getUsername(), cfg.getPassword());
				cfg.initConn(conn);
				conns.put(cfg.getID(), conn);
				log.debug("Open DB Conn : " + conn);
			}
			catch (SQLException e)
			{
				log.error("Create connection error : "+cfg, e);
			}
		}

		return conn;
	}

	/**
	 * 从线程池中取得数据库连接
	 *
	 * @param name
	 * @return
	 */
	static public Connection getConn(String name)
	{

		if (dbs.containsKey(name))
		{
			DBConfig cfg = dbs.get(name);
			if (cfg != null)
			{
				return getConn(cfg);
			}
		}
		else
		{
			log.error("Cannot find DB config : " + name);
		}

		return null;
	}

	/**
	 * 取默认的数据连接
	 *
	 * @return
	 */
	static public Connection getConn()
	{
		return getConn(DBBus.DEFAULT_DB);
	}

	/**
	 * 关闭线程中的所有连接
	 *
	 * @param t
	 */
	static public void closeConns(Thread t)
	{
		if (t == null)
			t = Thread.currentThread();

		if (threads.containsKey(t))
		{
			Map<String, Connection> conns = threads.get(t);
			if (conns != null)
			{
				Iterator<Entry<String, Connection>> it = conns.entrySet().iterator();
				while (it.hasNext())
				{
					Map.Entry<String, Connection> entity = (Entry<String, Connection>) it.next();
					Connection conn = entity.getValue();
					if (conn != null)
					{
						DbUtils.closeQuietly(conn);
						log.debug("Close DB Conn : " + conn);
					}
				}

				conns.clear();
			}

			threads.remove(t);
		}

		threads.remove(t);
	}

	/**
	 * 关闭当前线程中的所有连接
	 */
	static public void closeConn()
	{
		closeConns(null);
	}

	/**
	 * 判断线程中是否有打开的连接
	 *
	 * @param t
	 * @param name
	 * @return
	 */
	static public boolean hasConn(Thread t, String name)
	{
		if (t == null)
			t = Thread.currentThread();

		if (threads.containsKey(t))
		{
			Map<String, Connection> conns = threads.get(t);
			if (conns != null)
				return conns.containsKey(name);
		}

		return false;
	}

	/**
	 * 判断当前线程中是否有打开的连接
	 *
	 * @param name
	 * @return
	 */
	static public boolean hasConn(String name)
	{
		return hasConn(null, name);
	}

	public static Map<String, DBConfig> getDbs()
	{
		return dbs;
	}
}
