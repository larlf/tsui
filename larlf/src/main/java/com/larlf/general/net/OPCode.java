package com.larlf.general.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.larlf.general.ctrl.EventBus;
import com.larlf.general.utils.FileUtils;

/**
 * 用于操作代码转换
 * @author Larlf.Wang
 * @creation 2012-11-7
 */
public class OPCode
{
	static Logger log=Logger.getLogger(OPCode.class);
	static private Map<String, Long> OP=new HashMap<String, Long>();
	static private Map<Long, String> CODE=new HashMap<Long, String>();
	
	/**
	 * 读取代码表
	 * @param filename
	 */
	static public void init(String filename)
	{
		try
		{
			String text = FileUtils.readFile(EventBus.class.getResource(filename));
			JSONObject json = new JSONObject(text);
			if(json != null)
			{
				Iterator<?> it=json.keys();
				while(it.hasNext())
				{
					String key=it.next().toString();
					long op=json.getLong(key);
					OP.put(key, op);
					CODE.put(op, key);
				}
			}
		}
		catch(Exception e)
		{
			log.error("Init OP error", e);
		}
	}
	
	/**
	 * 把代码转换成数值
	 * @param code
	 * @return
	 */
	static public long getOP(String code)
	{
		if(OP.containsKey(code))
			return OP.get(code);
		else
			log.error("Cannot find code : "+code);
		
		return 0;
	}
	
	/**
	 * 把数值转换成代码
	 * @param op
	 * @return
	 */
	static public String getCode(long op)
	{
		if(op>0)
		{
			if(CODE.containsKey(op))
				return CODE.get(op);
			else
				log.error("Cannot find op : "+op);
		}
		
		return "";
	}
}
