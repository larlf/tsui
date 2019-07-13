package com.larlf.general.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

public class FileUtils
{
	static Logger log=Logger.getLogger(FileUtils.class);
	
	/**
	 * 在指定的位置查找文件
	 * @param url
	 * @param startStr
	 * @param endStr
	 * @return
	 */
	static public String[] findFiles(String url,String startStr,String endStr)
	{
		String[] result=new String[0];
		File path=new File(url);

		if(path!=null)
		{
			String[] files=path.list();
			if(files!=null&&files.length>0)
			{
				for(int i=0;i<files.length;i++)
				{
					if(files[i].startsWith(startStr)&&files[i].endsWith(endStr)) result=(String[])ArrayUtils.add(result,path.getPath()+"/"+files[i]);
				}
			}
		}

		return result;
	}
	
	/**
	 * 读取文件的内容
	 * @param filename
	 * @return
	 */
	static public String readFile(File file)
	{
		String text="";
		
		try
		{
//			BufferedReader in=new BufferedReader(new FileReader(file));
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String line;
			while((line=in.readLine())!=null)
			{
				text+=line;
			}
			
			in.close();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		
		return text;
	}
	
	static public String readFile(URL url)
	{
		try
		{
			File file = new File(url.toURI());
			if(!file.exists())
				log.error("Can't find file : "+url);
			else
				return readFile(file);			
		}
		catch(URISyntaxException e)
		{
			log.error(e);
		}

		return "";
	}
}
