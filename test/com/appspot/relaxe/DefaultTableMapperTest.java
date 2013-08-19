/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import org.apache.log4j.Logger;

import com.appspot.relaxe.map.TableMapper;

import junit.framework.TestCase;

public class DefaultTableMapperTest extends TestCase {

	private static Logger logger = Logger.getLogger(DefaultTableMapperTest.class);
	
	public DefaultTableMapperTest() {		
	}
	
	public void testToJavaIdentifier() {
		TableMapper tm = newMapper();
				
		String[] eqi = { "Id", "ID", "id", "GC_ed" };
		
		for(String s : eqi)	{			
			testIdentityTransform(tm, s);
		}
	}
	
	public void testToJavaIdentifierEmpty() {
		TableMapper tm = newMapper();
		String jid = tm.toJavaIdentifier("");
		assertNotNull(jid);
		assertEquals("_", jid);		
	}
	

	public void testToJavaIdentifier2() {
		TableMapper tm = newMapper();
		String jid = tm.toJavaIdentifier("R&D  ");
		assertNotNull(jid);
		assertEquals("RD", jid);		
	}
	
	public void testToJavaIdentifier3() {		
		TableMapper tm = newMapper();
		String jid = tm.toJavaIdentifier("123");
		assertNotNull(jid);
		assertEquals("_123", jid);		
	}


	public void testToJavaIdentifier4() {
		TableMapper tm = newMapper();
		String jid = tm.toJavaIdentifier("a$e");
		assertNotNull(jid);
		assertEquals("a$e", jid);		
	}
	
	public void testToJavaIdentifier5() {
		TableMapper tm = newMapper();
		String jid = tm.toJavaIdentifier("צה");
		assertNotNull(jid);
		assertEquals("צה", jid);		
	}

	protected TableMapper newMapper() {
		TableMapper tm = new DefaultTableMapper("root");
		return tm;
	}

	protected void testIdentityTransform(TableMapper tm, String s) {		
		logger().debug("testIdentityTransform: input='" + s + "'");
		String jid = tm.toJavaIdentifier(s);
		assertNotNull(jid);
		assertEquals(s, jid);
	}

	
	private static Logger logger() {
		return DefaultTableMapperTest.logger;
	}
}
