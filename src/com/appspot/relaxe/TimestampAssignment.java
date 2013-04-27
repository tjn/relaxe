/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.appspot.relaxe.rpc.TimestampHolder;
import com.appspot.relaxe.types.TimestampType;


public class TimestampAssignment
	extends AbstractDateAssignment<TimestampType, TimestampHolder> {

	public TimestampAssignment(TimestampHolder value) {
		super(value);
	}
	
	@Override
	public void assign(PreparedStatement ps, int ordinal, Date newValue)
			throws SQLException {
		ps.setTimestamp(ordinal, new java.sql.Timestamp(newValue.getTime()), getCalendar());		
	}
}
