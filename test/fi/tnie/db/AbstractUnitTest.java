/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.log.DefaultLogger;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

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
		
	protected abstract String implementationTag();
		
	
	@Override
	protected final void setUp() throws Exception {		
		this.current = null;		
		this.current = getContext();
		DefaultLogger.getInstance().setTarget(new Log4JLogger(logger()));
		init();
	}	
	
	protected final TestContext<I> getContext() throws IOException {
		if (current == null) {
			current = createContext();			
		}

		return current;
	}

	protected TestContext<I> createContext() throws IOException {
		// I imp = getImplementation();
		PersistenceContext<I> pc = getPersistenceContext();
		String h = getHost();
		
		String d = getDatabase();
		
		Properties c = getJdbcConfig();
		
		logger().debug("jdbc config for context: " + c);								
		DefaultTestContext<I> tc = new DefaultTestContext<I>(pc, h, null, d, c);		
						
		return tc;
	}

	protected Properties getJdbcConfig()
		throws IOException {
		Properties c = new Properties();
		c.setProperty("user", getUser());
		c.setProperty("password", getPassword());		
		return c;
	}
	
	protected Properties getJdbcConfigForDatabase() throws IOException {
		String d = getDatabase();
		StringBuilder buf = new StringBuilder("/").append(implementationTag()).append("/").append(d).append(".properties");
		
		logger().info("jdbc config: " + buf);
		
		return getJdbcConfig(buf.toString());
	}
	
	
	protected Properties getJdbcConfig(String profile) 
		throws IOException {
		InputStream in = getClass().getResourceAsStream(profile);
		
		if (in == null) {
			throw new IllegalArgumentException("no configuration file for profile: " + profile);
		}
		
		Properties config = new Properties();
		config.load(in);
		in.close();
		return config;		
	}

	public String getHost() {
		return null;
	}
	
	public abstract String getDatabase();
	
	public String getUser() {
		return null;
	}
	
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
	
	public Connection newConnection() throws SQLException, ClassNotFoundException, IOException {
		return getContext().newConnection();
	}
	
	
	protected abstract PersistenceContext<I> createPersistenceContext();
	
	public Connection close(Connection c) {
		return QueryHelper.doClose(c);
	}
	
	public Statement close(Statement st) {
		return QueryHelper.doClose(st);
	}
	
	public ResultSet close(ResultSet rs) {
		return QueryHelper.doClose(rs);
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


	protected int executeUpdate(String q) throws Exception {
		Connection c = null;
		Statement st = null;
		
		try {		
			c = newConnection();		
			st = c.createStatement();		
			
			logger().debug("executing: " + q);
			int u = st.executeUpdate(q);
			logger().debug("executed: " + u);
			return u;
		}
		finally {
			st = QueryHelper.doClose(st);
			c = QueryHelper.doClose(c);
		}
	}
	
	
	protected <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	PersistenceManager<A, R, T, E, H, F, M, C> create(E e) {
		PersistenceManager<A, R, T, E, H, F, M, C> pm = new PersistenceManager<A, R, T, E, H, F, M, C>(e, getPersistenceContext(), null);
		return pm;
	}
	
	public <
		T extends ReferenceType<?, ?, T, E, ?, ?, ?, ?>,
		E extends Entity<?, ?, T, E, ?, ?, ?, ?>
	> 
	E newInstance(T type) {
		return type.getMetaData().getFactory().newInstance();
	}
}
