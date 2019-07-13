package com.larlf.general.layout.model;

import javax.servlet.jsp.PageContext;

/**
 * <h1>Property的接口</h1>
 * @author larlf
 */
public interface Property
{
	/**
	 * 设定这个Property的值
	 * @param value
	 */
	public void setValue(String value);
	
	/**
	 * 取得这个Property的值
	 */
	public String getValue();
	
	/**
	 * 把这个Property的内容写入到PageContext对象中
	 * @param pageContext
	 */
	public void write(PageContext pageContext);
}
