package com.larlf.general.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConstantsServlet extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
	{
		String result="var _constants={\n";
		result+=this.addConstantVar("validateConfig",Constants.VALIDATE_CONFIG);
		result+=this.addConstantVar("validateI18n",Constants.VAIDATE_I18N);
		result+="_:\"\"}\n\n";
		response.getWriter().print(result);
	}

	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
	{
		this.doGet(request,response);
	}

	protected String addConstantVar(String name,String value)
	{
		return name+":\""+value+"\",\n";
	}

}
