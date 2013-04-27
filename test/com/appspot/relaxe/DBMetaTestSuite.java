/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.textui.TestRunner;

import org.apache.log4j.Logger;

public class DBMetaTestSuite
    extends TestCase {
          
    private static Logger logger = Logger.getLogger(DBMetaTestSuite.class);
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        int failed = 0;
        
        try {
            TestResult result = TestRunner.run(suite());
            failed = result.failureCount();
        }
        catch (Throwable e) {
            logger().error(e.getMessage(), e);
            failed = -1;
        }
        
        System.exit(failed);
    }
    
    public static Test suite() {
        
        TestSuiteBuilder tb = new TestSuiteBuilder();                
        Test suite = null;
        
        try {
            suite = tb.createSuite(new File("test-config"));
        } 
        catch (Exception e) {        
            logger().error(e.getMessage(), e);
        }
        
        return suite;
    }

    public static Logger logger() {
        return DBMetaTestSuite.logger;
    }     

}
