/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.appspot.relaxe.ent.value.LongVarBinary;
import com.appspot.relaxe.rpc.LongVarBinaryHolder;
import com.appspot.relaxe.types.LongVarBinaryType;


public class LongVarBinaryAssignment
	extends AbstractParameterAssignment<LongVarBinary, LongVarBinaryType, LongVarBinaryHolder> {

	public LongVarBinaryAssignment(LongVarBinaryHolder value) {
		super(value);
	}
	
	@Override
	public void assign(PreparedStatement ps, int ordinal, LongVarBinary newValue) throws SQLException {
		// TODO: do not copy arrays
		ps.setBytes(ordinal, newValue.toArray());
	}
}
