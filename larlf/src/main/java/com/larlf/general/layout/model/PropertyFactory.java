package com.larlf.general.layout.model;

/**
 * <h1>于用创建Property对象的工厂</h1>
 * 
 * @author Larlf.Wang 
 * <p> 这个类实现了一个工厂模式。
 */
public class PropertyFactory
{
	/**
	 * <b>创建一个实现了Property的对象</b>
	 * @param propertyType string | page | url 不区分大小写
	 * @return Property接口的一个实现
	 */
	static public Property getInstance(String propertyType)
	{
		if(propertyType!=null&&!"".equals(propertyType))
		{
			if("string".equalsIgnoreCase(propertyType)) return new StringProperty();

			if("page".equalsIgnoreCase(propertyType)) return new PageProperty();

			if("url".equalsIgnoreCase(propertyType)) return new URLProperty();
		}

		return new StringProperty();
	}
}
