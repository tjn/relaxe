/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DefaultConnectionManager
	implements ConnectionManager {
		
	private String jdbcURL;
	private Properties properties;
	private ConnectionFactory connectionFactory;
	
	public DefaultConnectionManager(ConnectionFactory cf, String jdbcURL, String user, String passwd) {
		this(cf, jdbcURL);		
		Properties p = new Properties();
		
		if (user != null) {
			p.setProperty("user", user);
		}
		if (passwd != null) {
			p.setProperty("password", passwd);
		}
		
		this.properties = p;
	}
	
	private DefaultConnectionManager(ConnectionFactory cf, String jdbcURL) {
		super();
		this.connectionFactory = cf;
		this.jdbcURL = jdbcURL;			
	}	
	
	public DefaultConnectionManager(ConnectionFactory cf, String jdbcURL, Properties properties) {
		this(cf, jdbcURL);
		this.properties = new Properties();
		
		if (properties != null) {
			this.properties.putAll(properties);
		}
	}


	@Override
	public void release(Connection c) {
		if (c != null) {
			try {
				c.close();		
			}
			catch (SQLException e) {
				closingFailed(c, e);
			}
		}		
	}

	protected void closingFailed(Connection c, SQLException e) {		
	}

	@Override
	public Connection reserve() throws SQLException {
		Connection c = connectionFactory.newConnection(jdbcURL, properties);
		return c;
	}

	
	
}
