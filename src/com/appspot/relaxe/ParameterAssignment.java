/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterAssignment
{
	public void assign(PreparedStatement ps, int ordinal) 
		throws SQLException;
	
	
}