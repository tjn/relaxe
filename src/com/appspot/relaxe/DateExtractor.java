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

import com.appspot.relaxe.rpc.DateHolder;
import com.appspot.relaxe.types.DateType;


class DateExtractor
	extends ValueExtractor<Date, DateType, DateHolder>
{
	public DateExtractor(int column) {
		super(column);			
	}
	
	@Override
	public DateHolder extractValue(ResultSet rs) throws SQLException {
		Date date = rs.getDate(getColumn());
		return (date == null) ? DateHolder.NULL_HOLDER : new DateHolder(date);
	}
}