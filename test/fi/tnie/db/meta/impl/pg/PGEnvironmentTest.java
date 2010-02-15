/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import fi.tnie.db.meta.CatalogFactory;
import junit.framework.TestCase;

public class PGEnvironmentTest 
	extends PGTestCase {
		
	public void testCatalogFactory() 
		throws Exception {
		
		PGEnvironment env = new PGEnvironment();
		
		CatalogFactory cf = env.catalogFactory();
			
		fail("Not yet implemented");
	}

	public void testCreateIdentifier() {
		fail("Not yet implemented");
	}

}
