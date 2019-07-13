/*
 * Created on 2005-7-26
 */
package com.larlf.general.core.exception;

import org.apache.log4j.Logger;

/**
 * 
 * @author Larlf.Wang
 */
public class ExceptionWrapper
{
	Logger log=Logger.getLogger(this.getClass());

	Exception e=new Exception("");

	public ExceptionWrapper(Exception e)
	{
		this.e=e;
		e.getStackTrace();
	}

	public String getShowMessage()
	{
		StringBuffer result=new StringBuffer();
		String mainMessage=null; // 主要的错误信息
		Throwable mainThrowable=null;

		if(e!=null)
		{
			Throwable throwable=e;

			// 循环所有的异常
			while(true)
			{
				// 如果有MessageException，它就是主要错误信息，之前的异常都无效
				// 但如果有多个MessageException，只取最后一个
				if(mainMessage==null&&throwable instanceof MessageException)
				{
					mainMessage=throwable.getMessage();
					mainThrowable=throwable;
					result=new StringBuffer();
				}
				else
				{
					result.append("\n["+throwable.getClass().getSimpleName()+"] "+throwable.getMessage());
				}

				if(throwable.getCause()!=null) throwable=throwable.getCause();
				else break;
			}

			if(mainThrowable==null) mainThrowable=throwable;

			result.append("\nTrace:");
			StackTraceElement[] stes=mainThrowable.getStackTrace();
			if(stes!=null)
			{
				for(int i=0;i<stes.length;i++)
				{
					String className=stes[i].getClassName();
					result.append("\n"+className+"."+stes[i].getMethodName()+"():"+stes[i].getLineNumber());
				}
			}
		}

		if(mainMessage==null) mainMessage="操作中出现错误！";

		return mainMessage+result.toString();
	}

	public String getMessage()
	{
		return e.getMessage();
	}

	public String getStackTrace()
	{
		StringBuffer sb=new StringBuffer();

		StackTraceElement[] ste=this.e.getStackTrace();

		if(ste!=null)
		{
			for(int i=0;i<ste.length;i++)
			{
				sb.append("\tat "+ste[i].getClassName());
				sb.append("."+ste[i].getMethodName());
				sb.append("("+ste[i].getFileName()+":"+ste[i].getLineNumber()+")");
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public String getReport()
	{
		return this.getMessage()+"\n"+this.getStackTrace();
	}
}
