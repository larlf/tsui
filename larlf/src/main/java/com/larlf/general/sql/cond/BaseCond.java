package com.larlf.general.sql.cond;

import com.larlf.general.sql.ISqlCond;
import com.larlf.general.sql.SQL;
import com.larlf.general.sql.SqlUtils;

public class BaseCond implements ISqlCond
{
	private String name;
	private Object value;
	private String op;
	
	public BaseCond(String name, Object value, String op)
	{
		this.name=name;
		this.value=value;
		this.op=op;
	}
	
	public BaseCond(String name, Object value)
	{
		this.name=name;
		this.value=value;
		this.op=SQL.OP_EQ;
	}

	@Override
	public String sql()
	{
		StringBuilder str=new StringBuilder();
		
		if(this.name!=null)
		{	
			str.append(this.name);
			
			if(this.value==null)
			{
				if(this.op==SQL.OP_NE)
					str.append(" "+SQL.KEY_IS_NOT_NULL);
				else
					str.append(" "+SQL.KEY_IS_NULL);
			}
			else if("like".equalsIgnoreCase(op))
			{
				str.append(" LIKE ");
				str.append("'"+this.value+"'");
			}
			else
			{
				str.append(this.op);
				str.append(SqlUtils.value(this.value));
			}
		}
		
		return str.toString();
	}

}
