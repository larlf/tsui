package com.larlf.general.sql.part;

import org.apache.commons.lang3.StringUtils;

import com.larlf.general.sql.BasePart;
import com.larlf.general.sql.ISql;
import com.larlf.general.sql.SQL;
import com.larlf.general.sql.SqlUtils;

public class ColumnPart extends BasePart
{
	private String name; // 名称
	private String alias; // 别名

	public ColumnPart(String name, String alias)
	{
		this.name = name;
		this.alias = alias;
	}

	public ColumnPart(String name)
	{
		this(name, null);
	}

	@Override
	public int part()
	{
		return SQL.PART_COLUMN;
	}

	@Override
	public String sql(ISql parent, String sql, String partSql)
	{
		String funName = null;
		String columnName = null;
		String tableName = null;

		if (this.name != null)
		{
			// 处理函数
			if (this.name.indexOf("(") >= 0 && this.name.indexOf(")") >= 0) // 如果是函数
			{
				funName = this.name.substring(0, this.name.indexOf("("));
				columnName = this.name.substring(this.name.indexOf("(") + 1, this.name.lastIndexOf(")"));
			}
			else
				columnName = this.name;

			// 处理有表名的情况
			int pos = columnName.indexOf(".");
			if (pos >= 0)
			{
				tableName = columnName.substring(0, pos);
				columnName = columnName.substring(pos + 1, columnName.length());
			}

			// 构造字段名
			String str = "";
			if (StringUtils.isNotBlank(columnName))
			{
				str = SqlUtils.column(columnName.trim());
				if (StringUtils.isNotBlank(tableName))
				{
					str = SqlUtils.column(tableName.trim()) + "." + str;
				}
			}

			// 加入函数
			if (StringUtils.isNotBlank(funName))
				str = funName.toUpperCase().trim() + "(" + str + ")";

			// 加入别名
			if (StringUtils.isNotBlank(this.alias))
				str += " AS " + SqlUtils.column(this.alias.trim());

			// 生成最终值
			if (partSql.length() > 0)
				partSql += ",";
			return partSql += str;
		}

		return partSql;
	}

}
