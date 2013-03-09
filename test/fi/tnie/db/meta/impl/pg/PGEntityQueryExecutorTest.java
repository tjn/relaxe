/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import fi.tnie.db.EntityQueryExecutorTest;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.env.pg.PGPersistenceContext;

public class PGEntityQueryExecutorTest 
	extends EntityQueryExecutorTest<PGImplementation> {
	
	@Override
	public void testExecute3() throws Exception {	
		super.testExecute3();
	}

	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PGPersistenceContext();
	}
}
