package com.larlf.general.ajax;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;

import com.google.gson.Gson;

import com.larlf.general.ajax.Ajax.RunType;
import com.larlf.general.core.Constants;
import com.larlf.general.core.exception.ExceptionWrapper;
import com.larlf.general.utils.StrUtils;

public class AjaxServlet extends HttpServlet
{
	Logger log=Logger.getLogger(this.getClass());

	ServletConfig cfg=null;

	static public String mode;

	static Map<String, String> mapping=new HashMap<String, String>(); // 用于存放Path->Package的映射关系

	GenericKeyedObjectPool pool=null;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException
	{
		// 初始化系统运行的模式
		cfg=servletConfig;

		// 初始化系统的Mode
		{
			mode=cfg.getInitParameter("mode");

			// 检查是否是已知模式
			if(!("develop".equalsIgnoreCase(mode)||"product".equalsIgnoreCase(mode))) log.error("System's mode must is one of 'develop' or 'product' !");
			else Constants.MODE=mode;

			log.info("Format system's mode is ["+Constants.MODE+"] .");
		}

		// 初始化Path->Package的映射关系
		{
			String s1=cfg.getInitParameter("mapping");
			if(s1!=null&&s1.length()>0)
			{
				String[] a1=s1.split(",");
				for(String s2 : a1)
				{
					String[] a2=s2.split("=");
					if(a2.length>1)
					{
						AjaxServlet.mapping.put(a2[0],a2[1]);
						log.debug("Init controlelr mapping : "+a2[0]+" -> "+a2[1]);
					}
				}
			}
		}

		super.init(servletConfig);

		Constants.WEB_ROOT=this.getServletContext().getRealPath("/");
		log.debug("Init constant parameter WEB_ROOT : "+Constants.WEB_ROOT);

		BOPoolFactory factory=new BOPoolFactory();
		this.pool=new GenericKeyedObjectPool(factory);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
	{
		String uri=request.getRequestURI();
		String contextPath=request.getContextPath();
		String className=AjaxServlet.mappingClassPath(uri,contextPath);

		if(uri.endsWith(".ojs"))
		{
			this.doJs(response,uri,className);
		}
		else if(uri.endsWith(".ajax")||uri.endsWith(".do"))
		{
			AjaxWebManager.init(request,response); // 初始化线程池里的Web对象

			try
			{
				this.doAjax(request,response,className);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				AjaxWebManager.remove();
			}
		}
	}

	protected void doJs(HttpServletResponse response,String uri,String className) throws IOException
	{
		StringBuffer sb=new StringBuffer(); // 缓存输出信息

		response.setContentType("text/javascript; charset="+Constants.ENCODING);
		sb.append(this.makeJS(uri,className));

		response.getWriter().print(sb.toString());
	}

	protected void doAjax(HttpServletRequest request,HttpServletResponse response,String className) throws IOException,ServletException
	{
		response.setContentType("text/html; charset="+Constants.ENCODING);
		StringBuffer sb=new StringBuffer();

		// 取得传来的变量信息
		String method=request.getParameter("op");
		String data=request.getParameter("data");
		String[] pTypes=request.getParameter("pTypes")==null?new String[0]:request.getParameter("pTypes").split(",");
		int pCount=request.getParameter("pCount")==null?0:Integer.valueOf(request.getParameter("pCount"));

		Ajax ajax=null; // 要调用方法的相关信息，在下面初始化

		log.debug("=> "+data);

		// 返回值
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("state","OK"); // 默认是执行成功

		try
		{
			Class<?> clazz=ClassUtils.forName(className, this.getClass().getClassLoader());
			Class<?>[] parameterTypes=new Class[pCount];
			Object[] parameters=new Object[pCount];

			Gson gson=new Gson();
			if(pCount>0)
			{
				for(int i=0;i<pCount;i++)
				{
					parameterTypes[i]=ClassUtils.forName(pTypes[i],this.getClass().getClassLoader());
					parameters[i]=gson.fromJson(request.getParameter("p"+i), parameterTypes[i]);
				}
			}

			Method m=clazz.getMethod(method,parameterTypes);
			ajax=m.getAnnotation(Ajax.class);
			RunType runType=ajax.type();

			Object obj;
			Object v;
			switch(runType)
			{
				case NEW:
					obj=clazz.newInstance();
					v=m.invoke(obj,parameters);
					result.put("data",v);
					break;
				case POOL:
					obj=this.pool.borrowObject(clazz);
					v=m.invoke(obj,parameters);
					result.put("data",v);
					this.pool.returnObject(clazz,obj);
					break;
				case STATIC:
					v=m.invoke(clazz,parameters);
					result.put("data",v);
					break;
				default:
					throw new Exception("Error run type : "+runType);
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
			e.printStackTrace();

			result.put("state","Exception");
			result.put("data",(new ExceptionWrapper(e)).getShowMessage());
		}
		
		Gson gson=new Gson();
		String jsonStr=gson.toJson(result);
		log.debug("<= "+jsonStr);
		sb.append(jsonStr);
		response.getWriter().print(sb.toString());
	}

	private String makeJS(String uri,String className)
	{
		String basePath=uri.substring(0,uri.lastIndexOf("/")+1); // 取得URI前面的路径
		try
		{
			Class<?> clazz=Class.forName(className);
			return AjaxUtils.class2js(clazz,basePath);
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			return "alert('ClassNotFoundException : "+e.getMessage()+"');\n";
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
	{
		this.doPost(request,response);
	}

	@Override
	public void destroy()
	{
		super.destroy();
	}

	/*
	 * 把URI转换成对应的Class
	 */
	static public String mappingClassPath(String uri,String contextPath)
	{
		if(uri.indexOf(".")>-1) uri=uri.split("[.]")[0];
		if(contextPath!=null&&contextPath.length()>0) uri=uri.substring(contextPath.length(),uri.length());

		if(uri.startsWith("/")) uri=uri.substring(1);
		String mapping=AjaxServlet.mapping.get(uri.substring(0,uri.indexOf("/")));
		if(mapping==null) return StrUtils.uri2class(uri);
		else
		{
			if(!mapping.endsWith(".")) mapping+=".";
			return mapping+StrUtils.uri2class(uri.substring(uri.indexOf("/")));
		}
	}
}
