package com.larlf.general.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.larlf.general.core.exception.MessageException;

/**
 * 以线程池的方式对J2EE的对象进行管理
 * 
 * @author larlf
 * 
 */
public class AjaxWebManager
{
	private static final ThreadLocal<HttpServletRequest> requests=new ThreadLocal<HttpServletRequest>();

	private static final ThreadLocal<HttpServletResponse> responses=new ThreadLocal<HttpServletResponse>();

	static public void init(HttpServletRequest request,HttpServletResponse response)
	{
		requests.set(request);
		responses.set(response);
	}

	static public void remove()
	{
		requests.remove();
		responses.remove();
	}

	static public HttpServletRequest request()
	{
		return requests.get();
	}

	static public HttpServletResponse response()
	{
		return responses.get();
	}

	static public HttpSession session()
	{
		HttpServletRequest request=requests.get();
		if(request!=null) return request.getSession();
		return null;
	}

	static public void populate(Object obj) throws MessageException
	{
		try
		{
			if(obj!=null) BeanUtils.populate(obj,AjaxWebManager.request().getParameterMap());
		}
		catch(Exception e)
		{
			throw new MessageException(e,"Can't populate data to "+obj.getClass()+"!");
		}
	}

	@SuppressWarnings("unchecked")
	static public <T> T populate(Class T) throws MessageException
	{
		T t=null;
		try
		{
			t=(T)T.newInstance();
		}
		catch(Exception e)
		{
			throw new MessageException(e,"Can't create new instance for : "+T);
		}

		populate(t);
		return t;
	}
}
