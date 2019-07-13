package com.larlf.general.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;

public class WebRequestFilter implements Filter
{
	@Override public void init(FilterConfig filterConfig) throws ServletException
	{

	}

	@Override public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
	{
		if (((HttpServletRequest) servletRequest).getRequestURI().endsWith("/WebRequestFilter"))
		{
			servletResponse.getWriter().print("{\"state\":true}");
			servletResponse.flushBuffer();
		}
		else
		{
			HttpServletRequest req = (HttpServletRequest) servletRequest;
			Enumeration<String> it = req.getParameterNames();
			String params = "";
			while (it.hasMoreElements())
			{
				String key = it.nextElement();
				if (params.length() > 0)
					params += "&";

				String value = req.getParameter(key);
				if (value != null && req.getMethod().equalsIgnoreCase("GET"))
					value = new String(value.getBytes("ISO-8859-1"), "utf-8");

				params += key + "=" + URLEncoder.encode(value, "UTF-8");
			}
			System.out.println("[" + req.getMethod() + "] " + req.getRequestURL() + ((params.length() > 0) ? ("?" + params) : ""));
			filterChain.doFilter(servletRequest, servletResponse);
		}

	}

	@Override public void destroy()
	{

	}
}