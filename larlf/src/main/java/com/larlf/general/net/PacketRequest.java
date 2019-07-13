package com.larlf.general.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;

/**
 * 一个用DataPacket协议的请求
 * 
 * @author Larlf.Wang
 * @creation 2012-11-6
 */
public class PacketRequest
{
	Logger log = Logger.getLogger(this.getClass());

	// 连接相关的信息
	private String host;
	private int port;
	private int timeOut = 60000; // 默认超时时间1分钟

	public PacketRequest(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	/*
	 * 发送协议包
	 */
	private DataPacket send(DataPacket sendPacket)
	{
		Socket sock = null;
		OutputStream os = null;
		DataInputStream dis = null;

		try
		{
			sock = new Socket(this.host, this.port);
			sock.setSoTimeout(this.timeOut);		// 1分钟未返回则捕获抛出的异常，关闭socket连接

			// 发送数据
			os = sock.getOutputStream();
			os.write(sendPacket.toByteArray());
			os.flush();
			log.debug("<= " + sendPacket.toString());

			// 读取返回包
			dis = new DataInputStream(sock.getInputStream());
			DataPacket receivePacket = new DataPacket();
			receivePacket.fromStream(dis);
			log.debug("=> " + receivePacket.toString());
			return receivePacket;
		}
		catch(IOException e)
		{
			log.error("Socket error", e);
		}
		finally
		{
			if(os!=null)
			{
				try
				{
					os.close();
				}
				catch(Exception e)
				{
					log.error("Close output error", e);
				}
			}
			
			if(dis!=null)
			{
				try
				{
					dis.close();
				}
				catch(Exception e)
				{
					log.error("Close input error", e);
				}
			}
			
			if(sock != null && sock.isConnected())
			{
				try
				{
					sock.close();
				}
				catch(IOException e)
				{
					log.error("Close error", e);
				}
			}
		}

		return null;
	}

	/**
	 * 发送请求
	 * @param op
	 * @param data
	 * @return
	 */
	private String send(long op, String data)
	{
		//构造发送的数据包
		DataPacket sendPacket = new DataPacket();
		sendPacket.setOp((int)op);
		sendPacket.setData(data);
		
		//构造数据包用于接收数据
		DataPacket receivePacket=this.send(sendPacket);
		if(receivePacket!=null)
		{
			String str=receivePacket.getStrData();
			if(str!=null && str.length()>0)
			{
				if(str.endsWith("\n")) str = str.substring(0, str.length() - 1);
				if(str.startsWith("\n")) str = str.substring(1);
				return str;
			}
		}
		
		return "";
	}
	
	/**
	 * 发送请求
	 * @param code
	 * @param data
	 * @return
	 */
	public String execute(String code, String ... params)
	{
		String data=code;
		
		for(String param : params)
		{
			//如果参数中有空格或引号，就处理一下
			if(param.indexOf(" ")>=0 || param.indexOf("\"")>=0)
			{
				param=param.replace("\"", "\\\"");
				param="\""+param+"\"";
			}
			
			data+=" "+param;
		}
		
		return this.send(0, data);
	}

	/**
	 * 设置socket超时时间
	 * @param timeOut
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
}
