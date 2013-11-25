/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
