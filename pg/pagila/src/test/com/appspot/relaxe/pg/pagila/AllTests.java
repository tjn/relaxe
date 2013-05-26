/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.appspot.relaxe.meta.impl.pg.pagila");
		//$JUnit-BEGIN$
		suite.addTestSuite(PagilaEntityQueryExecutorTest.class);
		suite.addTestSuite(PagilaDataAccessSessionTest.class);
		suite.addTestSuite(PagilaPersistenceManagerTest.class);
		suite.addTestSuite(PagilaUnificationContextTest.class);		
		//$JUnit-END$
		return suite;
	}

}