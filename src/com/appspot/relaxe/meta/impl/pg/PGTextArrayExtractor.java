/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.meta.impl.pg;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.ValueExtractor;
import com.appspot.relaxe.env.pg.PGTextArrayHolder;
import com.appspot.relaxe.env.pg.PGTextArrayType;
import com.appspot.relaxe.rpc.StringArray;


class PGTextArrayExtractor
	extends ValueExtractor<StringArray, PGTextArrayType, PGTextArrayHolder> {

	public PGTextArrayExtractor(int column) {
		super(column);
	}

	@Override
	public PGTextArrayHolder extractValue(ResultSet rs) throws SQLException {
		Array a = rs.getArray(getColumn());
		
		if (a == null) {
			return PGTextArrayHolder.NULL_HOLDER;
		}
		
		String[] ac = (String[]) a.getArray();
		StringArray sa = new StringArray(ac);
		return PGTextArrayHolder.newHolder(sa);
	}
}