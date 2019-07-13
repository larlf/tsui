package com.larlf.general.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class KeyUtils
{
	/**
	 * 生成md5字符串
	 * @param str
	 * @return
	 */
	static public String md5(String str)
	{
		return DigestUtils.md5Hex(str);
	}
	
	/**
	 * 取得时间戳
	 * @return
	 */
	static public int time()
	{
		return (int)TimeUtils.timeStamp();
	}
}
