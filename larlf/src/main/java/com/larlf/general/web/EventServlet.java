package com.larlf.general.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.larlf.general.ctrl.EventBus;

/**
 * 用于在容器中启动EventBus的Servlet
 * @author Larlf.Wang
 * @creation 2012-11-6
 */
public class EventServlet extends HttpServlet
{
	Logger log=Logger.getLogger(this.getClass());

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		
		String configFile=config.getInitParameter("configFile");
		if(configFile!=null && configFile.length()>0)
		{
			EventBus.refresh(configFile);
		}
		
		EventBus.start();
		log.info("Start EventBus");
	}

	@Override
	public void destroy()
	{
		super.destroy();
	}

}
