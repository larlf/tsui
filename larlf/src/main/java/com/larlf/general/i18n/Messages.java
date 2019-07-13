package com.larlf.general.i18n;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>每一个Message对应着一个i18n.xml文件
 * <p>这里实现了从中取得国际化信息的方法
 * @author larlf
 */
public class Messages
{
	protected Logger log=Logger.getLogger(this.getClass());

	private Map<String, String> messages=new HashMap<String, String>(); //当前Message里存放的所有信息

	//不允许直接创建
	private Messages()
	{}

	/**
	 * 对Message进行初始化
	 * @param path
	 */
	public Messages(String path)
	{
		this.messages=I18nBO.readI18nResource(path);
	}

	/**
	 * 取出一条国际化信息
	 * @param name 信息的代码
	 * @return
	 */
	public String getMessage(String name)
	{
		return this.getMessage(name,I18nBO.getUserLang());
	}

	public String getMessage(String name,String lang)
	{
		name=lang+"\n"+name;
		String msg=messages.get(name);

		if(msg!=null) return msg;
		else return name.replace('\n',':');
	}

	/**
	 * 把信息中的占位符转换为国际化后的信息
	 * @param msg 需要转换的字符串
	 * @param start 占位符的开始标志
	 * @param end 占位符的结束标志
	 * @return
	 */
	public String replaceMessage(String msg,String start,String end)
	{
		if(msg!=null)
		{
			String[] strArray=StringUtils.split(msg,start);
			if(strArray.length>1)
			{
				int i=1;
				if(msg.startsWith(start)) i=0;
				for(;i<strArray.length;i++)
				{
					String i18nName=strArray[i].substring(0,strArray[i].indexOf(end));
					String i18nValue=this.getMessage(i18nName);
					msg=StringUtils.replace(msg,start+i18nName+end,i18nValue);
				}
			}
		}
		return msg;
	}
}
