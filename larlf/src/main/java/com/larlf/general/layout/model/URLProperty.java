package com.larlf.general.layout.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;

import com.larlf.general.utils.WebUtils;

/**
 * <h1>从一个URL读取数据的Property</h1>
 * @author larlf
 *
 */
public class URLProperty extends BaseProperty
{
	Logger log=Logger.getLogger(this.getClass());

	public void write(PageContext pageContext)
	{
		try
		{
			if(this.value!=null&&!"".equals(this.value))
			{
				if(this.value.startsWith("/"))
				{//对于同一个应用中的处理方法
					HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
					this.value=WebUtils.spellURLRoot(request.getRequestURL().toString(),request.getRequestURI(),request.getContextPath())+this.value;
					pageContext.getOut().print(WebUtils.readFromURL(this.value)); //输出内容
				}
				else
				{//对于远程URL处理的方法
					if(!this.value.startsWith("http://")) this.value="http://"+this.value;
					pageContext.getOut().print(WebUtils.readFromURL(this.value)); //输出内容
				}
			}
		}
		catch(IOException e)
		{
			log.error("Can't read data from URL : "+this.value+" !");
		}
	}

}
