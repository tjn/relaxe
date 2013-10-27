/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mariadb;


import java.io.IOException;
import java.util.Properties;

import com.appspot.relaxe.AbstractUnitTest;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.mariadb.MariaDBImplementation;
import com.appspot.relaxe.env.mariadb.MariaDBPersistenceContext;


public abstract class AbstractMariaDBTestCase
	extends AbstractUnitTest<MariaDBImplementation> {
	
	@Override
	protected PersistenceContext<MariaDBImplementation> createPersistenceContext() {
		return new MariaDBPersistenceContext();
	}
			
	@Override
	protected Properties getJdbcConfig() throws IOException {		
		return getJdbcConfigForDatabase();
	}

	@Override
	protected String implementationTag() {
		return "mariadb";
	}
	

	@Override
	public Integer getPort() {
		return Integer.valueOf(3307);
	}
}
