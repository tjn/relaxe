/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg.pagila;

import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.gen.pagila.ent.LiteralCatalog;
import fi.tnie.db.gen.pagila.ent.LiteralCatalog.LiteralBaseTable;
import fi.tnie.db.StatementExecutorTest;

public class PagilaStatementExecutorTest
	extends StatementExecutorTest<PGImplementation> {

	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return null;
	}
		
	public void testFetch() throws Exception {
		LiteralBaseTable t = LiteralCatalog.LiteralBaseTable.PUBLIC_ACTOR;
		super.testFetch(t);
	}

	@Override
	protected String getDatabase() {
		return "pagila";
	}	
	
	
}
