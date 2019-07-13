package com.larlf.general.net;

import java.io.DataInputStream;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.json.JSONObject;

/*
 * GM们需保持在线，故应采用长连接，每个管理员在jsp的sessionDestroyed时断开与控制服的连接
 */
public class DataPacket
{
	// 用于进行16进制转换
	static String hexString = "0123456789ABCDEF";

	// 报文格式：4字节（报文长度）+ 报文头（类似head，笑傲中接下来3个字段，每个字段各4字节） + 报文体内容 +
	// 报文分隔符(笑傲中4字节4个0xFF)；
	public static final byte[] SOCKET_DELIMITER = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
	public static final int SOCKET_HEAD_LEN = 16;

	// 加密时使用，必须和游戏中的一致，相当于密钥
	private static final String SOCKET_KEY = "C!@s7DfOfs$vJaFSfrxAzIXnzgXC6o1!.4@cZtL6leea@JUd0H2CRpZgZp.ngvLQ";

	Logger log = Logger.getLogger(this.getClass());

	private int op;				// 可定义一个为GM专有的操作码;也可以不定义，通过IP鉴权控制
	private int clientId;		// GM帐号userId，不做鉴权用，主要用作操作日志记录
	private int info;			// BUS_TYPE_RESPONSE，需要异步回调，返回操作结果
	private byte[] data;		// 控制服解析data，执行响应操作,该协议需另行定义，协议：cmd arg1 arg2 转成json后传输

	public DataPacket()
	{
		this.data = new byte[0];// 避免空指针异常
	}

	/**
	 * 报文总大小，即报文前4个字节
	 * 
	 * @return
	 */
	public int size()
	{
		return data.length + SOCKET_HEAD_LEN + SOCKET_DELIMITER.length;
	}

	public void encode()
	{
		int SOCKET_KEY_SIZE = SOCKET_KEY.length();
		for(int i = 0; i < data.length; ++i)
		{
			data[i] ^= SOCKET_KEY.charAt(i % SOCKET_KEY_SIZE);
		}
	}

	public void decode()
	{
		encode();
	}

	public byte[] toByteArray()
	{
		byte[] bytes = new byte[size()];
		byte[] b0 = intToBytes(size());
		System.arraycopy(b0, 0, bytes, 0, b0.length);
		byte[] b1 = intToBytes(this.op);
		System.arraycopy(b1, 0, bytes, 4, b1.length);
		byte[] b2 = intToBytes(this.clientId);
		System.arraycopy(b2, 0, bytes, 8, b2.length);
		byte[] b3 = intToBytes(this.info);
		System.arraycopy(b3, 0, bytes, 12, b3.length);
		System.arraycopy(data, 0, bytes, SOCKET_HEAD_LEN, data.length);
		System.arraycopy(SOCKET_DELIMITER, 0, bytes, data.length + SOCKET_HEAD_LEN, SOCKET_DELIMITER.length);
		return bytes;
	}

	/**
	 * 从输入流中读取包
	 * 
	 * @param dis
	 */
	public void fromStream(DataInputStream dis)
	{
		if(dis != null)
		{
			try
			{
				//读取包的长度并构造数据容器
				int size = dis.readInt();
				this.data = new byte[size - DataPacket.SOCKET_HEAD_LEN - DataPacket.SOCKET_DELIMITER.length];
				
				//读取数据
				this.op = dis.readInt();
				this.clientId = dis.readInt();
				this.info = dis.readInt();
				dis.read(this.data);
			}
			catch(Exception e)
			{
				log.error("Read packet error", e);
			}
		}
	}

	public int getInfo()
	{
		return info;
	}

	public void setInfo(int info)
	{
		this.info = info;
	}

	public int getClientId()
	{
		return clientId;
	}

	public void setClientId(int clientId)
	{
		this.clientId = clientId;
	}

	public int getOp()
	{
		return op;
	}

	public void setOp(int op)
	{
		this.op = op;
	}

	public byte[] getData()
	{
		return data;
	}

	public String getStrData()
	{
		try
		{
			return new String(data, "UTF-8");
		}
		catch(UnsupportedEncodingException e)
		{
			log.error("Encode UTF-8 string error", e);
		}

		return "";
	}
	
	public JSONObject getJsonData()
	{
		try
		{
			String str=new String(data, "UTF-8");
			if(str!=null)
			{
				JSONObject json=new JSONObject(str);
				return json;
			}
		}
		catch(Exception e)
		{
			log.error("Parse json error", e);
		}
		
		return null;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public void setData(String dataStr)
	{
		try
		{
			this.data = dataStr.getBytes("UTF-8");
		}
		catch(UnsupportedEncodingException e)
		{
			log.error("Read UTF-8 string error", e);
		}
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("size=").append(size()).append(",");
		sb.append("op=").append(OPCode.getCode(this.op)).append(",");
		sb.append("clientId=").append(clientId).append(",");
		sb.append("info=").append(info).append(",");
		//sb.append("data=").append(this.getStrData());
		return sb.toString();
	}

	/**
	 * 整数转Bytes
	 * 
	 * @param number
	 * @return
	 */
	private static byte[] intToBytes(int number)
	{
		int temp = number;
		byte[] b = new byte[4];
		for(int i = b.length - 1; i >= 0; --i)
		{
			b[i] = new Integer(temp & 0xFF).byteValue();
			temp = temp >> 8;
		}

		return b;
	}
}
