package com.larlf.general.ctrl;

import org.apache.log4j.Logger;

public abstract class Task extends Thread implements ITask
{
	public int workload;		// 总的工作量
	public int worked;			// 当前工作进度
	public String working;		// 正在工作的内容
	public long startTime;		//开始时间

	protected Logger log = Logger.getLogger(this.getClass());
	protected boolean running = true;		// 是否要运行下去
	protected String taskName;			// 任务名称，需要在start()中进行设

	/**
	 * 开始任务
	 * 
	 * @param data
	 */
	public synchronized void start(String name)
	{
		this.taskName=name;
		
		// 检查是否是单例模式，如果是的话进行判断
		if(this.isSingle())
		{
			if(TaskBus.taskCount(this.getClass(), name) > 0)
			{
				log.info("Single task " + this.getClass() + " existed, stop new task.");
				return;
			}
		}

		super.start();
	}
	
	@Override
	public synchronized void start()
	{
		log.error("Task thread must set name");
	}

	@Override
	public void run()
	{
		TaskBus.addTask(this);

		try
		{
			this.execute();
		}
		catch(Exception e)
		{
			log.error("Execute task error", e);
		}
		finally
		{
			this.workload = 0;
			this.workload = 0;
			this.working = "";

			TaskBus.removeTask(this);
		}
	}

	/**
	 * 通知任务结束，只是改变状态，具体动作依赖实现
	 */
	public void shutdown()
	{
		this.running = false;
	}

	/**
	 * 是否是单例格式，单例的Task需要重载此方法
	 * 
	 * @return
	 */
	public boolean isSingle()
	{
		return false;
	}
	
	/**
	 * 返回任务的名称
	 * @return
	 */
	public String getTaskName()
	{
		return this.taskName;
	}
}
