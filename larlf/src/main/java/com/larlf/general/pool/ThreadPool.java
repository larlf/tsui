package com.larlf.general.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;
import org.apache.log4j.Logger;

/**
 * 线程池的总线
 * @author wangwen
 */
public class ThreadPool
{
	static private Logger log=Logger.getLogger(ThreadPool.class);
	static private HashMap<Long, HashMap<String, Object>> Threads=new HashMap<Long, HashMap<String, Object>>();
	
	/**
	 * 取得固定类型的数据
	 * @param clazz
	 * @return
	 */
	static public void set(String key, Object obj)
	{
		//根据Thread的ID生成对象
		Long threadID=Thread.currentThread().getId();
		HashMap<String, Object> objs=null;
		if(!Threads.containsKey(threadID)) 
		{
			objs=new HashMap<String, Object>();
			Threads.put(threadID, objs);
		}
		else 
		{
			objs=Threads.get(threadID);
		}
		
		if(objs!=null)
		{
			//根据ClassName取得所有的对象
			if(objs.containsKey(key)) 
			{
				log.error("Repeated Thread Object : "+key+" : "+obj);
			}
			
			objs.put(key, obj);
		}
	}
	
	/**
	 * 取得线程对象
	 * @param clazz
	 * @return
	 */
	static public Object get(String key)
	{
		//根据Thread的ID生成对象
		Long threadID=Thread.currentThread().getId();
		if(Threads.containsKey(threadID)) 
		{
			HashMap<String, Object> objs=Threads.get(threadID);
			if(objs!=null)
			{
				return objs.get(key);
			}
		}
		
		return null;
	}
	
	/**
	 * 根据类型取出一组对象
	 * @param clazz
	 * @return
	 */
	static public List<Object> getByClass(Class<?> clazz)
	{
		List<Object> list=new ArrayList<Object>();
		
		Long threadID=Thread.currentThread().getId();
		if(Threads.containsKey(threadID)) 
		{
			HashMap<String, Object> objs=Threads.get(threadID);
			if(objs!=null)
			{
				//遍历取得指定类型的数据
				for(Map.Entry<String, Object> it : objs.entrySet())
				{
					if(ClassUtils.isAssignable(it.getValue().getClass(), clazz))
					{
						list.add(it.getValue());
					}
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 删除资源
	 * @param name
	 */
	static public void remove(String key)
	{	
		Long threadID=Thread.currentThread().getId();
		if(Threads.containsKey(threadID)) 
		{
			HashMap<String, Object> objs=Threads.get(threadID);
			if(objs!=null && objs.containsKey(key))
			{
				objs.remove(key);
			}
		}
		
		//检查删除此Thread
		checkRemoveThread(threadID);
	}
	
	/**
	 * 根据类型删除数据
	 * @param clazz
	 */
	static public void removeByClass(Class<?> clazz)
	{
		List<String> list=new ArrayList<String>();
		
		Long threadID=Thread.currentThread().getId();
		if(Threads.containsKey(threadID)) 
		{
			HashMap<String, Object> objs=Threads.get(threadID);
			if(objs!=null)
			{
				//遍历取得指定类型的数据
				for(Map.Entry<String, Object> it : objs.entrySet())
				{
					if(ClassUtils.isAssignable(it.getValue().getClass(), clazz))
					{
						list.add(it.getKey());
					}
				}
				
				//删除数据
				for(Object key : list)
				{
					objs.remove(key);
				}
			}
		}
		
		//检查删除此Thread
		checkRemoveThread(threadID);
	}	
	
	/**
	 * 检查当前Thread里是否还有对象，如果没有的话就从Thread集合中删除
	 * @param threadID
	 */
	static private void checkRemoveThread(Long threadID)
	{
		if(Threads.containsKey(threadID)) 
		{
			HashMap<String, Object> objs=Threads.get(threadID);
			if(objs!=null)
			{
				if(objs.size()<=0) Threads.remove(threadID);
			}
		}
	}
	
	/**
	 * 取得Thread的数量
	 * @return
	 */
	static public int threadSize()
	{
		return Threads.size();
	}
	
	/**
	 * 取得对象的数量
	 * @return
	 */
	static public int objSize()
	{
		int size=0;
		
		for(Map.Entry<Long, HashMap<String, Object>> it : Threads.entrySet())
		{
			if(it.getValue()!=null) size+=it.getValue().size()+1;
		}
		
		return size;
	}
}

