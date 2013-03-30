/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fi.tnie.db.ent.value.LongVarBinary;
import fi.tnie.db.rpc.LongVarBinaryHolder;
import fi.tnie.db.types.LongVarBinaryType;

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
