/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.query.QueryException;

public class DefaultTestContext
	implements TestContext {

	private Catalog catalog;
	private Implementation implementation;
	// private Connection connection;		
	private Properties jdbcProperties;
	private String jdbcUrl;
	
	public DefaultTestContext(Implementation imp) throws SQLException, QueryException {
		this(imp, "test");
	}
		
	public DefaultTestContext(Implementation imp, String database) throws SQLException, QueryException {		
		Properties config = createJdbcProperties();
		init(imp, database, config);
	}

	protected Properties createJdbcProperties() {
		Properties config = new Properties();		
		config.setProperty("user", "test");
		config.setProperty("password", "test");		
		return config;
	}

	private void init(Implementation imp, String database, Properties jdbcProperties) throws SQLException, QueryException {		
				
		if (imp == null) {
			throw new NullPointerException("implementation");
		}
		
		this.implementation = imp;
		this.jdbcProperties = jdbcProperties;		
		this.jdbcUrl = imp.createJdbcUrl(database);
		
	}

	@Override
	public Catalog getCatalog() throws SQLException, QueryException {
		if (catalog == null) {
			Connection c = newConnection();
			CatalogFactory cf = this.implementation.catalogFactory();
			this.catalog = cf.create(c);
			c.close();
			
		}
		
		return this.catalog;
	}

	@Override
	public Implementation getImplementation() {
		return this.implementation;
	}

	@Override
	public Connection newConnection() throws SQLException {
		Implementation imp = getImplementation();
		Driver drv = imp.getDriver();
		Connection c = drv.connect(jdbcUrl, jdbcProperties);		
		return c;
	}
	
}
