/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.appspot.relaxe.rpc");
		//$JUnit-BEGIN$
		suite.addTestSuite(DecimalTest.class);
		suite.addTestSuite(IntervalTest.class);
		//$JUnit-END$
		return suite;
	}

}
