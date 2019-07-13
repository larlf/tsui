package com.larlf.general.ctrl;

import org.json.JSONObject;

/**
 * 事件任务
 * 
 * @author Larlf.Wang
 * @creation 2012-11-6
 */
public abstract class EventTask extends Task
{
	protected JSONObject data;				// 运行数据
	protected long time;					// 运行时间
	

	/**
	 * 开始任务
	 * 
	 * @param data
	 */
	public synchronized void start(String name, JSONObject data, long time)
	{
		this.data = data;
		this.time = time;
		super.start(name);
	}
}
