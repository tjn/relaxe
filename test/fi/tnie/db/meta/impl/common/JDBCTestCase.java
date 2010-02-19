/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

import org.apache.log4j.Logger;

import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;

import junit.framework.TestCase;

public abstract class JDBCTestCase
	extends TestCase {
	
	private Connection connection;
	
	private String userid;
	private String passwd;
	private String database;
	private String driverClass;
	
	private static Logger logger = Logger.getLogger(JDBCTestCase.class);
	
	protected JDBCTestCase(String driverClass) {
		this(driverClass, "tester", "password", "dbmeta_test");	
	}
	
	protected JDBCTestCase(String driverClass, String userid, String passwd, String database) {
		super();
		this.userid = userid;
		this.passwd = passwd;
		this.database = database;
		this.driverClass = driverClass;
		
		if (userid == null) {
			throw new NullPointerException("'userid' must not be null");
		}
		
		if (passwd == null) {
			throw new NullPointerException("'passwd' must not be null");
		}
		
		if (database == null) {
			throw new NullPointerException("'database' must not be null");
		}						
	}

	protected abstract String getDatabaseURL();

	@Override
	protected void setUp() throws Exception {			
		super.setUp();
				
//		restore();
		
		if (this.driverClass != null) {
			Class.forName(this.driverClass);
		}
		
		String url = getDatabaseURL();
		this.connection = DriverManager.getConnection(url, getUserid(), getPasswd());
		logger().debug("connection resreved");
	}

	@Override
	protected void tearDown() throws Exception {
		if (this.connection != null) {
			this.connection.close();
			this.connection = null;
			logger().debug("connection closed");
		}		
	}

	protected Connection getConnection() {
		return connection;
	}

	protected String getUserid() {
		return userid;
	}

	protected String getPasswd() {
		return passwd;
	}

	protected String getDatabase() {
		return database;
	}
	
	public void restore()
		throws Exception {		
	}

	protected <E extends Exception> void assertThrown(Class<? extends E> e) {
		fail(e + " was not thrown");
	}

	protected void testPrimaryKey(PrimaryKey pk) {
		assertNotNull(pk);
		assertNotNull(pk.getTable());				
		assertNotNull(pk.columns());
		assertFalse(pk.columns().isEmpty());
	}

	protected void testForeignKey(ForeignKey fk) {
		assertNotNull(fk);
		assertNotNull(fk.getReferenced());
		assertNotNull(fk.getReferencing());
		assertFalse(fk.columns().isEmpty());
		
		for (Map.Entry<Column, Column> p : fk.columns().entrySet()) {					
			assertNotNull(p.getKey());
			assertNotNull(p.getValue());
			
			assertNotSame(p.getKey(), p.getValue());
			
			logger().debug("ref'ing: " + p.getKey().getUnqualifiedName().getName());
			logger().debug("ref'ed: " + p.getValue().getUnqualifiedName().getName());
			
			assertNotNull(fk.getReferencing().columnMap().get(p.getKey().getUnqualifiedName()));
			assertNotNull(fk.getReferenced().columnMap().get(p.getValue().getUnqualifiedName()));
								
			// TODO: 
			// it should be enough for all referenced columns to be
			// part of the same candidate key (not necessarily a primary key),
			// but we have no representation for candidate key at the moment 
//			assertTrue(p.getValue().isPrimaryKeyColumn());
		}
	}

	
	public static Logger logger() {
		return JDBCTestCase.logger;
	}
}

