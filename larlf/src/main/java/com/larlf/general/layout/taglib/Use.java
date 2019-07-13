package com.larlf.general.layout.taglib;

import java.util.HashMap;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;

import com.larlf.general.core.Constants;

/**
 * <h1>使用一个页面的模板</h1>
 * @author larlf
 */
public class Use extends Layout
{
	Logger log=Logger.getLogger(this.getClass());

	public String layout; //要使用的页面模板

	public int doStartTag() throws JspException
	{
		//使用模板页前，先把所有的Property清空
		this.properties=new HashMap();
		return Tag.EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException
	{
		//把Property存入Request中
		this.pageContext.getRequest().setAttribute(Constants.LAYOUT_REQUEST_MAP,this.properties);

		//去请求模板页
		try
		{
			if(layout!=null&&!"".equals(layout)) this.pageContext.include(layout);
		}
		catch(Exception e)
		{
			log.error(e);
		}

		//释放资源
		this.pageContext.getRequest().removeAttribute(Constants.LAYOUT_REQUEST_MAP);
		this.properties=null;
		layout=null;
		return Tag.EVAL_PAGE;
	}

	public String getLayout()
	{
		return layout;
	}

	public void setLayout(String layout)
	{
		this.layout=layout;
	}
}
