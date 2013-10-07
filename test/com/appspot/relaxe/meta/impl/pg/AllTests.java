/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for com.appspot.relaxe.meta.impl.pg");
        //$JUnit-BEGIN$
        suite.addTestSuite(PGEnvironmentTest.class);
//        suite.addTestSuite(PGDumpMetaTest.class);
//        suite.addTestSuite(PGRestoreTest.class);
//        suite.addTestSuite(PGWellKnownTableTest.class);
        //$JUnit-END$
        return suite;
    }

}
