package com.larlf.general.web;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class StaticFileFilter implements Filter
{

	@Override
	public void destroy()
	{
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException
	{
		HttpServletResponse response = (HttpServletResponse) resp;
		response.setHeader("Cache-Control", "max-age=604800");
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
	}

}
