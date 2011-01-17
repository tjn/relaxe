/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.types.CharType;

class CharExtractor
	extends ValueExtractor<String, CharType>
{
	public CharExtractor(int column) {
		super(column);			
	}
	
	@Override
	public CharHolder extractValue(ResultSet rs) throws SQLException {
		String s = rs.getString(getColumn());
		return (s == null) ? CharHolder.NULL_HOLDER : new CharHolder(s);
	}
}