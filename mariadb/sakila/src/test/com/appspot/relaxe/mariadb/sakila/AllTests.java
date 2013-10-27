/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mariadb.sakila;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.appspot.relaxe.mariadb.sakila");
		//$JUnit-BEGIN$		
		suite.addTestSuite(SakilaEntityQueryExecutorTest.class);
		//$JUnit-END$
		return suite;
	}

}
