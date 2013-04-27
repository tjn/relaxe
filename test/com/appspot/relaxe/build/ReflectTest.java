/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.build;

import org.apache.log4j.Logger;

import com.appspot.relaxe.rpc.Interval;


public class ReflectTest {
	
	private static Logger logger = Logger.getLogger(ReflectTest.class);

	public static void main(String[] args) {
		
		
		Class<?> t = Interval.DayTime.class;
						
		dump(Interval.DayTime.class);
		dump(Interval.DayTime.class.getEnclosingClass());
		dump(Interval.DayTime.class.getSuperclass());
		
		
	}

	private static void dump(Class<?> c) {
		logger().debug("dump: c=" + c);
		
		if(c == null) {
			return;
		}
		
		logger().debug("c.getCanonicalName()=" + c.getCanonicalName());
		logger().debug("c.getSimpleName()=" + c.getSimpleName());
	}
	
	private static Logger logger() {
		return ReflectTest.logger;
	}
	
}
