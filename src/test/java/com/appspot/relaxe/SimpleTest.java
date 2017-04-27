package com.appspot.relaxe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class SimpleTest
	extends TestCase {

	private static Logger logger = LoggerFactory.getLogger(SimpleTest.class);
	
	
	public void testMultiplication() {
		
		long m1 = Integer.MAX_VALUE;
		long result = m1 * m1;
		
		
		logger.info("result : {}", result);
		logger.info("max : {}", Long.MAX_VALUE);
		
		long m2 = 47900160L;		        
		
		for (int i = 1; i < 5; i++) {
			m2 *= 10;
			logger.info("m1 * m2 = {} ({}, {})", m1 * m2 , m1, m2);
		}
		
		
	}
	
}
