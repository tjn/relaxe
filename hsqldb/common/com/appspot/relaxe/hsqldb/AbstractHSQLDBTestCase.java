/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.hsqldb;

import java.io.IOException;
import java.util.Properties;

import com.appspot.relaxe.AbstractUnitTest;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.hsqldb.HSQLDBImplementation;
import com.appspot.relaxe.env.hsqldb.HSQLDBPersistenceContext;


public abstract class AbstractHSQLDBTestCase
	extends AbstractUnitTest<HSQLDBImplementation> {
	
	@Override
	protected PersistenceContext<HSQLDBImplementation> createPersistenceContext() {
		return new HSQLDBPersistenceContext();
	}
			
	@Override
	protected Properties getJdbcConfig() throws IOException {		
		return getJdbcConfigForDatabase();
	}

	@Override
	protected String implementationTag() {
		return "hsqldb";
	}
}
