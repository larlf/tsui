package com.larlf.general.web;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.larlf.general.core.Constants;
import com.larlf.general.i18n.I18nBO;

import java.io.*;

/**
 * 用于进行本地化的过滤器
 * 
 * @author larlf
 */
public class I18nFilter implements Filter
{
	public Logger log=Logger.getLogger(this.getClass());

	private FilterConfig cfg;

	public I18nFilter()
	{}

	/**
	 * 初始化系统的字符集
	 * 
	 * @param filterConfig
	 *            FilterConfig
	 */
	public void init(FilterConfig filterConfig)
	{
		this.cfg=filterConfig;

		String encoding=cfg.getInitParameter("encoding");
		String language=cfg.getInitParameter("language");
		String country=cfg.getInitParameter("country");

		if(encoding!=null&&!"".equals(encoding)) Constants.ENCODING=encoding;
		if(language!=null&&!"".equals(language)) Constants.LANGUAGE=language;
		if(country!=null&&!"".equals(country)) Constants.COUNTRY=country;

		log.info("Init the localization setup : "+Constants.LANGUAGE+"_"+Constants.COUNTRY+"."+Constants.ENCODING);
	}

	/**
	 * doFilter
	 * 
	 * @param request
	 *            ServletRequest
	 * @param response
	 *            ServletResponse
	 * @param chain
	 *            FilterChain
	 */
	public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws UnsupportedEncodingException,ServletException,IOException
	{
		// 设定Request的字符集
		request.setCharacterEncoding(Constants.ENCODING);

		// 对当前和户的语言进行设定
		// 因为用取了Thread池，在离来时需所进行一下remove的操作
		this.setUserLang((HttpServletRequest)request,(HttpServletResponse)response);
		chain.doFilter(request,response);
		I18nBO.removeUserLang();
	}

	/**
	 * 检查并设置用户的语言
	 * 
	 * @param request
	 * @param response
	 */
	protected void setUserLang(HttpServletRequest request,HttpServletResponse response)
	{
		// 从Session中取用户语言
		Object lang=request.getSession().getAttribute(Constants.LANGUAGE_PARAMETER);

		if(lang==null||"".equals(lang))
		{
			// 如果没有从Cookie中取
			Cookie[] cookies=request.getCookies();
			if(cookies!=null)
			{
				for(int i=0;i<cookies.length;i++)
				{
					Cookie cookie=cookies[i];
					if(Constants.LANGUAGE_PARAMETER.equalsIgnoreCase(cookie.getName())) lang=I18nBO.reviseLanguage(cookie.getValue());
				}
			}

			// 如果没有，就从Http的头信息中取
			if(lang==null||"".equals(lang))
			{
				lang=I18nBO.reviseLanguage(request.getHeader("accept-language"));
				if(lang==null) lang=Constants.LANGUAGE; // 如果没有，就取默认值
			}

			//如果都取不到，就使用默认值
			if(lang==null||"".equals(lang)) lang=Constants.LANGUAGE;

			I18nBO.setupUserLang(lang.toString(),request,response);
		}

		I18nBO.setUserLang(lang.toString());
	}

	/**
	 * destroy
	 */
	public void destroy()
	{
		this.cfg=null;
	}
}
