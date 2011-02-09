/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.types.IntegerType;

public class IntegerAssignment
	extends AbstractParameterAssignment<Integer, IntegerType, IntegerHolder> {

	public IntegerAssignment(IntegerHolder value) {
		super(value);
	}

	@Override
	public void assign(PreparedStatement ps, int ordinal, Integer newValue) 
		throws SQLException {
		ps.setInt(ordinal, newValue.intValue());
	}
}
