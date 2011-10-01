/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import fi.tnie.db.env.pg.PGImplementation;

public class MyTestSuite {
	
	private static Logger logger = Logger.getLogger(MyTestSuite.class);
			
	public static void main(String[] args) {
		try {		
			InputStream in = null;
			
			if (args.length == 0) {
				in = MyTestSuite.class.getResourceAsStream("/classlist");
			}
			else {
				in = new FileInputStream(args[0]);
			}
			
			Class<?> tt = AbstractUnitTest.class;
			ArrayList<Class<?>> cases = new ArrayList<Class<?>>();			
			int cc = findImplementors(in, tt, cases);
			
			logger().debug("test cases: " + cc);
			
			TestSuite ts = new TestSuite();
		
			
			
			for (Class<?> c : cases) {
				ts.addTestSuite(c);
			}
						
			TestResult tr = new TestResult();
			
			tr.addListener(new TestListener() {
				@Override
				public void addError(Test test, Throwable t) {
					
				}

				@Override
				public void addFailure(Test test, AssertionFailedError t) {					
				}

				@Override
				public void endTest(Test test) {
 					
				}

				@Override
				public void startTest(Test test) {
					logger().debug("starting: " + test);
				}				
			});
			
			ts.run(tr);
			
			logger().info("results: " + tr.errorCount());
			logger().info("error: " + tr.errorCount());
			logger().info("failures: " + tr.failureCount());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static int findImplementors(InputStream in, Class<?> tt, List<Class<?>> dest) {
		Scanner s = new Scanner(in);
		
		int count = 0;
		
		try {
			while(s.hasNextLine()) {
				String line = s.nextLine();
				
				try {
					Class<?> tc = Class.forName(line);					
										
					if (tt.isAssignableFrom(tc) && isInstantiable(tc)) {
						logger().debug(tc);
						dest.add(tc);
						count++;
					}
				}
				catch (ClassNotFoundException e) {
					// logger().debug("NOT FOUND: " + e.getMessage());
				}
			}
		}
		finally {
			s.close();
		}
		
		return count;
	}
	
	private static boolean isInstantiable(Class<?> tc) {
		return
			((tc.getModifiers() & Modifier.ABSTRACT) != Modifier.ABSTRACT) && 
			(!tc.isAnonymousClass()) && 
			(!tc.isLocalClass()) &&
			(!tc.isSynthetic()) &&
			(!tc.isInterface());
	}


	public static Logger logger() {
		return logger;
	}
}
