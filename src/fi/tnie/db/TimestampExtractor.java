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

import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.types.TimestampType;

class TimestampExtractor
	extends ValueExtractor<Date, TimestampType>
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