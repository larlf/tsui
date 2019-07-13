package com.larlf.general.sql.part;

import com.larlf.general.sql.BasePart;
import com.larlf.general.sql.ISql;

public class TextPart extends BasePart
{
	private String text;
	private int _level;

	public TextPart(String text, int level)
	{
		this.text = text;
		this._level = level;
	}

	@Override
	public String sql(ISql parent, String sql, String partSql)
	{
		if(partSql.length()>0) partSql+=" ";
		return partSql+this.text;
	}

	@Override
	public int part()
	{
		return this._level;
	}

}
