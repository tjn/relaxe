/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fi.tnie.db.rpc.BooleanHolder;
import fi.tnie.db.types.BooleanType;

public class BooleanAssignment
	extends AbstractParameterAssignment<Boolean, BooleanType, BooleanHolder> {

	public BooleanAssignment(BooleanHolder value) {
		super(value);
	}

	@Override
	public void assign(PreparedStatement ps, int ordinal, Boolean newValue) 
		throws SQLException {
		ps.setBoolean(ordinal, newValue.booleanValue());
	}
}
