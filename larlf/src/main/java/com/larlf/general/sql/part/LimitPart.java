package com.larlf.general.sql.part;

import com.larlf.general.sql.BasePart;
import com.larlf.general.sql.ISql;
import com.larlf.general.sql.SQL;

public class LimitPart extends BasePart
{
	private int limit;
	private int start;

	public LimitPart(int limit, int start)
	{
		this.limit = limit;
		this.start = start;
	}

	@Override
	public String sql(ISql parent, String sql, String partSql)
	{
		if (start > 0)
			return SQL.KEY_LIMIT + " " + this.start + "," + this.limit;
		else
			return SQL.KEY_LIMIT + " " + this.limit;
	}

	@Override
	public int part()
	{
		return SQL.PART_LIMIT;
	}

}
