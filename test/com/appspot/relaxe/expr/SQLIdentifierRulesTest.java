/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import org.apache.log4j.Logger;

import com.appspot.relaxe.meta.Folding;
import com.appspot.relaxe.meta.SQLIdentifierRules;

import junit.framework.TestCase;

public class SQLIdentifierRulesTest extends TestCase {
	
	private static Logger logger = Logger.getLogger(SQLIdentifierRulesTest.class);
	
	private SQLIdentifierRules sir = new SQLIdentifierRules();

	public void testStartVisitContextIdentifier1() {
		StringBuilder buf = new StringBuilder();
		QueryGenerator g = new QueryGenerator(buf);
		Identifier ident = sir.toIdentifier("Lost+Found");
		assertFalse(ident.isOrdinary());
		ident.traverse(null, g);
		assertEquals("\"Lost+Found\"", buf.toString());
	}
	
	public void testStartVisitContextIdentifier2() {
		StringBuilder buf = new StringBuilder();
		QueryGenerator g = new QueryGenerator(buf);		
		Identifier ident = sir.toIdentifier("My_Table");
		assertTrue(ident.isOrdinary());
		ident.traverse(null, g);
		assertEquals("MY_TABLE", buf.toString());
	}

	
	
//	public void testStartVisitContextIdentifier4() {
//		StringBuilder buf = new StringBuilder();
//		QueryGenerator g = new QueryGenerator(buf, true);		
//		Identifier ident = AbstractIdentifier.create("My_Table", Folding.LOWERCASE);
//		assertTrue(ident.isOrdinary());
//		ident.traverse(null, g);
//		assertEquals("\"my_table\"", buf.toString());
//	}

	private static Logger logger() {
		return SQLIdentifierRulesTest.logger;
	}

}
