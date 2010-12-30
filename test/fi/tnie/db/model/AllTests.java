/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for fi.tnie.db.model");
		//$JUnit-BEGIN$
		suite.addTestSuite(ComputedModelTest.class);
		suite.addTestSuite(ValueModelTest.class);
		suite.addTestSuite(TransformationModelTest.class);
		suite.addTestSuite(ConstrainedModelTest.class);
		//$JUnit-END$
		return suite;
	}

}
