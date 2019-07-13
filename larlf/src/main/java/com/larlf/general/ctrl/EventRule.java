package com.larlf.general.ctrl;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用于对时间规则进行验证
 * 
 * @author Larlf.Wang
 * @creation 2012-11-5
 */
public class EventRule
{
	private Logger log = Logger.getLogger(this.getClass());

	private Time equalRule = new Time();	// 相等规则
	private Time dividRule = new Time();	// 可除规则

	private String task;			// 执行的类名
	private String name;			// 任务的名称
	private JSONObject data;		// 注入的数据

	public EventRule(JSONObject data)
	{
		this.prase(data);
	}

	/**
	 * 检查时间是否符合要求
	 * 
	 * @param t
	 * @return
	 */
	boolean check(long t)
	{
		Time time = new Time(t);

		for(int i = 0; i < Time.POS_COUNT; ++i)
		{
			if(!this.checkValue(time.data[i], equalRule.data[i], dividRule.data[i])) return false;
		}

		return true;
	}

	/**
	 * 检查一项值是否符合规则
	 * 
	 * @param value
	 * @param equalValue
	 * @param dividValue
	 * @return
	 */
	boolean checkValue(int value, int equalValue, int dividValue)
	{
		if(equalValue >= 0 && equalValue != value) return false;
		if(dividValue > 0 && value % dividValue != 0) return false;

		return true;
	}

	/**
	 * 解析数据
	 * 
	 * @param data
	 */
	void prase(JSONObject data)
	{
		try
		{
			String rule = data.getString("rule");
			this.task = data.getString("task");
			this.name = data.has("name") ? data.getString("name") : "EventTask";
			this.data = data.has("data") ? data.getJSONObject("data") : null;
			this.praseTime(rule);
		}
		catch(JSONException e)
		{
			log.error(e);
		}
	}

	/**
	 * 解析时间规则
	 * 
	 * @param str
	 */
	void praseTime(String str)
	{
		str = str.replaceAll("\\s", " ");
		str = str.replaceAll("\\s{2,}", " ");
		String[] list = str.split(" ");

		for(int i = 0; i < list.length; ++i)
		{
			if(i < Time.POS_COUNT)
			{
				String s = list[i];
				int pos = s.indexOf("/");
				if(pos >= 0)
				{
					s = s.substring(pos + 1);
					this.dividRule.data[i] = Integer.valueOf(s);
				}
				else if(s.indexOf("*") < 0)
				{
					this.equalRule.data[i] = Integer.valueOf(s);
				}
			}
		}
	}

	/**
	 * 执行任务
	 */
	void execute(long time)
	{
		try
		{
			Class<?> clazz = Class.forName(this.task);
			if(clazz != null)
			{
				EventTask task = (EventTask) clazz.newInstance();
				if(task != null)
				{
					task.start(this.name, this.data, time);
				}
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}

	/**
	 * 打印信息
	 */
	public String toString()
	{
		StringBuilder str = new StringBuilder();

		str.append("[ ");
		for(int i = 0; i < Time.POS_COUNT; ++i)
		{
			if(this.equalRule.data[i] >= 0) str.append(this.equalRule.data[i]);
			else if(this.dividRule.data[i] >= 0) str.append("/").append(this.dividRule.data[i]);
			else str.append("*");

			str.append(" ");
		}
		str.append("]");

		str.append(" ").append(this.task).append("(").append(this.data == null ? "" : this.data.toString()).append(")");

		return str.toString();
	}
}
