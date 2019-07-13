package com.larlf.general.sys;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log的管理类
 */
public class FileLogManager
{
	//用于对文件名进行格式化的类
	static SimpleDateFormat fileTimeFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	//用于生成ID格式的类
	static SimpleDateFormat IDTimeFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

	Logger log = Logger.getLogger(this.getClass());

	String logPath;  //用于存放Log的地址
	String name;
	long interval;

	Date startTime;
	Date endTime;
	//Date lastIDTime;
	String lastIdPrefix="";
	int idIndex;

	String tempLogFilename;
	Writer tempLogWriter;

	/**
	 * 创建一个Log管理器
	 *
	 * @param name     log的名称
	 * @param logPath  存放log的位置
	 * @param interval 间隔多少秒生成新的Log文件
	 */
	public FileLogManager(String name, String logPath, int interval)
	{
		this.name = name;
		this.interval = interval * 1000;

		//检查是否有分隔符
		if (!logPath.endsWith("/") && !logPath.endsWith("\\"))
			logPath += File.separator;

		this.logPath = logPath;
		this.tempLogFilename = logPath + name + "_current.log";

		checkLogPath(logPath);
	}

	/**
	 * 检查是否有存放Log的目录
	 * @param logPath
	 */
	private void checkLogPath(String logPath)
	{
		File path = new File(logPath);

		//检查是否存在
		if (!path.exists())
		{
			if (!path.mkdirs())
			{
				log.error("Cannot create log path : " + logPath);
				return;
			}
		}

		//检查是否是目录
		if (!path.isDirectory())
		{
			log.error("Log path is bad : " + logPath);
			return;
		}

		//检查是不是有留下来的文件
		this.startTime = new Date();
		this.endTime = new Date(this.startTime.getTime() + this.interval);
		this.renameLogFile();
	}

	/**
	 * 默认隔10分钟生成一个新的Log文件
	 *
	 * @param name
	 * @param logPath
	 */
	public FileLogManager(String name, String logPath)
	{
		this(name, logPath, 600);
	}

	/**
	 * 记录数据内容，会自动在后面加个\n
	 *
	 * @param data
	 */
	public void record(Object data)
	{
		Gson gson = new Gson();
		String msg = gson.toJson(data) + "\n";
		this.writeLog(new Date(), msg, 0);
	}

	/**
	 * 记录数据内容，会自动在后面加个\n
	 *
	 * @param data
	 */
	public void record(String data)
	{
		this.writeLog(new Date(), data, 0);
	}

	/**
	 * 这个方法会强到产生一个把当前的Log命名为正式文件
	 */
	public void flush()
	{
		this.newLogFile();
	}

	/**
	 * 重新命名现有的Log文件
	 */
	synchronized private void renameLogFile()
	{
		//检查文件是否存在
		File file = new File(this.tempLogFilename);
		if (!file.exists())
			return;

		//如果有文件，确定文件的名称
		String filename = this.logPath + this.name;
		filename += "_" + fileTimeFormat.format(new Date());
		filename += ".log";

		//重命名文件
		File destFile = new File(filename);
		int index = 0;
		while (destFile.exists())
		{
			//如果文件已经存在，就多加个后缀
			index++;
			destFile = new File(filename.substring(0, filename.length() - 4) + "_" + index + ".log");
		}

		log.debug("Rename log file from " + file + " to " + destFile);
		if (!file.renameTo(destFile))
		{
			log.error("Cannot rename log file from " + file + " to " + destFile);
		}
	}

	/**
	 * 写入Log，这个方法不能并发执行
	 *
	 * @param time
	 * @param msg
	 */
	synchronized private void writeLog(Date time, String msg, int retry)
	{
		//如果写Log的目录有问题，直接返回不处理
		if (this.logPath == null)
			return;

		String id=IDTimeFormat.format(time);
		if(!id.equalsIgnoreCase(this.lastIdPrefix))
		{
			this.lastIdPrefix=id;
			this.idIndex = 0;
		}

		//在信息前面加上ID
		String newMsg = this.lastIdPrefix + "_" + (this.idIndex++) + "|" + msg;

		//要保证后面有个换行符
		if(!newMsg.endsWith("\n"))
			newMsg+="\n";

		//需要写入下一个文件
		if (time.compareTo(this.endTime) >= 0)
			newLogFile();

		if (this.tempLogWriter == null)
		{
			//创建临时文件
			File file = new File(this.tempLogFilename);
			if (!file.exists())
			{
				log.debug("Crate log file : " + this.tempLogFilename);
				try
				{
					//检查目录是不是存在
					this.checkLogPath(this.logPath);

					if (!file.createNewFile())
					{
						log.error("Cannot create log file : " + this.tempLogFilename);
						return;
					}
				}
				catch (IOException e)
				{
					log.error("Create log file error : " + this.tempLogFilename, e);
					return;
				}
			}

			try
			{
				//this.tempLogWriter = new FileWriter(file);
				//this.tempLogWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
				this.tempLogWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
				//this.tempLogWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"));
			}
			catch (IOException e)
			{
				log.error("Craete file writer error : " + this.tempLogFilename, e);
				return;
			}
		}

		try
		{
			if (this.tempLogWriter != null)
			{
				//写入Log数据
				this.tempLogWriter.write(newMsg);
				this.tempLogWriter.flush();
			}
		}
		catch (IOException e)
		{
			log.error("Write log error : " + msg, e);

			//尝试关闭
			try
			{
				this.tempLogWriter.close();
			}
			catch(IOException ex)
			{
			}

			this.tempLogWriter=null;

			if(retry<3)
				this.writeLog(time, msg,++retry);
		}
	}

	/**
	 * 创建新的Log文件
	 */
	synchronized private void newLogFile()
	{
		//关闭当前的写文件
		if (this.tempLogWriter != null)
		{
			try
			{
				this.tempLogWriter.flush();
				this.tempLogWriter.close();
			}
			catch (IOException e)
			{
				log.error("Close log file error : " + this.tempLogFilename, e);
			}
			finally
			{
				this.tempLogWriter = null;
			}
		}

		//重命名文件
		this.renameLogFile();

		//重设时间
		this.startTime=new Date();
		this.endTime = new Date(this.startTime.getTime() + this.interval);
	}
}
