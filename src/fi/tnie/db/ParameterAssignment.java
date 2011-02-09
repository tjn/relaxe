/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fi.tnie.db.types.PrimitiveType;

public interface ParameterAssignment
{
	public void assign(PreparedStatement ps, int ordinal) 
		throws SQLException;
	
	
}
