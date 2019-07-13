package com.larlf.general.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TaskBus
{
	static protected Map<Class<?>, Map<Thread, Task>> tasks=new HashMap<Class<?>, Map<Thread,Task>>();	//所有正在进行的任务
	
	/**
	 * 添加正在工作的
	 * @param task
	 */
	static public void addTask(Task task)
	{
		if(task!=null)
		{
			Thread t=Thread.currentThread();
			Class<?> clazz=task.getClass();
			
			Map<Thread, Task> threads=tasks.get(clazz);
			if(threads==null)
			{
				threads=new HashMap<Thread, Task>();
				tasks.put(clazz, threads);
			}
			
			threads.put(t, task);
		}
	}
	
	/**
	 * 移除正在工作的Task
	 * @param task
	 */
	static public void removeTask(Task task)
	{
		Thread t = Thread.currentThread();
		Class<?> clazz = task.getClass();
		if(tasks.containsKey(clazz) && tasks.get(clazz).containsKey(t)){
			tasks.get(clazz).remove(t);
			if(tasks.get(clazz).size() == 0){
				tasks.remove(clazz);
			}
		}
	}
	
	/**
	 * 取得一类正在运行的任务的数目
	 * @param clazz
	 */
	static public int taskCount(Class<?> clazz)
	{
		int count = 0;
		if(tasks.containsKey(clazz)){
			Map<Thread, Task> taskMap = tasks.get(clazz);
			count = taskMap.size();
		}
		return count;
	}
	
	/**
	 * 取得指定名称的一类任务数目
	 * @param clazz
	 * @param name
	 * @return
	 */
	static public int taskCount(Class<?> clazz, String name)
	{
		if(name==null) return taskCount(clazz);
		
		int count=0;
		
		Map<Thread, Task> taskMap=tasks.get(clazz);
		if(taskMap!=null)
		{
			Iterator<Thread> it=taskMap.keySet().iterator();
			while(it.hasNext())
			{
				Task task=taskMap.get(it.next());
				if(task!=null && name.equals(task.getTaskName()))
				{
					count++;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * 取得正在运行的任务列表
	 * @return
	 */
	static public List<Task> getTasks()
	{
		List<Task> taskList = new ArrayList<Task>();
		for (Map.Entry<Class<?>, Map<Thread, Task>> tasksEntry : tasks.entrySet()) {
			for (Map.Entry<Thread, Task> taskEntry : tasksEntry.getValue().entrySet()) {
				taskList.add(taskEntry.getValue());
			}
		}
		return taskList;
	}
}
