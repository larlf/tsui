package com.larlf.general.i18n;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.larlf.general.core.Constants;

/**
 * <h1>国际化中一些共用的业务方法</h1>
 * 
 * @author larlf
 */
public class I18nBO
{
	static public Logger log=Logger.getLogger(I18nBO.class);

	static private Map<String, Messages> messagesMap=new HashMap<String, Messages>(); // 用来缓存Messages的一个Map

	static protected Map userLanguages=new HashMap(); // 保存当前线程的用户的国际化信息

	// 进行国际化转化的数组
	static private String[] cnByname=new String[]{"cn","中","china","cha"};
	static private String[] enByname=new String[]{"en","英","eng"};

	/**
	 * 根据一个URI，创建出对应的Message
	 * <p>
	 * 这个方法会在Product模式进行缓存。
	 * <p>
	 * 注意：一般不建议直接用new Messages(url)来创建Messages对象
	 * 
	 * @param uri
	 * @return
	 */
	static public Messages createMessages(String uri)
	{
		Messages msgs=null;

		// 产品模式使用缓存
		if(Constants.ProductMode.equalsIgnoreCase(Constants.MODE)) msgs=messagesMap.get(uri);

		if(msgs==null)
		{
			msgs=new Messages(uri);
			if(Constants.ProductMode.equalsIgnoreCase(Constants.MODE)) messagesMap.put(uri,msgs);
		}

		return msgs;
	}

	/**
	 * 更改当前用户的语言设定
	 * 
	 * @param lang :
	 *            语言代码
	 * @see I18nBO#reviseLanguage(String)
	 */
	static public void setupUserLang(String lang,HttpServletRequest request,HttpServletResponse response)
	{
		lang=I18nBO.reviseLanguage(lang);
		if(lang!=null)
		{
			setUserLang(lang);

			// 把值存入Session
			request.getSession().setAttribute(Constants.LANGUAGE_PARAMETER,lang);

			// 把值存入Cookie，保存一年
			Cookie cookie=new Cookie(Constants.LANGUAGE_PARAMETER,lang);
			cookie.setMaxAge(60*60*24*365);
			response.addCookie(cookie);
		}
	}

	/**
	 * 设置当前线程用户使用的语言
	 * <p>
	 * 同setupUserLang的区别是，这个设定只对当前线程有效。
	 * 
	 * @param lang
	 * @see I18nBO#reviseLanguage(String)
	 */
	@SuppressWarnings("unchecked")
	static public void setUserLang(String lang)
	{
		userLanguages.put(Thread.currentThread(),lang);
	}

	/**
	 * 从线程池中取出当前用户的语言
	 * 
	 * @return 用户的语言代码
	 */
	static public String getUserLang()
	{
		Object obj=userLanguages.get(Thread.currentThread());
		if(obj!=null) return obj.toString();
		return Constants.LANGUAGE;
	}

	/**
	 * 从线和池只把当前用户的语言设置注销了
	 * <p>
	 * 在使用线程池对用户国际化信息进行控制时，必需在Response之前操作此方法。
	 * <p>
	 * 否者可能会有Map中的资源无法收回
	 */
	static public void removeUserLang()
	{
		userLanguages.remove(Thread.currentThread());
	}

	/**
	 * 读取一个国际化资源文件的内容
	 * <p>
	 * 文件是一个*.i18n.xml，会使用本地的i18n.dtd进行验证
	 * 
	 * @param filePath :
	 *            文件的路径
	 */
	@SuppressWarnings("unchecked")
	static public Map<String, String> readI18nResource(String filePath)
	{
		Map<String, String> messages=new HashMap<String, String>();

		try
		{
			File file=new File(filePath);

			SAXReader reader=new SAXReader();
			reader.setEntityResolver(new I18nEntityResolver());
			reader.setIncludeInternalDTDDeclarations(false);
			Document document=reader.read(file);

			List moduleList=document.selectNodes("//module");
			if(moduleList!=null&&moduleList.size()>0)
			{
				for(int i=0;i<moduleList.size();i++)
				{
					Element module=(Element)moduleList.get(i);

					// String moduleName=module.attributeValue("name");
					String moduleLang=module.attributeValue("lang");
					// if(!moduleName.endsWith(".")) moduleName+=".";
					moduleLang=I18nBO.reviseLanguage(moduleLang);

					List msgList=module.elements("msg");
					if(msgList!=null&&msgList.size()>0)
					{
						for(int j=0;j<msgList.size();j++)
						{
							Element msg=(Element)msgList.get(j);

							// 把国际化信息存入Map中，语言和代码间用\n分隔
							// String name=moduleName+msg.attributeValue("name");
							String name=msg.attributeValue("name");
							String value=msg.getText();
							messages.put(moduleLang+"\n"+name,value);
						}
					}
				}
			}
		}
		catch(DocumentException e)
		{
			e.printStackTrace();
		}

		return messages;
	}

	/**
	 * 修正语言编码为标准的编码
	 * 
	 * @param language
	 * @return
	 */
	public static String reviseLanguage(String language)
	{
		if(language!=null)
		{
			for(int i=0;i<cnByname.length;i++)
			{
				if(language.toLowerCase().indexOf(cnByname[i])>-1) return "cn";
			}

			for(int i=0;i<enByname.length;i++)
			{
				if(language.toLowerCase().indexOf(enByname[i])>-1) return "en";
			}
		}

		return language;
	}
}
