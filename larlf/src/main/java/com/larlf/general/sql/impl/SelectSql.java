package com.larlf.general.sql.impl;

import org.apache.commons.lang3.StringUtils;

import com.larlf.general.sql.BaseSql;
import com.larlf.general.sql.ISqlCond;
import com.larlf.general.sql.SQL;
import com.larlf.general.sql.cond.BaseCond;
import com.larlf.general.sql.cond.EQCond;
import com.larlf.general.sql.part.ColumnPart;
import com.larlf.general.sql.part.LimitPart;
import com.larlf.general.sql.part.OrderByPart;
import com.larlf.general.sql.part.TablePart;
import com.larlf.general.sql.part.TextPart;
import com.larlf.general.sql.part.WherePart;

public class SelectSql extends BaseSql
{
	public SelectSql()
	{
		this.part(new TextPart(SQL.KEY_SELECT, SQL.PART_KEY));
	}

	public SelectSql count(String name)
	{
		this.part(new ColumnPart("count("+name+")"));
		return this;
	}

	public SelectSql count()
	{
		this.part(new ColumnPart("count(*)"));
		return this;
	}

	/**
	 * 添加多个字段
	 * 
	 * @param columns
	 *            例："aaa,bbb,ccc|c"
	 * @return
	 */
	public SelectSql columns(String columns)
	{
		if (StringUtils.isNotBlank(columns))
		{
			String[] list = columns.split(",");
			for (String name : list)
			{
				if (StringUtils.isNotBlank(name))
				{
					String alias = null;

					// 查找有没有分隔符
					if (name.indexOf("|") >= 0)
					{
						int pos = name.indexOf("|");
						alias = name.substring(pos + 1, name.length());
						name = name.substring(0, pos);
					}
					else if (name.indexOf(":") >= 0)
					{
						int pos = name.indexOf(":");
						alias = name.substring(pos + 1, name.length());
						name = name.substring(0, pos);
					}
					else if (name.toLowerCase().indexOf(" as ") >= 0)
					{
						int pos = name.toLowerCase().indexOf(" as ");
						alias = name.substring(pos + 4, name.length());
						name = name.substring(0, pos);
					}

					// 如果有分隔符
					if (StringUtils.isNotBlank(alias))
						this.part(new ColumnPart(name, alias));
					else
						this.part(new ColumnPart(name));
				}
			}
		}

		return this;
	}

	public SelectSql table(String table)
	{
		this.part(new TablePart(table));
		return this;
	}

	public SelectSql where(String name, Object value)
	{
		this.part(new WherePart(new EQCond(name, value)));
		return this;
	}

	public SelectSql where(String name, Object value, String op)
	{
		this.part(new WherePart(new BaseCond(name, value, op)));
		return this;
	}

	public SelectSql where(ISqlCond... conds)
	{
		this.part(new WherePart(conds));
		return this;
	}

	public SelectSql sort(String name, boolean isAsc)
	{
		this.part(new OrderByPart(name, isAsc));
		return this;
	}

	public SelectSql sort(String name, String order)
	{
		this.part(new OrderByPart(name, "DESC".equalsIgnoreCase(order) ? false : true));
		return this;
	}

	public SelectSql sort(String name)
	{
		this.part(new OrderByPart(name, true));
		return this;
	}

	public SelectSql limit(int limit, int start)
	{
		this.part(new LimitPart(limit, start));
		return this;
	}

	public SelectSql limit(int limit)
	{
		this.part(new LimitPart(limit, 0));
		return this;
	}
}
