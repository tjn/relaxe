/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

public class PGRestoreTest
	extends PGJDBCTestCase {
	
	@Override
	protected void setUp() throws Exception {	
	}
	
	public void testRestore() 
		throws Exception {
		restore();
	}
	
    public void testRestoreDropped() 
        throws Exception {        
        dropDatabaseIfExists();
        restore();
    }	
	
	@Override
	protected void tearDown() throws Exception {
	}
}
