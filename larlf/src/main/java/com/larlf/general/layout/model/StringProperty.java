package com.larlf.general.layout.model;

import java.io.IOException;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;

/**
 * <h1>把值做为字符串显示出来的Property</h1>
 * @author larlf
 *
 */
public class StringProperty extends BaseProperty
{
	Logger log=Logger.getLogger(this.getClass());

	public void write(PageContext pageContext)
	{
		try
		{
			//把内容在页面上打印出来
			pageContext.getOut().write(this.value);
		}
		catch(IOException e)
		{
			log.error(e);
		}
	}

}
