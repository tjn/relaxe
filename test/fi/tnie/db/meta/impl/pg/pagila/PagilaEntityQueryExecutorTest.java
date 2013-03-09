/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg.pagila;

import fi.tnie.db.EntityQueryExecutorTest;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.test.PagilaPersistenceContext;

public class PagilaEntityQueryExecutorTest 
	extends EntityQueryExecutorTest<PGImplementation> {
	
	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PagilaPersistenceContext();
	}
}
