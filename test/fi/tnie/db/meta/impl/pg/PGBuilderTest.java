/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.IOException;

public class PGBuilderTest
	extends PGTestCase {
    
    @Override
    protected void setUp() throws Exception {
        logger().debug("setUp - enter");
    }

	@Override
	public void restore() throws IOException, InterruptedException {
		// no need to restore
	}

//	public void testBuilder() 
//	    throws Exception {	    
//	    Connection c = getConnection();
//	    Catalog cat = getCatalog();
//	    new BuilderTest().testGeneration(cat, c);
//	}
	
	public void testDummy() {
	    logger().debug("testDummy - enter");
	}
	
	@Override
	protected void tearDown() throws Exception {
	    logger().debug("tearDown - enter");
	}
	
	
}
