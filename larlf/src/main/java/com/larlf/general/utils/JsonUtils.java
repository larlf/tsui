package com.larlf.general.utils;

import com.google.gson.Gson;

public class JsonUtils
{
	static public String toJson(Object obj)
	{
		Gson gson=new Gson();
		return gson.toJson(obj);
	}
	
	static public Object fromJson(String json)
	{
		Gson gson=new Gson();
		return gson.fromJson(json, Object.class);
	}
}
