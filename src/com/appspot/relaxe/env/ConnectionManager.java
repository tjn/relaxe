/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionManager {
		
	Connection reserve()
		throws SQLException;
	
	void release(Connection c);
}
