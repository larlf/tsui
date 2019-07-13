package com.larlf.general.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public abstract class BaseSql implements ISql
{
	Logger log = Logger.getLogger(this.getClass());

	private List<ISqlPart> parts = new ArrayList<ISqlPart>();
	private int _index = 0;

	public BaseSql part(ISqlPart part)
	{
		// 设置顺序
		((BasePart) part)._index = ++_index;

		this.parts.add(part);
		return this;
	}

	@Override
	public String sql()
	{
		StringBuilder sql = new StringBuilder();
		String partSql = "";
		int level = 0;

		// 对条件进行排序
		Collections.sort(parts, new Comparator<ISqlPart>()
		{
			public int compare(ISqlPart a, ISqlPart b)
			{
				if (a.part() != b.part())
					return a.part() - b.part();
				else
					return a.index() - b.index();
			}
		});

		for (ISqlPart part : parts)
		{
			//上一部分构造完了
			if (part.part() != level)
			{
				if(StringUtils.isNotBlank(partSql))
				{
					if (sql.length() > 0 && sql.charAt(sql.length() - 1) != ' ')
						sql.append(" ");
					sql.append(partSql);
				}
				
				//重置，处理下一部分
				partSql="";
				level = part.part();
			}
			
			//构造SQL
			partSql = part.sql(this, sql.toString(), partSql);
		}
		
		//处理最后一部分
		if(StringUtils.isNotBlank(partSql))
		{
			if (sql.length() > 0 && sql.charAt(sql.length() - 1) != ' ')
				sql.append(" ");
			sql.append(partSql);
		}

		log.debug("SQL : " + sql.toString());
		return sql.toString();
	}

	@Override
	public String toString()
	{
		return this.sql();
	}

}
