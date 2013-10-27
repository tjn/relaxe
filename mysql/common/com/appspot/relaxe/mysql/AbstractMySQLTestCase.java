/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql;


import java.io.IOException;
import java.util.Properties;

import com.appspot.relaxe.AbstractUnitTest;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.mysql.MySQLImplementation;
import com.appspot.relaxe.env.mysql.MySQLPersistenceContext;


public abstract class AbstractMySQLTestCase
	extends AbstractUnitTest<MySQLImplementation> {
	
	@Override
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new MySQLPersistenceContext();
	}
			
	@Override
	protected Properties getJdbcConfig() throws IOException {		
		return getJdbcConfigForDatabase();
	}
	

	@Override
	protected String implementationTag() {
		return "mysql";
	}
}
