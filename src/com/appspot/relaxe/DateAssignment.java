/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.appspot.relaxe.rpc.DateHolder;
import com.appspot.relaxe.types.DateType;


public class DateAssignment
	extends AbstractDateAssignment<DateType, DateHolder> {

	public DateAssignment(DateHolder value) {
		super(value);
	}

	@Override
	public void assign(PreparedStatement ps, int ordinal, Date newValue)
			throws SQLException {
		ps.setDate(ordinal, new java.sql.Date(newValue.getTime()), getCalendar());
	}

}
