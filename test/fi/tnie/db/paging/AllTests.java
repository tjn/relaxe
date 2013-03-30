/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for fi.tnie.db.paging");
		//$JUnit-BEGIN$
		suite.addTestSuite(PagerTest.class);
		//$JUnit-END$
		return suite;
	}

}
