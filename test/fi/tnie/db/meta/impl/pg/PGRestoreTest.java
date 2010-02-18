/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

public class PGRestoreTest
	extends PGTestCase {
	
	
	
	@Override
	protected void setUp() throws Exception {	
	}
	
	public void testRestore() 
		throws Exception {
		restore();
	}
	
	
	@Override
	protected void tearDown() throws Exception {
	}
}
