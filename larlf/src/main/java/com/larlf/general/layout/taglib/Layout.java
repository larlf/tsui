package com.larlf.general.layout.taglib;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;

import com.larlf.general.layout.model.Property;
import com.larlf.general.layout.model.PropertyFactory;

/**
 * <h1>可以对Property进行存取的标签</h1>
 * <p>这个类是Define和Layout的父类
 * @author larlf
 */
public abstract class Layout extends TagSupport
{
	Logger log=Logger.getLogger(this.getClass());

	protected Map properties=new HashMap(); //用于存放Property的Map

	/**
	 * 加入一个Property
	 * @param name
	 * @param value
	 * @param type
	 */
	@SuppressWarnings("unchecked")
	public void putProperty(String name,String value,String type)
	{
		//只有当这个Property不存在时才存进去，这样对Property的定义在前的优先
		if(properties.get(name)==null)
		{
			//从工厂中生成一个Property对象并保存
			Property property=PropertyFactory.getInstance(type);
			property.setValue(value);
			properties.put(name,property);
		}
	}

	/**
	 * 取得一个Property
	 * @param name Property的名称
	 * @return Property对象
	 */
	public Property getProperty(String name)
	{
		Object property=properties.get(name);
		if(property!=null) return (Property)property;
		else return null;
	}

	/**
	 * 释放properties中的资源
	 */
	public void release()
	{
		this.properties=null;
		super.release();
	}

}
