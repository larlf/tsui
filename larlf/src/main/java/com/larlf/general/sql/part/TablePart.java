package com.larlf.general.sql.part;

import org.apache.commons.lang3.StringUtils;

import com.larlf.general.sql.BasePart;
import com.larlf.general.sql.ISql;
import com.larlf.general.sql.SQL;
import com.larlf.general.sql.SqlUtils;

public class TablePart extends BasePart
{
	private String table;
	private String alias;

	public TablePart(String table, String alias)
	{
		this.table = table;
		this.alias = alias;
	}
	
	public TablePart(String table)
	{
		this(table, null);
	}

	@Override
	public String sql(ISql parent, String sql, String partSql)
	{
		if (sql.indexOf(SQL.KEY_SELECT) == 0 || sql.indexOf(SQL.KEY_DELETE) == 0)
		{
			if(StringUtils.isBlank(partSql)) partSql+=SQL.KEY_FROM+" ";
			else partSql+=",";
		}
		
		partSql+=SqlUtils.column(this.table);
		if(StringUtils.isNotBlank(this.alias)) partSql+=" "+SqlUtils.column(this.alias);

		return partSql;
	}

	@Override
	public int part()
	{
		return SQL.PART_TABLE;
	}

}
