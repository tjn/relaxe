/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.DBMetaTestCase;

public class EnvironmentTest 
	extends DBMetaTestCase {
	
//	@Override
//	public void restore() throws IOException, InterruptedException {
//		// no need to restore
//	}
	
	public void testCreateIdentifier() 
		throws Exception {
	    
		Implementation env = getEnvironmentContext().getImplementation();
		// PGEnvironment env = newEnv();		
		 
		final Identifier a = env.createIdentifier("abc");		
		assertTrue(a.isOrdinary());
				
		final Identifier b = env.createIdentifier("Abc");
		assertTrue(b.isOrdinary());

		final Identifier c = env.createIdentifier("ABC");
		assertTrue(c.isOrdinary());

		final Identifier d = env.createIdentifier("Mary's");
		assertFalse("Delimited expected", d.isOrdinary());	
	}
	
	
	public void testIllegalIdentifier() throws Exception {
		Implementation env = getEnvironmentContext().getImplementation();
		
		try {
			env.createIdentifier("");
			assertThrown(IllegalIdentifierException.class);
		}
		catch (IllegalIdentifierException e) {
//			OK, expected
		}
				
		assertNull(env.createIdentifier(null));
		
		Identifier id = env.createIdentifier("1A");
		assertFalse(id + " is not not valid as an ordinary", id.isOrdinary());
	}
	
}
