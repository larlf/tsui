package com.larlf.general.sql;

import com.larlf.general.sql.cond.ANDGroup;
import com.larlf.general.sql.cond.EQCond;
import com.larlf.general.sql.cond.ORGroup;
import com.larlf.general.sql.impl.DeleteSql;
import com.larlf.general.sql.impl.InsertSql;
import com.larlf.general.sql.impl.SelectSql;
import com.larlf.general.sql.impl.UpdateSql;

/**
 * 一些处理的快捷方式
 * 
 * @author Larlf
 * 
 */
public class SQL
{

	// 关键字
	static public String KEY_SELECT = "SELECT";
	static public String KEY_INSERT = "INSERT INTO";
	static public String KEY_UPDATE = "UPDATE";
	static public String KEY_DELETE = "DELETE";
	static public String KEY_WHERE = "WHERE";
	static public String KEY_AND = "AND";
	static public String KEY_OR = "OR";
	static public String KEY_ORDER_BY = "ORDER BY";
	static public String KEY_ASC = "ASC";
	static public String KEY_DESC = "DESC";
	static public String KEY_IS_NULL = "IS NULL";
	static public String KEY_IS_NOT_NULL = "IS NOT NULL";
	static public String KEY_LIMIT = "LIMIT";
	static public String KEY_FROM = "FROM";

	// 条件中的操作符
	static public String OP_EQ = "=";
	static public String OP_NE = "<>";
	static public String OP_LT = "<";
	static public String OP_LE = "<=";
	static public String OP_GT = ">";
	static public String OP_GE = ">=";
	static public String OP_LIKE = "LIKE";

	// 各部分的代码，拼接的时候按这个排序
	static public int PART_KEY = 1;
	static public int PART_COLUMN = 2;
	static public int PART_TABLE = 3;
	static public int PART_VALUE = 4;
	static public int PART_WHERE = 5;
	static public int PART_SORT = 6;
	static public int PART_LIMIT = 7;

	static public SelectSql Select()
	{
		return new SelectSql();
	}

	static public SelectSql Select(String table)
	{
		return new SelectSql().table(table);
	}
	
	static public SelectSql Select(String table, Object id)
	{
		return new SelectSql().table(table).where("id", id);
	}

	static public InsertSql Insert(String table)
	{
		return new InsertSql().table(table);
	}

	static public UpdateSql Update(String table)
	{
		return new UpdateSql().table(table);
	}
	
	static public UpdateSql Update(String table, Object id)
	{
		return new UpdateSql().table(table).where("id", id);
	}

	static public DeleteSql Delete(String table)
	{
		return new DeleteSql().table(table);
	}

	static public DeleteSql Delete(String table, String id)
	{
		return new DeleteSql().table(table).where("id", id);
	}

	static public EQCond EQ(String name, Object value)
	{
		return new EQCond(name, value);
	}

	static public ANDGroup AND(ISqlCond... conds)
	{
		return new ANDGroup(conds);
	}

	static public ORGroup OR(ISqlCond... conds)
	{
		return new ORGroup(conds);
	}

}
