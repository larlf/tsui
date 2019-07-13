package com.larlf.general.layout.taglib;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;

import com.larlf.general.core.Constants;

/**
 * <h1>定义一个页面的模板</h1>
 * @author larlf
 */
public class Define extends Layout
{
	Logger log=Logger.getLogger(this.getClass());

	protected String extend; //模板的父页面

	public int doStartTag() throws JspException
	{
		//从Request中取得所有的Property
		Object obj=this.pageContext.getRequest().getAttribute(Constants.LAYOUT_REQUEST_MAP);
		if(obj!=null) this.properties=(Map)obj;
		else this.properties=new HashMap();

		return Tag.EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException
	{
		try
		{
			//把所有的Property存入到Request
			this.pageContext.getRequest().setAttribute(Constants.LAYOUT_REQUEST_MAP,this.properties);

			//如果是从其它模板继承来的，就加载相应的页面
			if(extend!=null&&!"".equals(extend)) this.pageContext.include(extend);
		}
		catch(Exception e)
		{
			log.error(e);
		}

		//释放资源
		this.properties=null;
		extend=null;
		return Tag.EVAL_PAGE;
	}

	public String getExtend()
	{
		return extend;
	}

	public void setExtend(String extend)
	{
		this.extend=extend;
	}

}
