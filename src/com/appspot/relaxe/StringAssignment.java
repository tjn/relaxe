/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;


import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;

public class StringAssignment<T extends PrimitiveType<T>, H extends PrimitiveHolder<String, T, H>>	
	extends AbstractParameterAssignment<String, T, H> {

	public StringAssignment(H value) {
		super(value);
	}

	@Override
	public void assign(PreparedStatement ps, int ordinal, String newValue) 
		throws SQLException {
		ps.setString(ordinal, newValue);
	}
}
