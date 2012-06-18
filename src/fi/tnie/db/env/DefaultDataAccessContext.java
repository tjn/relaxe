/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import fi.tnie.db.service.DataAccessContext;
import fi.tnie.db.service.DataAccessException;
import fi.tnie.db.service.DataAccessSession;

public class DefaultDataAccessContext<I extends Implementation>
	implements DataAccessContext {
	
	private I implementation;
	private ConnectionManager connectionManager;
			
	public DefaultDataAccessContext(I implementation, String jdbcURL, Properties jdbcConfig) {
		super();
		
		if (implementation == null) {
			throw new NullPointerException("implementation");
		}
		
		if (jdbcURL == null) {
			throw new NullPointerException("jdbcURL");
		}
				
		ConnectionFactory cf = new DriverManagerConnectionFactory();
		this.implementation = implementation;
		this.connectionManager = new DefaultConnectionManager(cf, jdbcURL, jdbcConfig);
	}

	public DefaultDataAccessContext(I implementation, ConnectionManager connectionManager) {
		super();
		
		if (implementation == null) {
			throw new NullPointerException("implementation");
		}
		
		if (connectionManager == null) {
			throw new NullPointerException("connectionManager");
		}
		
		this.implementation = implementation;
		this.connectionManager = connectionManager;
	}
	
	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}
	
	public I getImplementation() {
		return implementation;
	}
	
	@Override
	public DataAccessSession newSession() throws DataAccessException {
		try {
			final Connection c = connectionManager.reserve();
			
			return new AbstractDataAccessSession<I>(implementation, c) {
				@Override
				public void close() {
					connectionManager.release(c);
				}
			};
		}
		catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

}
