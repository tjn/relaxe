/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.DoubleHolder;
import com.appspot.relaxe.types.DoubleType;


public class DoubleExtractor
	extends ValueExtractor<Double, DoubleType, DoubleHolder>
{
	public DoubleExtractor(int column) {
		super(column);			
	}

	@Override
	public DoubleHolder extractValue(ResultSet rs) throws SQLException {
		double v = rs.getDouble(getColumn());
		return rs.wasNull() ? DoubleHolder.NULL_HOLDER : DoubleHolder.valueOf(v);			
	}
	
}