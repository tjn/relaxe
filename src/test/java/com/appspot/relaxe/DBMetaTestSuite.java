/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.textui.TestRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBMetaTestSuite
    extends TestCase {
          
    private static Logger logger = LoggerFactory.getLogger(DBMetaTestSuite.class);
    
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
