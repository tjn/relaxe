/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.common;

import java.sql.Connection;
import java.sql.DriverManager;

import junit.framework.TestCase;

public abstract class JDBCTestCase
	extends TestCase {
	
	private Connection connection;
	
	private String userid;
	private String passwd;
	private String database;
	private String driverClass;
	
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
	}

	@Override
	protected void tearDown() throws Exception {
		if (this.connection != null) {
			this.connection.close();
			this.connection = null;
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

}
