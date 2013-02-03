/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

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
