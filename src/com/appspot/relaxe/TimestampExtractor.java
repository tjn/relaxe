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

import com.appspot.relaxe.rpc.TimestampHolder;
import com.appspot.relaxe.types.TimestampType;


class TimestampExtractor
	extends ValueExtractor<Date, TimestampType, TimestampHolder>
{
	public TimestampExtractor(int column) {
		super(column);			
	}
	
	@Override
	public TimestampHolder extractValue(ResultSet rs) throws SQLException {
		Date date = rs.getDate(getColumn());
		return (date == null) ? TimestampHolder.NULL_HOLDER : new TimestampHolder(date);
	}
}