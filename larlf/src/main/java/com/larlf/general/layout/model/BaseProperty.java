package com.larlf.general.layout.model;

/**
 * <h1>所有Property共同的基类</h1>
 * @author larlf
 * <p>这里实现了所有Property共同的方法
 */
public abstract class BaseProperty implements Property
{
	protected String value; //这个Property的值

	/**
	 * @see com.opesoft.layout.model.Property
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @see com.opesoft.layout.model.Property
	 */
	public void setValue(String value)
	{
		this.value=value;
	}

}
