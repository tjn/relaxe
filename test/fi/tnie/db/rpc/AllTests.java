/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for fi.tnie.db.rpc");
		//$JUnit-BEGIN$
		suite.addTestSuite(DecimalTest.class);
		suite.addTestSuite(IntervalTest.class);
		//$JUnit-END$
		return suite;
	}

}
