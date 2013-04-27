/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.BooleanHolder;
import com.appspot.relaxe.types.BooleanType;


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
