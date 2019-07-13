package com.larlf.general.sql.impl;

import com.larlf.general.sql.BaseSql;
import com.larlf.general.sql.ISqlCond;
import com.larlf.general.sql.SQL;
import com.larlf.general.sql.cond.EQCond;
import com.larlf.general.sql.part.TablePart;
import com.larlf.general.sql.part.TextPart;
import com.larlf.general.sql.part.WherePart;

public class DeleteSql extends BaseSql
{
	public DeleteSql()
	{
		this.part(new TextPart(SQL.KEY_DELETE, SQL.PART_KEY));
	}
	
	public DeleteSql table(String table)
	{
		this.part(new TablePart(table));
		return this;
	}
	
	public DeleteSql where(String name, Object value)
	{
		this.part(new WherePart(new EQCond(name, value)));
		return this;
	}
	
	public DeleteSql where(ISqlCond ... conds)
	{
		this.part(new WherePart(conds));
		return this;
	}
}
