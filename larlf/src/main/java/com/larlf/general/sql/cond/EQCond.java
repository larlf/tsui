package com.larlf.general.sql.cond;

import com.larlf.general.sql.SQL;

public class EQCond extends BaseCond
{
	public EQCond(String name, Object value)
	{
		super(name, value, SQL.OP_EQ);
	}
}
