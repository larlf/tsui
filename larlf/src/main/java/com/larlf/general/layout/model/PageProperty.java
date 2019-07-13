package com.larlf.general.layout.model;

import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;

/**
 * <h1>加载应用中页面的Property</h1>
 * <p>发现如果用了Struts，在Include一个路径后在一些容器中会把out给Close掉，
 * 所以对Struts的路径（以.do结束），要采用URL获取的方式而非这里的Page方式。
 * <b>注意：</b>在URL方式中引入的内容，Session和Request里的变量都是无效的。
 * 这并非是一个Bug，因为就算你使用&lt;jsp:include/&gt;标签，也会有同样的问题。
 * @author larlf
 * @see com.opesoft.layout.model.Property
 */
public class PageProperty extends BaseProperty
{
	Logger log=Logger.getLogger(this.getClass());

	public void write(PageContext pageContext)
	{
		try
		{
			if(this.value!=null&&!"".equals(this.value))
			{
				pageContext.include(this.value);
			}
		}
		catch(Exception e)
		{
			log.error("Get page failed ! ["+this.value+"]");
		}
	}
}
