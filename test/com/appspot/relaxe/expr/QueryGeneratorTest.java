/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import org.apache.log4j.Logger;

import com.appspot.relaxe.meta.Folding;

import junit.framework.TestCase;

public class QueryGeneratorTest extends TestCase {
	
	private static Logger logger = Logger.getLogger(QueryGeneratorTest.class);

	public void testStartVisitContextIdentifier1() {
		StringBuilder buf = new StringBuilder();
		QueryGenerator g = new QueryGenerator(buf);		
		Identifier ident = AbstractIdentifier.create("Lost+Found");
		assertFalse(ident.isOrdinary());
		ident.traverse(null, g);
		assertEquals("\"Lost+Found\"", buf.toString());
	}
	
	public void testStartVisitContextIdentifier2() {
		StringBuilder buf = new StringBuilder();
		QueryGenerator g = new QueryGenerator(buf);		
		Identifier ident = AbstractIdentifier.create("My_Table", Folding.UPPERCASE);
		assertTrue(ident.isOrdinary());
		ident.traverse(null, g);
		assertEquals("MY_TABLE", buf.toString());
	}
	
	public void testStartVisitContextIdentifier3() {
		StringBuilder buf = new StringBuilder();
		QueryGenerator g = new QueryGenerator(buf);		
		Identifier ident = AbstractIdentifier.create("My_Table", Folding.LOWERCASE);
		assertTrue(ident.isOrdinary());
		ident.traverse(null, g);
		assertEquals("my_table", buf.toString());
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
		return QueryGeneratorTest.logger;
	}

}
