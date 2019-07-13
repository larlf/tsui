package com.larlf.general.layout.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.larlf.general.layout.model.Property;
import com.larlf.general.layout.model.PropertyFactory;

/**
 * <h1>从父标签中取出Property值</h1>
 * @author larlf
 */
public class Get extends TagSupport
{
	Logger log=Logger.getLogger(this.getClass());

	protected String name; //名称
	protected String value; //值
	protected String type; //类型

	public int doStartTag() throws JspException
	{
		Property property=null;

		if(name!=null&&!"".equals(name))
		{
			//取得父标签
			Layout layout=this.findLayout(this.getParent());

			//取得相应的Property
			property=layout.getProperty(name);

			//如果Property不存在，就显示默认值
		}

		if(property==null&&value!=null)
		{
			property=PropertyFactory.getInstance(type);
			property.setValue(value);
		}

		//如果存在Property，把内容到页面中
		if(property!=null)
		{
			property.write(this.pageContext);
			return Tag.SKIP_BODY;
		}

		name=null;
		value=null;
		type=null;
		return Tag.EVAL_BODY_INCLUDE;
	}

	protected Layout findLayout(Tag tag)
	{
		if(tag instanceof Layout) return (Layout)tag;
		else return findLayout(tag.getParent());
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name=name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type=type;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value=value;
	}

}
