package com.larlf.general.sql.impl;

import java.util.Map;

import com.larlf.general.sql.BaseSql;
import com.larlf.general.sql.ISqlCond;
import com.larlf.general.sql.SQL;
import com.larlf.general.sql.cond.EQCond;
import com.larlf.general.sql.part.TablePart;
import com.larlf.general.sql.part.TextPart;
import com.larlf.general.sql.part.ValuesPart;
import com.larlf.general.sql.part.WherePart;

public class UpdateSql extends BaseSql
{
	private ValuesPart values = new ValuesPart();

	public UpdateSql()
	{
		this.part(new TextPart(SQL.KEY_UPDATE, SQL.PART_KEY));
	}

	public UpdateSql table(String table)
	{
		this.part(new TablePart(table));
		this.part(values);
		return this;
	}

	public UpdateSql value(String column, Object value)
	{
		this.values.addValue(column, value);
		return this;
	}

	public UpdateSql values(Map map)
	{
		for (Object key : map.keySet())
		{
			this.value(key.toString(), map.get(key));
		}

		return this;
	}
	
	public UpdateSql where(String name, Object value)
	{
		this.part(new WherePart(new EQCond(name, value)));
		return this;
	}
	
	public UpdateSql where(ISqlCond ... conds)
	{
		this.part(new WherePart(conds));
		return this;
	}
}
