/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.types.DoubleType;

public class DoubleExtractor
	extends ValueExtractor<Double, DoubleType, DoubleHolder>
{
	public DoubleExtractor(int column) {
		super(column);			
	}

	@Override
	public DoubleHolder extractValue(ResultSet rs) throws SQLException {
		int v = rs.getInt(getColumn());
		return rs.wasNull() ? DoubleHolder.NULL_HOLDER : DoubleHolder.valueOf(v);			
	}
	
}