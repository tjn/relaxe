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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.SLF4JLogger;
import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.QueryHelper;
import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.env.ConnectionManager;
import com.appspot.relaxe.env.DefaultConnectionManager;
import com.appspot.relaxe.env.DefaultDataAccessContext;
import com.appspot.relaxe.env.DriverManagerConnectionFactory;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.log.DefaultLogger;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessException;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.ReferenceType;


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
	protected void setUp() throws Exception {		
		this.current = null;		
		this.current = getContext();
		DefaultLogger.getInstance().setTarget(new SLF4JLogger(logger()));
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
		Integer p = getPort();
		
		String d = getDatabase();
		
		Properties c = getJdbcConfig();
		
		logger().debug("jdbc config for context: " + c);								
		DefaultTestContext<I> tc = new DefaultTestContext<I>(pc, h, p, d, c);		
						
		return tc;
	}
	
	public Integer getPort() {
		return null;
	}
	
	public Integer getPort(int defaultPort) {		 				
		Integer port = getInteger("jdbc.url.port");
		
		if (port == null) {
			port = Integer.valueOf(defaultPort);
		}
		
		return null;		
	}

	private Integer getInteger(String key) {		
		Integer value = null;
		
		try {
			Properties config = getJdbcConfigForDatabase();
			String p = config.getProperty(key);
			value = (p == null) ? null : Integer.valueOf(p);			
		}
		catch (IOException e) {
			logger.warn(e.getMessage());
		}	
		catch (NumberFormatException e) {
			logger.warn(e.getMessage());
		}
		
		return value;
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
			logger = LoggerFactory.getLogger(getClass());			
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
			logger().debug("type      : " + type + " (" + AbstractPrimitiveType.getSQLTypeName(type) + ")");
			logger().debug("typeName  : " + typeName);
			logger().debug("className : " + className);
			logger().debug("content   : " + content);
			logger().debug("object    : " + o);
			logger().debug("objType   : " + ((o == null) ? null : o.getClass().getName()));
			logger().debug("string    : " + (s));
			
			if (o != null && o instanceof java.sql.Array) {
				Array a = (Array) o;
				logger().debug("arrayBaseType   : " + a.getBaseType() + " (" + AbstractPrimitiveType.getSQLTypeName(a.getBaseType()) + ")");
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
			logger().debug("type      : " + type + " (" + AbstractPrimitiveType.getSQLTypeName(type) + ")");
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
		R extends com.appspot.relaxe.ent.Reference, 
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
	E newEntity(T type) {
		return type.getMetaData().getFactory().newEntity();
	}
	

	protected DefaultConnectionManager newConnectionManager() throws IOException {
		TestContext<I> tc = getContext();
				
		String jdbcURL = tc.getJdbcURL();
		Properties config = tc.getJdbcConfig();		
		
		logger().debug("testLoad: jdbcURL=" + jdbcURL);
		logger().debug("testLoad: jdbcConfig=" + config);
		
		DriverManagerConnectionFactory cf = new DriverManagerConnectionFactory();
		DefaultConnectionManager cm = new DefaultConnectionManager(cf, jdbcURL, config);
		return cm;
	}
	
	protected DataAccessContext newDataAccessContext(ConnectionManager cm) {
		PersistenceContext<I> pc = getPersistenceContext();
		DefaultDataAccessContext<I> dctx = new DefaultDataAccessContext<I>(pc, cm);
		return dctx;
	}
	
	protected DataAccessSession newSession() throws IOException, DataAccessException {	
		ConnectionManager cm = newConnectionManager();
		DataAccessContext ctx = newDataAccessContext(cm);		
		DataAccessSession das = ctx.newSession();
		return das;
	}
	
	protected <
		A extends Attribute, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content,
	    RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>
	>
	QueryExpression toQueryExpression(EntityQuery<A, R, T, E, H, F, M, C, RE> qo) {
		EntityQueryExpressionBuilder<A, R, T, E, H, F, M, C, RE> eqb = new EntityQueryExpressionBuilder<A, R, T, E, H, F, M, C, RE>(qo);
		return eqb.getQueryExpression();
	}
}
