package com.larlf.general.sql.impl;

import java.util.Map;

import com.larlf.general.sql.BaseSql;
import com.larlf.general.sql.SQL;
import com.larlf.general.sql.part.TablePart;
import com.larlf.general.sql.part.TextPart;
import com.larlf.general.sql.part.ValuesPart;

public class InsertSql extends BaseSql
{
	private ValuesPart values=new ValuesPart();

	public InsertSql()
	{
		this.part(new TextPart(SQL.KEY_INSERT, SQL.PART_KEY));
	}
	
	public InsertSql table(String table)
	{
		this.part(new TablePart(table));
		return this;
	}
	
	public InsertSql value(String column, Object value)
	{
		this.values.addValue(column, value);
		return this;
	}
	
	public InsertSql values(Map map)
	{
		for(Object key : map.keySet())
		{
			this.value(key.toString(), map.get(key));
		}
			
		return this;
	}
	
	@Override
	public String sql()
	{
		this.part(this.values);
		return super.sql();
	}
	
}
