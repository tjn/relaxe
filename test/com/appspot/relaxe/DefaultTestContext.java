/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.query.QueryException;


public class DefaultTestContext<I extends Implementation<I>>
	implements TestContext<I> {

	private Catalog catalog;
	private PersistenceContext<I> persistenceContext;
	private Properties jdbcProperties;
	private String jdbcURL;
	
//	public DefaultTestContext(PersistenceContext<I> persistenceContext) {
//		this(persistenceContext, "test");
//	}
		
//	public DefaultTestContext(PersistenceContext<I> persistenceContext, String database) {
//		init(persistenceContext, database, createJdbcProperties());
//	}
	
	public DefaultTestContext(PersistenceContext<I> persistenceContext, String database, Properties jdbcProperties) {		
		init(persistenceContext, null, null, database, jdbcProperties);
	}
	
	public DefaultTestContext(PersistenceContext<I> persistenceContext, String host, Integer port, String database, Properties jdbcProperties) {		
		init(persistenceContext, host, port, database, jdbcProperties);
	}

//	protected Properties createJdbcProperties() {
//		Properties config = new Properties();		
//		config.setProperty("user", "test");
//		config.setProperty("password", "test");		
//		return config;
//	}

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
			CatalogFactory cf = getImplementation().catalogFactory();
			this.catalog = cf.create(c);
			c.close();
			
		}
		
		return this.catalog;
	}

	@Override
	public I getImplementation() {
		return this.persistenceContext.getImplementation();
	}

	@Override
	public Connection newConnection() throws SQLException, ClassNotFoundException {
		I imp = getImplementation();
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
