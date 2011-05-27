/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.types.TimeType;

class TimeExtractor
	extends ValueExtractor<Date, TimeType, TimeHolder>
{
	public TimeExtractor(int column) {
		super(column);			
	}
	
	@Override
	public TimeHolder extractValue(ResultSet rs) throws SQLException {
		Date date = rs.getDate(getColumn());		
		return TimeHolder.valueOf(date);
	}
}