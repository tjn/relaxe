/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestSuites {

	public static Test suite() {
		TestSuite suite = new TestSuite("All tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(fi.tnie.db.meta.impl.pg.pagila.AllTests.class);		
		suite.addTestSuite(fi.tnie.db.meta.impl.mysql.sakila.SakilaPersistenceManagerTest.class);
		// suite.addTest(AllTests.suite());
		//$JUnit-END$
		return suite;
	}
}
