package com.larlf.general.sql;

/**
 * 所有Part的基类
 * 
 * @author Larlf
 * 
 */
public abstract class BasePart implements ISqlPart
{
	protected int _index;

	@Override
	public int index()
	{
		return this._index;
	}

}
