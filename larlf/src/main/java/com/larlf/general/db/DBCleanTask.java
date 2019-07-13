package com.larlf.general.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.larlf.general.ctrl.EventTask;

/**
 * 定时清理没有用的DB连接的任务
 * 
 * @author Larlf.Wang
 * @creation 2012-11-6
 */
public class DBCleanTask extends EventTask
{
	public void execute()
	{
		List<Thread> badThreads = new ArrayList<Thread>();

		// 遍历所有的Thread
		this.workload = DBBus.threads.size();
		this.worked = 0;
		Iterator<Entry<Thread, Map<String, Connection>>> it = DBBus.threads.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Thread, Map<String, Connection>> entry = it.next();
			Thread t = entry.getKey();
			this.worked++;
			this.working = t.toString();

			// 如果已经结束，加入到失效队列中
			if(!t.isAlive())
			{
				badThreads.add(t);
			}
		}

		// 处理所有已经结束的线程
		if(badThreads.size() > 0)
		{
			this.workload = badThreads.size();
			this.worked = 0;

			for(int i = 0; i < badThreads.size(); ++i)
			{
				this.worked++;
				this.working = badThreads.get(i).toString();

				DBBus.closeConns(badThreads.get(i));
			}
		}
	}

}
