/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import fi.tnie.db.PersistenceManagerTest;
import fi.tnie.db.SimpleUnificationContext;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.test.PagilaPersistenceContext;

public class PagilaPersistenceManagerTest
	extends PersistenceManagerTest<PGImplementation> {
		
	private UnificationContext unificationContext = new SimpleUnificationContext();
	
	@Override
	public void testMerge() throws Exception {
		super.testMerge();
	}
	
	public void testMergeDependent1() throws Exception {
		setUnificationContext(unificationContext);
		super.testMergeDependent();		
	}

	public void testMergeDependent2() throws Exception {
		setUnificationContext(null);
		super.testMergeDependent();
	}

	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PagilaPersistenceContext();
	}
}
