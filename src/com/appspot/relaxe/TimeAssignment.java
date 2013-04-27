/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

//import com.appspot.relaxe.rpc.TimeHolder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.appspot.relaxe.rpc.TimeHolder;
import com.appspot.relaxe.types.TimeType;


public class TimeAssignment
	extends AbstractDateAssignment<TimeType, TimeHolder> {

	public TimeAssignment(TimeHolder value) {
		super(value);
	}
	
	@Override
	public void assign(PreparedStatement ps, int ordinal, Date newValue)
			throws SQLException {
		ps.setTime(ordinal, new java.sql.Time(newValue.getTime()), getCalendar());
	}
}
