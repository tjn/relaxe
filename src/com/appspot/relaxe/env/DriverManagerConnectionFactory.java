/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DriverManagerConnectionFactory 
	implements ConnectionFactory {
	
	public DriverManagerConnectionFactory() {
		super();
	}
		
	@Override
	public Connection newConnection(String jdbcURL, Properties properties)
			throws SQLException {		
		Connection c = DriverManager.getConnection(jdbcURL, properties);
		c.setAutoCommit(false);
		return c;
	}
}
