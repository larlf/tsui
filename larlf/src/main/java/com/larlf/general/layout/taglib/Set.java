package com.larlf.general.layout.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;

/**
 * <h1>向父标签中添加Property值</h1>
 * @author larlf
 *
 */
public class Set extends BodyTagSupport
{
	Logger log=Logger.getLogger(this.getClass());

	protected String name; //名称
	protected String value; //值
	protected String type; //类型

	public int doEndTag() throws JspException
	{
		//取得父标签
		Layout layout=this.findLayout(this.getParent());

		//如果相应的Property还没有值，保存
		if(layout.getProperty(name)==null)
		{
			if(value==null) value="";

			//如果标签中有内容，把相应的内容做为值
			if(this.getBodyContent()!=null) value+=this.getBodyContent().getString();

			layout.putProperty(name,value,type); //生成、保存Property
		}

		name=null;
		value=null;
		type=null;
		return Tag.EVAL_PAGE;
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
