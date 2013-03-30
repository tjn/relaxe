/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg.pagila;

import java.sql.SQLException;
import java.util.Properties;

import fi.tnie.db.TestContext;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.meta.DBMetaTestCase;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.test.PagilaPersistenceContext;

public abstract class AbstractPagilaTestCase
	extends DBMetaTestCase<PGImplementation> {

	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PagilaPersistenceContext();
	}
		
	public TestContext<PGImplementation> getTestContext()
			throws SQLException, QueryException {
		return super.getTestContext(getPersistenceContext().getImplementation());
	}

	@Override
	protected String getDatabase() {
		return "pagila";
	}
	
	@Override
	protected String getUsername() {
		return "relaxe_tester";
	}
}
