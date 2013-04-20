/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.DBMetaTestCase;

public abstract class EnvironmentTest<I extends Implementation<I>> 
	extends DBMetaTestCase<I> {
	
//	@Override
//	public void restore() throws IOException, InterruptedException {
//		// no need to restore
//	}
	
	public void testCreateIdentifier() 
		throws Exception {
	    
		Implementation<?> env = getEnvironmentContext().getImplementation();
		IdentifierRules ir = env.environment().getIdentifierRules();
		// PGEnvironment env = newEnv();		
		 
		final Identifier a = ir.toIdentifier("abc");		
		assertTrue(a.isOrdinary());
				
		final Identifier b = ir.toIdentifier("Abc");
		assertTrue(b.isOrdinary());

		final Identifier c = ir.toIdentifier("ABC");
		assertTrue(c.isOrdinary());

		final Identifier d = ir.toIdentifier("Mary's");
		assertFalse("Delimited expected", d.isOrdinary());	
	}
	
	
	public void testIllegalIdentifier() throws Exception {
		Implementation<?> env = getEnvironmentContext().getImplementation();
		IdentifierRules ir = env.environment().getIdentifierRules();
		
		try {
			ir.toIdentifier("");
			assertThrown(IllegalIdentifierException.class);
		}
		catch (IllegalIdentifierException e) {
//			OK, expected
		}
				
		assertNull(ir.toIdentifier(null));
		
		Identifier id = ir.toIdentifier("1A");
		assertFalse(id + " is not not valid as an ordinary", id.isOrdinary());
	}
	
}
