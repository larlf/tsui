package com.larlf.general.ajax;


import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;

public class BOPoolFactory extends BaseKeyedPoolableObjectFactory
{

	@Override
	public Object makeObject(Object clazz) throws Exception
	{
		if(clazz != null)
		{
			return ((Class<?>) clazz).newInstance();
		}

		return null;
	}

}
