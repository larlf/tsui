package com.larlf.general.sql.part;

import org.apache.commons.lang3.StringUtils;

import com.larlf.general.sql.BasePart;
import com.larlf.general.sql.ISql;
import com.larlf.general.sql.SQL;
import com.larlf.general.sql.SqlUtils;

public class OrderByPart extends BasePart
{
	private String name;
	private boolean isAsc = true;

	public OrderByPart(String name, boolean isAsc)
	{
		this.name = name;
		this.isAsc = isAsc;
	}

	@Override
	public String sql(ISql parent, String sql, String partSql)
	{
		if (this.name != null)
		{
			if (StringUtils.isEmpty(partSql))
				partSql = SQL.KEY_ORDER_BY + " ";
			else
				partSql += ",";

			partSql += SqlUtils.column(this.name);
			if (!this.isAsc)
				partSql += " " + SQL.KEY_DESC;
		}

		return partSql;
	}

	@Override
	public int part()
	{
		return SQL.PART_SORT;
	}

}
