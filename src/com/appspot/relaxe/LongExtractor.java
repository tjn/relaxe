/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.LongHolder;
import com.appspot.relaxe.types.LongType;


public class LongExtractor
	extends ValueExtractor<Long, LongType, LongHolder>
{
	public LongExtractor(int column) {
		super(column);			
	}

	@Override
	public LongHolder extractValue(ResultSet rs) throws SQLException {
		long v = rs.getLong(getColumn());
		return rs.wasNull() ? LongHolder.NULL_HOLDER : LongHolder.valueOf(v);			
	}	
}