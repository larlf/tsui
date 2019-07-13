package com.larlf.general.net;

/**
 * 连接到其它服务器的客户端
 * @author Larlf.Wang
 */
public class PacketClient 
{
	private String host;
	private int port;

	public PacketClient(String host, int port)
	{
		this.host = host;
		this.port = port;
	}
}
