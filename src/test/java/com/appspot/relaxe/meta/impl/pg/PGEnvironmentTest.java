/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.meta.impl.pg;

import com.appspot.relaxe.env.pg.PGEnvironment;
import com.appspot.relaxe.env.pg.PGIdentifierRules;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.IllegalIdentifierException;
import com.appspot.relaxe.expr.ddl.DefaultDefinition;
import com.appspot.relaxe.types.ValueType;

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
		DefaultDefinition dd = env.newDefaultDefinition("now()", ValueType.TIMESTAMP);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT CURRENT_TIMESTAMP", expr.toUpperCase());
	}
	
	public void testNewDefault2() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("('now'::text)::date", ValueType.DATE);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT CURRENT_DATE", expr.toUpperCase());
	}
	
	public void testNewDefault3() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("123", ValueType.INTEGER);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT 123", expr.toUpperCase());
	}
	
	public void testNewDefault4() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("123123123123", ValueType.BIGINT);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT 123123123123", expr.toUpperCase());
	}
	
	public void testNewDefault5() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("'2013-01-11 12:13:14.123'::timestamp without time zone", ValueType.TIMESTAMP);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT TIMESTAMP '2013-01-11 12:13:14.123'", expr.toUpperCase());
	}
	
	public void testNewDefault6() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("'2013-01-11 12:13:14'::timestamp without time zone", ValueType.TIMESTAMP);
		
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT TIMESTAMP '2013-01-11 12:13:14'", expr.toUpperCase());
	}
	
	public void testNewDefault7() throws Exception {
		PGEnvironment env = PGEnvironment.environment();
		DefaultDefinition dd = env.newDefaultDefinition("'2013-01-01'::date", ValueType.DATE);
						
		String expr = dd.generate();
		logger().debug("expr: " + expr);
		assertNotNull(expr);
		assertEquals("DEFAULT DATE '2013-01-01'", expr.toUpperCase());
	}
	
}
