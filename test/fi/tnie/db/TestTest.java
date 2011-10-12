/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class TestTest
	extends AbstractTest {
	
	
	@Override
	protected void initFixture() {
		logger().debug("initFixture - enter: " + getName());
	}
	
	public void testA() {
		assertNotNull(getContext());		
	}
	
	public void testB() {		
		assertNotNull(getContext());
		throw new RuntimeException("asdf");
	}
	
	
	@Override
	protected void tearDown() throws Exception {		
		logger().debug("initFixture - tear: " + getName());
	}
	

}
