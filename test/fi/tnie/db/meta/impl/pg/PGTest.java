/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;


import fi.tnie.db.AbstractUnitTest;
import fi.tnie.db.TestContext;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.env.pg.PGPersistenceContext;

public class PGTest
	extends AbstractUnitTest<PGImplementation>
{	
	public PGTest() {
		super();	
	}
		
	public void testA() {		
		logger().debug("enter: " + getName());
		TestContext<PGImplementation> imp = getContext();
		assertNotNull(imp);
		logger().debug("exit: " + getName());
	}

	public void testB() {		
		logger().debug("enter: " + getName());
		TestContext<PGImplementation> imp = getContext();
		assertNotNull(imp);
		logger().debug("exit: " + getName());
	}


	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PGPersistenceContext(new PGImplementation());
	}

}
