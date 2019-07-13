package com.larlf.general.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.larlf.general.utils.FileUtils;

public class EventBus
{
	static Logger log = Logger.getLogger(EventBus.class);

	// 处理事件家常匹配的线程
	static private EventBusThread thread = new EventBusThread();

	// 用来对事件表加锁
	static protected Lock eventLock = new ReentrantLock();

	// 所有的定时事件
	static protected List<EventRule> events = new ArrayList<EventRule>();

	/**
	 * 启动事件总线
	 */
	static public void start()
	{
		if (!thread.isAlive())
			thread.start();
	}

	/**
	 * 停止时间总线, 解决reload报错
	 */
	public static void stop()
	{
		thread.stopThread();
	}

	/**
	 * 用于检查事件总线是否是活跃状态
	 * 
	 * @return
	 */
	static public boolean active()
	{
		return thread.isAlive();
	}

	/**
	 * 从配置文件中刷新所有事件
	 * 
	 * @param filename
	 */
	static public void refresh(String filename)
	{
		if (EventBus.eventLock.tryLock())
		{
			try
			{
				events.clear();

				String text = FileUtils.readFile(EventBus.class
						.getResource(filename));
				JSONArray json = new JSONArray(text);
				if (json != null && json.length() > 0)
				{
					for (int i = 0; i < json.length(); ++i)
					{
						EventRule event = new EventRule(json.getJSONObject(i));
						events.add(event);
					}
				}
			}
			catch (Exception e)
			{
				log.error("Refresh events error : " + filename, e);
			}
			finally
			{
				EventBus.eventLock.unlock();
			}
		}
	}

	/**
	 * 打印信息
	 */
	static public String info()
	{
		StringBuilder str = new StringBuilder();
		str.append("Events:");

		for (int i = 0; i < events.size(); ++i)
		{
			str.append("\n").append(events.get(i).toString());
		}

		return str.toString();
	}
}
