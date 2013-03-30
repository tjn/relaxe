/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.log.DefaultLogger;
import fi.tnie.db.types.PrimitiveType;

public abstract class AbstractUnitTest<I extends Implementation<I>>
	extends TestCase {
		
	private Logger logger;
	
	private TestContext<I> current;
	
	/**
	 * Context variables:
	 */
	private PersistenceContext<I> persistenceContext;
	// private I implementation;
	// private String host;
						
	public TestContext<I> getCurrent() {
		return current;
	}
	
		
		
	
	@Override
	protected final void setUp() throws Exception {		
		this.current = null;		
		this.current = getContext();
		DefaultLogger.getInstance().setTarget(new Log4JLogger(logger()));
		init();
	}	
	
	protected final TestContext<I> getContext() {
		if (current == null) {
			current = createContext();			
		}

		return current;
	}

	protected TestContext<I> createContext() {
		// I imp = getImplementation();
		PersistenceContext<I> pc = getPersistenceContext();
		String d = getDatabase();
		Properties c = getJdbcConfig();
								
		DefaultTestContext<I> tc = new DefaultTestContext<I>(pc, d, c);		
						
		return tc;
	}

	protected Properties getJdbcConfig() {
		Properties c = new Properties();
		c.setProperty("user", getUser());
		c.setProperty("password", getPassword());		
		return c;
	}

	public String getHost() {
		return null;
	}
	
	public abstract String getDatabase();
	public abstract String getUser();
	
	public String getPassword() {
		return "password"; 
	}
	
	protected PersistenceContext<I> getPersistenceContext() {
		if (persistenceContext == null) {
			persistenceContext = createPersistenceContext();			
		}

		return persistenceContext;
		
	}
	
	protected void init() {
		
	}
	
	public Logger logger() {
		if (logger == null) {
			logger = Logger.getLogger(getClass());			
		}

		return logger;
	}
	
	public Connection newConnection() throws SQLException, ClassNotFoundException {
		return getContext().newConnection();
	}
	
	
	protected abstract PersistenceContext<I> createPersistenceContext();
	
	public Connection close(Connection c) {
		return QueryHelper.doClose(c);
	}
	
	public Statement close(Statement st) {
		return QueryHelper.doClose(st);
	}
	
	
	protected void dumpMetaDataWithSamples(Statement st, String q) throws SQLException {
		ResultSet rs = st.executeQuery(q);
		ResultSetMetaData meta = rs.getMetaData();
		int cc = meta.getColumnCount();
		
		logger().debug(q);
		
		boolean content = rs.next();
		
		for (int i = 1; i <= cc; i++) {
			String label = meta.getColumnLabel(i);
			String name = meta.getColumnName(i);
			String className = meta.getColumnClassName(i);
			int type = meta.getColumnType(i);
			String typeName = meta.getColumnTypeName(i);
						
			Object o = content ? rs.getObject(i) : null;
			String s = content ? rs.getString(i) : null;
			
			logger().debug("ordinal   : " + i);
			logger().debug("label     : " + label);
			logger().debug("name      : " + name);
			logger().debug("type      : " + type + " (" + PrimitiveType.getSQLTypeName(type) + ")");
			logger().debug("typeName  : " + typeName);
			logger().debug("className : " + className);
			logger().debug("content   : " + content);
			logger().debug("object    : " + o);
			logger().debug("objType   : " + ((o == null) ? null : o.getClass().getName()));
			logger().debug("string    : " + (s));
			
			if (o != null && o instanceof java.sql.Array) {
				Array a = (Array) o;
				logger().debug("arrayBaseType   : " + a.getBaseType() + " (" + PrimitiveType.getSQLTypeName(a.getBaseType()) + ")");
				logger().debug("arrayBaseTypeName  : " + a.getBaseTypeName());
			}
			
						
			logger().debug("");
		}
		
		rs.close();
	}
	
	protected void dumpMetaData(String q) throws Exception {
		Connection c = null;
		Statement st = null;
		
		try {
			c = getContext().newConnection();		
			st = c.createStatement();
			
			ResultSet rs = st.executeQuery(q);			
			logger.debug(q);						
			dump(rs.getMetaData());
			rs.close();
		}
		finally {
			close(st);
			close(c);			
		}		
	}
	
	protected void dump(ResultSetMetaData meta) throws SQLException {
		int cc = meta.getColumnCount();
		
		for (int i = 1; i <= cc; i++) {
			String label = meta.getColumnLabel(i);
			String name = meta.getColumnName(i);
			String className = meta.getColumnClassName(i);
			int type = meta.getColumnType(i);
			String typeName = meta.getColumnTypeName(i);						
		
			logger().debug("ordinal   : " + i);
			logger().debug("label     : " + label);
			logger().debug("name      : " + name);
			logger().debug("type      : " + type + " (" + PrimitiveType.getSQLTypeName(type) + ")");
			logger().debug("typeName  : " + typeName);
			logger().debug("className : " + className);
			
						
			logger().debug("");
		}
	}	
}
