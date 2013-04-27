/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.appspot.relaxe.paging");
		//$JUnit-BEGIN$
		suite.addTestSuite(PagerTest.class);
		//$JUnit-END$
		return suite;
	}

}
