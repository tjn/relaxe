/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;


import com.appspot.relaxe.AbstractUnitTest;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.pg.PGImplementation;
import com.appspot.relaxe.env.pg.PGPersistenceContext;



public abstract class PGTest
	extends AbstractUnitTest<PGImplementation>
{	
	public PGTest() {
		super();	
	}
		
//	public void testA() {		
//		logger().debug("enter: " + getName());
//		TestContext<PGImplementation> imp = getContext();
//		assertNotNull(imp);
//		logger().debug("exit: " + getName());
//	}
//
//	public void testB() {		
//		logger().debug("enter: " + getName());
//		TestContext<PGImplementation> imp = getContext();
//		assertNotNull(imp);
//		logger().debug("exit: " + getName());
//	}
	
	@Override
	public String getDatabase() {
		return "pagila";
	}
	
	@Override
	public String getUser() {
		return "relaxe_tester";
	}


	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PGPersistenceContext(new PGImplementation());
	}
	
	@Override
	protected String implementationTag() {
		return "pg";
	}
}
