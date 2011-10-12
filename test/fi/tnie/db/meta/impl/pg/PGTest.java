/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;


import fi.tnie.db.AbstractUnitTest;
import fi.tnie.db.TestContext;

public class PGTest
	extends AbstractUnitTest
{	
	public PGTest() {
		super();	
	}
		
	public void testA() {		
		logger().debug("enter: " + getName());
		TestContext imp = getContext();
		assertNotNull(imp);
		logger().debug("exit: " + getName());
	}

	public void testB() {		
		logger().debug("enter: " + getName());
		TestContext imp = getContext();
		assertNotNull(imp);
		logger().debug("exit: " + getName());
	}

}
