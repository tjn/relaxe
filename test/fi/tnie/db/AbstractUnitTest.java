/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.log.DefaultLogger;

public abstract class AbstractUnitTest
	extends TestCase {
		
	private Logger logger;
	
	private TestContext current;
	
	/**
	 * Context variables:
	 */
	private Implementation implementation;
	private String host;
	private String database;
	private String user;		
				
	public TestContext getCurrent() {
		return current;
	}
	
	@Override
	protected final void setUp() throws Exception {		
		this.current = null;		
		this.current = getContext();
		DefaultLogger.getInstance().setTarget(new Log4JLogger(logger()));
		init();
	}	
	
	protected final TestContext getContext() {
		if (current == null) {
			current = createContext();			
		}

		return current;
	}

	protected TestContext createContext() {
		Implementation imp = getImplementation();		
		String h = getHost();
		String d = getDatabase();		
		SimpleTestContext tc = new SimpleTestContext(imp, h, d, getUser(), "test");
		
		return tc;
	}

	public String getHost() {
		if (host == null) {
			host = createHost();			
		}

		return host;
	}
	
	protected String createHost() {
		return null;
	}
	
	public String getDatabase() {
		if (database == null) {
			database = createDatabase();			
		}

		return database;
	}
	
	public String createDatabase() {
		return "test";
	}

	public String getUser() {
		if (user == null) {
			user = createUser();			
		}

		return user;
	}

	private String createUser() {
		return "test";
	}

	public final Implementation getImplementation() {
		if (implementation == null) {
			implementation = createImplementation();			
		}

		return implementation;
	}
	
	
	protected Implementation createImplementation() {
		return new PGImplementation();
	}
	
	protected void init() {
		
	}
	
	public Logger logger() {
		if (logger == null) {
			logger = Logger.getLogger(getClass());			
		}

		return logger;
	}
	
	public Connection newConnection() throws SQLException {
		return getContext().newConnection();
	}
}
