package com.larlf.general.sql.part;

import org.apache.commons.lang3.StringUtils;

import com.larlf.general.sql.BasePart;
import com.larlf.general.sql.ISql;
import com.larlf.general.sql.ISqlCond;
import com.larlf.general.sql.SQL;

public class WherePart extends BasePart
{
	ISqlCond[] conds;

	public WherePart(ISqlCond... conds)
	{
		this.conds = conds;
	}

	@Override
	public String sql(ISql parent, String sql, String partSql)
	{
		StringBuilder str = new StringBuilder();

		if (this.conds.length > 0)
		{
			if (StringUtils.isBlank(partSql))
				partSql = SQL.KEY_WHERE + " ";
			else
				partSql += " " + SQL.KEY_AND + " ";

			for (ISqlCond cond : this.conds)
			{
				if (str.length() > 0)
					str.append(SQL.KEY_AND + " ");
				str.append(cond.sql());
			}
		}

		return partSql + str.toString();
	}

	@Override
	public int part()
	{
		return SQL.PART_WHERE;
	}

}
