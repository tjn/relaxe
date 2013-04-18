/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg.pagila;

import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.gen.pagila.ent.pub.Actor;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.StatementExecutorTest;

public class PagilaStatementExecutorTest
	extends StatementExecutorTest<PGImplementation> {

	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return null;
	}
		
	public void testFetch() throws Exception {
		BaseTable t = Actor.Type.TYPE.getMetaData().getBaseTable();
		super.testFetch(t);
	}

	@Override
	protected String getDatabase() {
		return "pagila";
	}	
	
	
}
