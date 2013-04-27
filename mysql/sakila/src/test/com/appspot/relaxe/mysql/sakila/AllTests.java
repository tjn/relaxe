/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.sakila;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.appspot.relaxe.mysql.sakila");
		//$JUnit-BEGIN$
		suite.addTestSuite(SakilaPersistenceManagerTest.class);
		suite.addTestSuite(SakilaEntityQueryExecutorTest.class);
		//$JUnit-END$
		return suite;
	}

}
