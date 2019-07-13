package com.larlf.general.sql.cond;

import com.larlf.general.sql.ISqlCond;
import com.larlf.general.sql.SQL;

public class ANDGroup implements ISqlCond
{
	ISqlCond[] conds;

	public ANDGroup(ISqlCond... conds)
	{
		this.conds = conds;
	}

	@Override
	public String sql()
	{
		StringBuilder str = new StringBuilder();

		for (ISqlCond cond : this.conds)
		{
			String condStr = cond.sql();

			if (condStr != null && condStr.length() > 0)
			{
				if (str.length() > 0)
					str.append(" " + SQL.KEY_AND + " ");

				str.append(condStr);
			}
		}

		if (str.length() > 0)
			return "(" + str.toString() + ")";

		return null;
	}
}
