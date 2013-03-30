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

public class IntegerExtractor
	extends ValueExtractor<Integer, IntegerType, IntegerHolder>
{	
	private static final IntegerExtractor[] PERMANENT = new IntegerExtractor[10];
	
	public static IntegerExtractor forColumn(int column) {
		int index = column - 1;
		
		IntegerExtractor e = (index >= 0 && index < PERMANENT.length) ? PERMANENT[index] : new IntegerExtractor(column);
		
		if (e == null) {
			PERMANENT[index] = e = new IntegerExtractor(column);			
		}
		
		return e;		
	}
	
	public IntegerExtractor(int column) {
		super(column);			
	}

	@Override
	public IntegerHolder extractValue(ResultSet rs) throws SQLException {
		int v = rs.getInt(getColumn());
		return rs.wasNull() ? IntegerHolder.NULL_HOLDER : IntegerHolder.valueOf(v);			
	}
}