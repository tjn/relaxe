/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg;

import java.io.IOException;
import java.util.Properties;

import com.appspot.relaxe.AbstractUnitTest;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.pg.PGImplementation;
import com.appspot.relaxe.env.pg.PGPersistenceContext;


public abstract class AbstractPGTestCase
	extends AbstractUnitTest<PGImplementation> {
	
	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PGPersistenceContext();
	}
			
	@Override
	protected Properties getJdbcConfig() throws IOException {		
		return getJdbcConfigForDatabase();
	}

	@Override
	protected String implementationTag() {
		return "pg";
	}
}
