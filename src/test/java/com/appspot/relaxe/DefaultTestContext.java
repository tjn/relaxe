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
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rdbms.CatalogFactory;
import com.appspot.relaxe.rdbms.Implementation;
import com.appspot.relaxe.rdbms.PersistenceContext;


public class DefaultTestContext<I extends Implementation<I>>
	implements TestContext<I> {

	private Catalog catalog;
	private PersistenceContext<I> persistenceContext;
	private Properties jdbcProperties;
	private String jdbcURL;

	public DefaultTestContext(PersistenceContext<I> persistenceContext, String database, Properties jdbcProperties) {		
		init(persistenceContext, null, null, database, jdbcProperties);
	}
	
	public DefaultTestContext(PersistenceContext<I> persistenceContext, String host, Integer port, String database, Properties jdbcProperties) {		
		init(persistenceContext, host, port, database, jdbcProperties);
	}

	private void init(PersistenceContext<I> persistenceContext, String host, Integer port, String database, Properties jdbcProperties) {		
				
		if (persistenceContext == null) {
			throw new NullPointerException("persistenceContext");
		}
		
		this.persistenceContext = persistenceContext;
		this.jdbcProperties = jdbcProperties;
		I imp = persistenceContext.getImplementation();
		this.jdbcURL = (port == null) ? imp.createJdbcUrl(host, database) : imp.createJdbcUrl(host, port, database);		
	}
	
	@Override
	public String getJdbcURL() {
		return jdbcURL;
	}

	@Override
	public Catalog getCatalog() throws SQLException, QueryException, ClassNotFoundException {
		if (catalog == null) {
			Connection c = newConnection();
			I imp = getPersistenceContext().getImplementation();			
			CatalogFactory cf = imp.catalogFactory();
			this.catalog = cf.create(c);
			c.close();
			
		}
		
		return this.catalog;
	}

	@Override
	public Connection newConnection() throws SQLException, ClassNotFoundException {
		I imp = getPersistenceContext().getImplementation();
//		Driver drv = imp.getDriver();		
//		Connection c = drv.connect(jdbcUrl, jdbcProperties);
		Class.forName(imp.defaultDriverClassName());
		
		
		
		Connection c = DriverManager.getConnection(jdbcURL, jdbcProperties);		
		return c;
	}
	
	@Override
	public PersistenceContext<I> getPersistenceContext() {
		return this.persistenceContext;
	}
	
	@Override
	public Properties getJdbcConfig() {
		return this.jdbcProperties;
	}
}
