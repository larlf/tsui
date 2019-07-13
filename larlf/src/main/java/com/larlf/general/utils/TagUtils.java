package com.larlf.general.utils;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class TagUtils
{
	static Logger log=Logger.getLogger(TagUtils.class);

	static public JspContext getParentJspContext(SimpleTagSupport tag)
	{
		Object parent=tag.getParent();
		if(parent!=null)
		{
			try
			{
				Object jspContext=PropertyUtils.getProperty(parent,"jspContext");
				if(jspContext!=null) return (JspContext)jspContext;
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}

		return null;
	}

	static public JspContext getParentJspContext(SimpleTagSupport tag,String tagName)
	{
		Object parent;

		try
		{
			while((parent=tag.getParent())!=null)
			{
				// 不同的应用服务器这里的处理可能会不太一样
				if(parent.getClass().getSimpleName().equals(tagName+"_tag"))
				{
					Object jspContext=PropertyUtils.getProperty(parent,"jspContext");
					if(jspContext!=null) return (JspContext)jspContext;
				}

				tag=(SimpleTagSupport)parent;
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		
		return null;
	}
}
