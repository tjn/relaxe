/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.TimeHolder;
import com.appspot.relaxe.types.TimeType;


class TimeExtractor
	extends ValueExtractor<Date, TimeType, TimeHolder>
{
	public TimeExtractor(int column) {
		super(column);			
	}
	
	@Override
	public TimeHolder extractValue(ResultSet rs) throws SQLException {
		Date date = rs.getTime(getColumn());		
		return TimeHolder.valueOf(date);
	}
}