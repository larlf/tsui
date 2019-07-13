package com.larlf.general.sql.cond;

import com.larlf.general.sql.SQL;

public class LikeCond extends BaseCond {

	public LikeCond(String name, Object value) {
		super(name, value, SQL.OP_LIKE);
	}

}
