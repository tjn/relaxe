/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import com.appspot.relaxe.env.pg.PGImplementation;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.IllegalIdentifierException;
import com.appspot.relaxe.expr.ddl.DefaultDefinition;
import com.appspot.relaxe.meta.impl.pg.PGIdentifierRules;
import com.appspot.relaxe.types.PrimitiveType;

public class PGEnvironmentTest
	extends PGTestCase {
	
//	@Override
//	public void restore() throws IOException, InterruptedException {
//		// no need to restore
//	}
	
	public void testCreateIdentifier() 
		throws Exception {
	    
		PGImplementation env = implementation();
		PGIdentifierRules ir = env.getEnvironment().getIdentifierRules();
		 
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
		// Implementation env = getEnvironmentContext().getImplementation();
		PGImplementation env = implementation();
		PGIdentifierRules ir = env.getEnvironment().getIdentifierRules();
		
		try {
			ir.toIdentifier("");
			assertThrown(IllegalIdentifierException.class);
		}
		catch (IllegalIdentifierException e) {
//			OK, expected
		}
				
		try {
			ir.toIdentifier(null);
			throw new Exception(NullPointerException.class + " expected");
		}
		catch (NullPointerException e) {			
		}
		
		Identifier id = ir.toIdentifier("1A");
		assertFalse(id + " is not not valid as an ordinary", id.isOrdinary());
	}
	
	public void testNewDefault1() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("now()", PrimitiveType.TIMESTAMP);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT CURRENT_TIMESTAMP", expr.toUpperCase());
	}
	
	public void testNewDefault2() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("('now'::text)::date", PrimitiveType.DATE);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT CURRENT_DATE", expr.toUpperCase());
	}
	
	public void testNewDefault3() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("123", PrimitiveType.INTEGER);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT 123", expr.toUpperCase());
	}
	
	public void testNewDefault4() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("123123123123", PrimitiveType.BIGINT);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT 123123123123", expr.toUpperCase());
	}
	
	public void testNewDefault5() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("'2013-01-11 12:13:14.123'::timestamp without time zone", PrimitiveType.TIMESTAMP);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT TIMESTAMP '2013-01-11 12:13:14.123'", expr.toUpperCase());
	}
	
	public void testNewDefault6() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("'2013-01-11 12:13:14'::timestamp without time zone", PrimitiveType.TIMESTAMP);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT TIMESTAMP '2013-01-11 12:13:14'", expr.toUpperCase());
	}
	
	public void testNewDefault7() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("'2013-01-01'::date", PrimitiveType.DATE);
						
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT DATE '2013-01-01'", expr.toUpperCase());
	}
	
}
