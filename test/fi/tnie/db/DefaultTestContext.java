/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.query.QueryException;

public class DefaultTestContext<I extends Implementation<I>>
	implements TestContext<I> {

	private Catalog catalog;
	private PersistenceContext<I> persistenceContext;
	private Properties jdbcProperties;
	private String jdbcUrl;
	
	public DefaultTestContext(PersistenceContext<I> persistenceContext) throws SQLException, QueryException {
		this(persistenceContext, "test");
	}
		
	public DefaultTestContext(PersistenceContext<I> persistenceContext, String database) throws SQLException, QueryException {		
		Properties config = createJdbcProperties();
		init(persistenceContext, database, config);
	}

	protected Properties createJdbcProperties() {
		Properties config = new Properties();		
		config.setProperty("user", "test");
		config.setProperty("password", "test");		
		return config;
	}

	private void init(PersistenceContext<I> persistenceContext, String database, Properties jdbcProperties) throws SQLException, QueryException {		
				
		if (persistenceContext == null) {
			throw new NullPointerException("persistenceContext");
		}
		
		this.persistenceContext = persistenceContext;
		this.jdbcProperties = jdbcProperties;		
		this.jdbcUrl = persistenceContext.getImplementation().createJdbcUrl(database);
		
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
		
		
		
		Connection c = DriverManager.getConnection(jdbcUrl, jdbcProperties);		
		return c;
	}
	
	@Override
	public PersistenceContext<I> getPersistenceContext() {
		return this.persistenceContext;
	}
}
