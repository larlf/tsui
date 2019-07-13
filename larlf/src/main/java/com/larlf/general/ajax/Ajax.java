package com.larlf.general.ajax;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Ajax
{

	public enum RunType
	{
		STATIC, 	// 静态方法的方式来运行
		NEW, 		// 新建一个对象的方式来运行
		POOL 		// 从池中取一个对象（可以重复利用）
	}

	RunType type() default RunType.NEW;

	boolean forward() default false; // 返回值是一个URI
}
