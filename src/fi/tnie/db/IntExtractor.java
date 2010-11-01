/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.types.IntegerType;

class IntExtractor
	extends ValueExtractor<Integer, IntegerType>
{
	public IntExtractor(int column) {
		super(column);			
	}

	@Override
	public IntegerHolder extractValue(ResultSet rs) throws SQLException {
		int v = rs.getInt(getColumn());
		return rs.wasNull() ? IntegerHolder.NULL_HOLDER : IntegerHolder.valueOf(v);			
	}
}