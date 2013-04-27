/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.appspot.relaxe.model");
		//$JUnit-BEGIN$
		suite.addTestSuite(ComputedModelTest.class);
		suite.addTestSuite(ValueModelTest.class);
		suite.addTestSuite(TransformationModelTest.class);
		suite.addTestSuite(ConstrainedModelTest.class);
		//$JUnit-END$
		return suite;
	}

}
