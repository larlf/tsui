package com.larlf.general.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import com.larlf.general.core.Constants;

/**
 * 一些用于字符串处理的公用方法
 * 
 * @author larlf
 */
public class StrUtils
{
	static Logger log=Logger.getLogger(StrUtils.class);

	/**
	 * 把一个URL（全小写，下划线分隔）改成Class命名的习惯
	 * 
	 * @param urlName
	 * @return
	 */
	static public String uriName2className(String urlName)
	{
		if(urlName!=null)
		{
			String[] strArray=urlName.split("_");
			if(strArray!=null&&strArray.length>0)
			{
				urlName="";
				for(int i=0;i<strArray.length;i++)
				{
					String fromStr=strArray[i];
					String toStr="";
					if(fromStr!=null&&fromStr.length()>0)
					{
						toStr+=fromStr.substring(0,1).toUpperCase();
						if(fromStr.length()>1) toStr+=fromStr.substring(1);
					}

					urlName+=toStr;
				}
			}
		}
		return urlName;
	}

	/**
	 * 把一个URL的路径对应为一个Class
	 * 
	 * @param url
	 * @return
	 */
	static public String uri2class(String url)
	{
		String clazz="";

		if(url!=null&&url.length()>0)
		{
			if(url.startsWith("/")) url=url.substring(1);
			String[] strArray=url.split("/");
			int i=0;
			for(;i<strArray.length-1;i++)
			{
				clazz+=strArray[i]+".";
			}
			clazz+=uriName2className(strArray[i]);
		}

		return clazz;
	}

	/**
	 * 把一个GET方式发送来的Form中的数据变成Map
	 * 
	 * @param str
	 * @return
	 */
	static public Map<String, Object> formData2Map(String str)
	{
		Map<String, Object> values=new HashMap<String, Object>();

		if(str!=null)
		{
			String[] strArray=str.split("&");
			if(strArray!=null&&strArray.length>0)
			{
				for(String str2 : strArray)
				{
					String[] strArray2=str2.split("=");
					if(strArray2!=null)
					{
						if(strArray2.length==1) values.put(strArray2[0],"");
						else if(strArray2.length>1)
						{
							String data=strArray2[1];
							try
							{
								data=java.net.URLDecoder.decode(data,Constants.ENCODING);
							}
							catch(UnsupportedEncodingException e)
							{
								e.printStackTrace();
							}
							
							Object value=values.get(strArray2[0]);
							
							//处理多选的情况(name=value1&name=value2&name=value3)
							if(value!=null)
							{
								//如果已经是数组，把下一个选项加在上面
								if(value.getClass().isArray())
								{
									value=ArrayUtils.add((String[])value,data);
								}
								else
								{
									//构造新的数组
									value=new String[]{value.toString(),data};
								}
							}
							else value=data;
							
							values.put(strArray2[0],value);
						}
					}
				}
			}
		}

		return values;
	}
}
