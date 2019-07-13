package com.larlf.general.ajax;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class AjaxUtils
{
	static Logger log=Logger.getLogger(AjaxUtils.class);

	static Map<String,Boolean> hiddenMethods=new HashMap<String,Boolean>(); // 不用生成JS代码的方法，只在JDK1.4下用

	static
	{
		hiddenMethods.put("hasCode",true);
		hiddenMethods.put("wait",true);
		hiddenMethods.put("notifyAll",true);
		hiddenMethods.put("notify",true);
		hiddenMethods.put("equals",true);
		hiddenMethods.put("toString",true);
	}

	static public String class2js(Class<?> clazz,String basePath)
	{
		StringBuffer sb=new StringBuffer();

		String jsName=StringUtils.replace(clazz.getName(),".","_");
		sb.append("function "+jsName+"()\n{\n");

		sb.append("this.url='"+basePath+clazz.getSimpleName()+".ajax';\n\n");

		Method[] ms=clazz.getMethods();
		if(ms!=null&&ms.length>0)
		{
			for(int i=0;i<ms.length;i++)
			{
				Method m=ms[i];
				
				Ajax ajax=m.getAnnotation(Ajax.class);
				if(ajax!=null)
				{
					sb.append("this."+m.getName()+"=function(");
					String pTypes="";
					int pCount=0;

					Class<?>[] ps=m.getParameterTypes();
					if(ps!=null&&ps.length>0)
					{
						for(int j=0;j<ps.length;j++)
						{
							sb.append("p"+j+",");
							pTypes+=ps[j].getName();
							pCount++;
							if(j<ps.length-1) pTypes+=",";
						}
					}

					sb.append("callback,args)\n");
					sb.append("{\n");
					sb.append("var method='"+m.getName()+"';\n");
					sb.append("var pTypes='"+pTypes+"';\n");
					sb.append("var pCount="+pCount+";\n");
					if(ajax.forward())
						sb.append("var forward=true;\n");
					else
						sb.append("var forward=false;\n");

					sb.append("var ajax=new OpeAjaxRequest(this.url,method,pTypes,pCount,forward);\n");
					sb.append("return ajax.execute(arguments);\n");
					sb.append("}\n\n");
				}
			}
		}

		sb.append("}");

		sb.append("\n\nvar "+clazz.getSimpleName()+" = new "+jsName+"();");

		return sb.toString();
	}
}
