/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.rdbms;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.appspot.relaxe.service.ClosableDataAccessSession;
import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessException;


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
	public ClosableDataAccessSession newSession() throws DataAccessException {
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
