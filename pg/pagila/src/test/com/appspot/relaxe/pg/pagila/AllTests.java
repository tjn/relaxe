/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.appspot.relaxe.pg.pagila");
		//$JUnit-BEGIN$
		suite.addTestSuite(PagilaAlterTableAddForeignKeyTest.class);
		suite.addTestSuite(PagilaAlterTableAddPrimaryKeyTest.class);
		suite.addTestSuite(PagilaCreateTableTest.class);
		suite.addTestSuite(PagilaDataAccessSessionTest.class);
		suite.addTestSuite(PagilaDefaultEntityQueryTest.class);
		suite.addTestSuite(PagilaEntityQueryElementTest.class);
		suite.addTestSuite(PagilaEntityQueryExecutorTest.class);
		suite.addTestSuite(PagilaIntegerKeyTest.class);
		suite.addTestSuite(PagilaPersistenceManagerTest.class);
		suite.addTestSuite(PagilaStatementExecutorTest.class);
		suite.addTestSuite(PagilaUnificationContextTest.class);
		//$JUnit-END$
		return suite;
	}

}
