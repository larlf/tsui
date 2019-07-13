package com.larlf.general.sql;

public interface ISqlPart
{
	int part();
	int index();
	String sql(ISql parent, String sql, String partSql);
}
