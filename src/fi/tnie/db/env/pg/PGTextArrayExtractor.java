/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.env.pg;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.ValueExtractor;
import fi.tnie.db.rpc.StringArray;

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