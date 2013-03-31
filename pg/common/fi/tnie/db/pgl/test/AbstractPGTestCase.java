/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.pgl.test;

import java.io.IOException;
import java.util.Properties;

import fi.tnie.db.AbstractUnitTest;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.env.pg.PGPersistenceContext;

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
