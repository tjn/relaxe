/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.IllegalIdentifierException;
import com.appspot.relaxe.meta.IdentifierRules;
import com.appspot.relaxe.meta.impl.pg.PGImplementation;
import com.appspot.relaxe.meta.impl.pg.PGTestCase;


public class PGEnvironmentTest 
	extends PGTestCase {
	
//	@Override
//	public void restore() throws IOException, InterruptedException {
//		// no need to restore
//	}
	
	public void testCreateIdentifier() 
		throws Exception {
	    
	    PGImplementation env = implementation();
		// PGEnvironment env = newEnv();
	    IdentifierRules ir = env.environment().getIdentifierRules();
		 
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
		PGImplementation env = implementation();
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
