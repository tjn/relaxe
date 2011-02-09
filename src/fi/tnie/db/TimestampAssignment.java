/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.types.TimestampType;

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
