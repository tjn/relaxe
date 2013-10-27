/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessException;
import com.appspot.relaxe.service.DataAccessSession;


public class DefaultDataAccessContext<I extends Implementation<I>>
	implements DataAccessContext {
	
	private PersistenceContext<I> persistenceContext;
	private ConnectionManager connectionManager;
			
	public DefaultDataAccessContext(PersistenceContext<I> persistenceContext, String jdbcURL, Properties jdbcConfig) {
		super();
		
		if (persistenceContext == null) {
			throw new NullPointerException("persistenceContext");
		}
		
		if (jdbcURL == null) {
			throw new NullPointerException("jdbcURL");
		}
				
		ConnectionFactory cf = new DriverManagerConnectionFactory();
		this.persistenceContext = persistenceContext;
		this.connectionManager = new DefaultConnectionManager(cf, jdbcURL, jdbcConfig);
	}

	public DefaultDataAccessContext(PersistenceContext<I> persistenceContext, ConnectionManager connectionManager) {
		super();
		
		if (persistenceContext == null) {
			throw new NullPointerException("persistenceContext");
		}
		
		if (connectionManager == null) {
			throw new NullPointerException("connectionManager");
		}
		
		this.persistenceContext = persistenceContext;
		this.connectionManager = connectionManager;
	}
	
	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}
	
	public PersistenceContext<I> getPersistenceContext() {
		return persistenceContext;
	}
	
	@Override
	public DataAccessSession newSession() throws DataAccessException {
		try {
			final Connection c = connectionManager.reserve();
			
			return new DefaultDataAccessSession<I>(getPersistenceContext(), c) {
				@Override
				protected void closed() {
					connectionManager.release(c);
				}
			};
		}
		catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

}
