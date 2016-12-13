package com.appspot.relaxe.rdbms;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.rdbms.ConnectionManager;

public final class DataSourceConnectionManager implements ConnectionManager {
		 
	private static Logger logger = LoggerFactory.getLogger(DataSourceConnectionManager.class);	
	
	private final DataSource dataSource;

	public DataSourceConnectionManager(DataSource ds) {
		this.dataSource = ds;
	}

	@Override
	public void release(Connection c) {
		try {
			c.close();
		} 
		catch (SQLException e) {					
			onCloseError(e);
		}					
	}

	public void onCloseError(SQLException e) {
		logger.warn(e.getMessage(), e);
	}

	@Override
	public Connection reserve() throws SQLException {
		Connection c = dataSource.getConnection();
		c.setAutoCommit(false);
		return c;
	}
}