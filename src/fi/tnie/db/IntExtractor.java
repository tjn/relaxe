/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

class IntExtractor
	extends ValueExtractor
{
	public IntExtractor(int column) {
		super(column);			
	}

	@Override
	public IntHolder extractValue(ResultSet rs) throws SQLException {
		int v = rs.getInt(getColumn());
		return rs.wasNull() ? IntHolder.NULL_HOLDER : IntHolder.valueOf(v);			
	}
}