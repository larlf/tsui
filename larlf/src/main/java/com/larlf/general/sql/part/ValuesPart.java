package com.larlf.general.sql.part;

import java.util.ArrayList;
import java.util.List;

import com.larlf.general.sql.BasePart;
import com.larlf.general.sql.ISql;
import com.larlf.general.sql.SQL;
import com.larlf.general.sql.SqlUtils;
import com.larlf.general.sql.SqlValue;

public class ValuesPart extends BasePart
{
	List<SqlValue> values = new ArrayList<SqlValue>();

	public ValuesPart()
	{

	}

	public void addValue(String column, Object value)
	{
		for (SqlValue it : this.values)
		{
			if (it.column == column)
			{
				it.value = value;
				return;
			}
		}

		this.values.add(new SqlValue(column, value));
	}

	@Override
	public String sql(ISql parent, String sql, String partSql)
	{
		if (sql.indexOf(SQL.KEY_INSERT) == 0)
		{
			StringBuilder keys = new StringBuilder();
			StringBuilder values = new StringBuilder();

			if (this.values.size() > 0)
			{
				for (SqlValue it : this.values)
				{
					if (keys.length() > 0)
						keys.append(",");
					keys.append(SqlUtils.column(it.column));

					if (values.length() > 0)
						values.append(",");
					values.append(SqlUtils.value(it.value));
				}
			}

			return "(" + keys.toString() + ") VALUES (" + values.toString() + ")";
		}
		else if (sql.indexOf(SQL.KEY_UPDATE) == 0)
		{
			StringBuilder str = new StringBuilder();

			for (SqlValue it : this.values)
			{
				if (str.length() > 0)
					str.append(",");
				str.append(SqlUtils.column(it.column));
				str.append("=");
				str.append(SqlUtils.value(it.value));
			}

			return "SET " + str.toString();
		}

		return null;
	}

	@Override
	public int part()
	{
		return SQL.PART_VALUE;
	}

}
