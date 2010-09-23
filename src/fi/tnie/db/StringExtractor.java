/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

class StringExtractor
	extends ValueExtractor
{
	public StringExtractor(int column) {
		super(column);			
	}
	
	@Override
	public StringHolder extractValue(ResultSet rs) throws SQLException {
		String s = rs.getString(getColumn());
		return (s == null) ? StringHolder.NULL_HOLDER : new StringHolder(s);
	}
}