package com.larlf.general.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.larlf.general.db.DBBus;

public class DBFilter implements Filter
{
	public void init(FilterConfig config) throws ServletException
	{
		//检查是否有默认的DB配置
		String url = config.getInitParameter("url");
		if (url != null && url.length() > 0)
		{
			String username = config.getInitParameter("username");
			String password = config.getInitParameter("password");
			String driver = config.getInitParameter("driver");
			DBBus.addDBCfg(DBBus.DEFAULT_DB, driver, url, username, password);
		}
	}

	@Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		try
		{
			if (((HttpServletRequest) request).getRequestURI().endsWith("/DBFilter"))
			{
				//用于验证是否开启了DBFilter
				response.getWriter().print("{\"state\":true}");
				response.flushBuffer();
			}
			else
				chain.doFilter(request, response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DBBus.closeConn();
		}
	}

	@Override public void destroy()
	{
	}
}
