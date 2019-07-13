package com.larlf.general.core.exception;

/**
 * 用于显示错误信息的异常，通常不用再处理，直接可以用于显示给用户
 * 
 * @author Larlf
 * 
 */
public class MessageException extends Exception
{
	public MessageException(String msg)
	{

		super(msg);
	}

	public MessageException(Throwable cause,String msg)
	{
		super(msg);
		super.initCause(cause);
	}
}
