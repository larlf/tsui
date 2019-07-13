package com.larlf.general.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * <h1>一些用于处理Web对象的公用方法</h1>
 * 
 * @author Larlf.Wang
 */
public class WebUtils
{
	static Logger log=Logger.getLogger(WebUtils.class);

	/**
	 * 拼出当前URL的根路径，如：http://localhost:8080/app1
	 * 
	 * @param url
	 *            当前Request完整的URL
	 * @param uri
	 *            Request的URI
	 * @param context
	 *            应用的发布路径
	 * @return Host的字符串
	 */
	public static String spellURLRoot(String url,String uri,String context)
	{
		if(url!=null)
		{
			if(uri!=null) url=url.substring(0,url.length()-uri.length());
			if(context!=null) url+=context;
		}
		return url;
	}

	/**
	 * 根据一个参考路径把一相对路径换成绝对路径
	 * 
	 * @param referencePath
	 * @param relativePath
	 * @return
	 */
	public static String relativePath2AbsolutePath(String referencePath,String relativePath)
	{
		if(relativePath!=null)
		{
			if(relativePath.startsWith("/")) return relativePath;
			if(referencePath!=null&&(referencePath.indexOf("/")>-1)) return referencePath.substring(0,referencePath.lastIndexOf("/"))+"/"+relativePath;
			else return "/"+relativePath;
		}
		else return "";
	}

	/**
	 * 从一个URL读取数据
	 * 
	 * @param url
	 *            URL字符串
	 * @return 从URL返回的内容
	 * @throws IOException
	 */
	static public StringBuffer readFromURL(String url) throws IOException
	{
		StringBuffer sbuf=new StringBuffer(); // 用于合并取得的数据
		BufferedReader in=null;

		try
		{

			// 把value的值做为一个URL打开相应的链接
			URL urlObject=new URL(url);
			URLConnection urlConn;

			// 打开联接
			urlConn=urlObject.openConnection();

			urlConn.setUseCaches(true);
			in=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

			char[] buf=new char[1024]; // 用于取得每部分数据

			// 读取URL中的内容并显示在页面中
			for(;;)
			{
				int moved=in.read(buf); // 读取字符并获得长度

				// 如果有内容，就把内容拼装起来
				if(moved<0) break;
				else if(moved<1024) sbuf.append(buf,0,moved);
				else sbuf.append(buf);
			}
		}
		catch(IOException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if(in!=null) in.close();
			}
			catch(IOException e)
			{
				log.error("Can't close URL connection : "+url+" !");
			}
		}

		return sbuf;
	}

	/**
	 * 从应用的范围内查找并返回一个变量
	 * 
	 * @param page
	 * @param name
	 *            变量的名称
	 * @param scope
	 *            page | request | session
	 * @return
	 */
	static public Object findAttribute(PageContext page,String name,String scope)
	{
		Object result=null;
		if("page".equalsIgnoreCase(scope)) result=page.getAttribute(name,PageContext.PAGE_SCOPE);
		else if("request".equalsIgnoreCase(scope)) result=page.getAttribute(name,PageContext.REQUEST_SCOPE);
		else if("session".equalsIgnoreCase(scope)) result=page.getAttribute(name,PageContext.SESSION_SCOPE);
		else result=page.findAttribute(name);
		return result;
	}

	/**
	 * 把一个没有ContextPath的URI加上ContextPath
	 * 
	 * @param page
	 * @param path
	 * @return
	 */
	static public String addContextPath(PageContext page,String path)
	{
		if(path==null) path="";
		HttpServletRequest request=(HttpServletRequest)page.getRequest();

		// 如果是相对路径，直接在上面加上URI。绝对路径就补上ContextPath。
		if(path.length()>0&&!path.startsWith("/"))
		{
			String uri=request.getRequestURI();
			return uri.substring(0,uri.lastIndexOf("/")+1)+path;
		}
		return request.getContextPath()+path;
	}

	/**
	 * 把一个有ContextPath的URI的ContextPath给删除
	 * 
	 * @param page
	 * @param path
	 * @return
	 */
	static public String subContextPath(PageContext page,String path)
	{
		if(page!=null)
		{
			HttpServletRequest request=(HttpServletRequest)page.getRequest();
			return subContextPath(request.getContextPath(),path);
		}
		return path;
	}

	static public String subContextPath(String contextPath,String path)
	{
		if(path!=null&&path.length()>0)
		{
			if(!path.startsWith("/")) path="/"+path;
			if(path.length()>=contextPath.length()&&path.startsWith(contextPath)) return path.substring(contextPath.length(),path.length());
		}
		return path;
	}

	/**
	 * 从Request的Map中返回一个字段的值，多个值之间用'\n'分隔
	 * 
	 * @param name
	 * @param values
	 * @return
	 */
	public static String getValueFromRequestMap(String name,Map<String,String> values)
	{
		Object obj=values.get(name);

		if(obj!=null)
		{
			if(obj instanceof String[]) return StringUtils.join((String[])obj,'\n');
			else return obj.toString();
		}

		return null;
	}
	
	/**
	 * 取得客户端的IP
	 * @param request
	 * @return
	 */
	public static String getClientIP(HttpServletRequest request)
	{
		String ip = request.getHeader("x-forwarded-for");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}
		
		if(ip==null) ip="";  //防止出错不直接返回Null
		
		return ip;
	}
}
