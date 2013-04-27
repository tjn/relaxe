/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import com.appspot.relaxe.SQLIntervalExtractor;
import com.appspot.relaxe.env.pg.PGIntervalExtractor;
import com.appspot.relaxe.rpc.IntervalHolder;

import junit.framework.TestCase;

public class IntervalExtractorTest extends TestCase {

	
	public void testPattern() {
		PGIntervalExtractor.YearMonth ye = new PGIntervalExtractor.YearMonth(1);
				
		assertTrue(IntervalHolder.YearMonth.NULL == ye.create(null));
		com.appspot.relaxe.rpc.Interval.YearMonth ym = null; 
			
		ym = ye.create("00:00:00").value();
		assertNotNull(ym);
		assertEquals(0, ym.getYear());		
		assertEquals(0, ym.getMonth());		

		ym = ye.create("1 year").value();
		assertNotNull(ym);
		assertEquals(1, ym.getYear());		
		assertEquals(0, ym.getMonth());

		ym = ye.create("-1 years").value();
		assertNotNull(ym);
		assertEquals(-1, ym.getYear());		
		assertEquals(0, ym.getMonth());

		ym = ye.create("-2 years -5 mons").value();
		assertNotNull(ym);
		assertEquals(-2, ym.getYear());		
		assertEquals(-5, ym.getMonth());

		ym = ye.create("6 mons").value();
		assertNotNull(ym);
		assertEquals(0, ym.getYear());		
		assertEquals(6, ym.getMonth());
	}
	

	
	public void testPatternDT() {
		PGIntervalExtractor.DayTime de = new PGIntervalExtractor.DayTime(1);
				
		assertTrue(IntervalHolder.DayTime.NULL == de.create(null));
		com.appspot.relaxe.rpc.Interval.DayTime ti = null; 
			
		ti = de.create("00:00:00").value();
		assertNotNull(ti);
		assertEquals(0, ti.getDay());		
		assertEquals(0, ti.getHour());		
		assertEquals(0, ti.getMinute());
		assertEquals(0D, ti.getSecond());

		ti = de.create("-2 days").value();
		assertNotNull(ti);
		assertEquals(-2, ti.getDay());		
		assertEquals(0, ti.getHour());		
		assertEquals(0, ti.getMinute());
		assertEquals(0D, ti.getSecond());

		ti = de.create("-2 days -02:44:44").value();
		assertNotNull(ti);
		assertEquals(-2, ti.getDay());		
		assertEquals(-2, ti.getHour());		
		assertEquals(-44, ti.getMinute());
		assertEquals(-44D, ti.getSecond());

		ti = de.create("-2 days -02:30:45.123").value();
		assertNotNull(ti);
		assertEquals(-2, ti.getDay());		
		assertEquals(-2, ti.getHour());		
		assertEquals(-30, ti.getMinute());
		assertEquals(-45.123D, ti.getSecond());

		ti = de.create("00:00:00.123123").value();
		assertNotNull(ti);
		assertEquals(0, ti.getDay());		
		assertEquals(0, ti.getHour());		
		assertEquals(0, ti.getMinute());
		assertEquals(.123123D, ti.getSecond());

		ti = de.create("-00:00:00.123123").value();
		assertNotNull(ti);
		assertEquals(0, ti.getDay());		
		assertEquals(0, ti.getHour());		
		assertEquals(0, ti.getMinute());
		assertEquals(-.123123D, ti.getSecond());
	}
	
	public void testPatternSQLDayTime() {
		SQLIntervalExtractor.DayTime de = new SQLIntervalExtractor.DayTime(1);
				
		assertTrue(IntervalHolder.DayTime.NULL == de.create(null));
		com.appspot.relaxe.rpc.Interval.DayTime ti = null; 
			
		ti = de.create("00:00:00").value();
		assertNotNull(ti);
		assertEquals(0, ti.getDay());		
		assertEquals(0, ti.getHour());		
		assertEquals(0, ti.getMinute());
		assertEquals(0D, ti.getSecond());

		ti = de.create("-2 00:00:00").value();
		assertNotNull(ti);
		assertEquals(-2, ti.getDay());		
		assertEquals(0, ti.getHour());		
		assertEquals(0, ti.getMinute());
		assertEquals(0D, ti.getSecond());

		ti = de.create("-2 -02:44:44").value();
		assertNotNull(ti);
		assertEquals(-2, ti.getDay());		
		assertEquals(-2, ti.getHour());		
		assertEquals(-44, ti.getMinute());
		assertEquals(-44D, ti.getSecond());

		ti = de.create("-2 -02:30:45.123").value();
		assertNotNull(ti);
		assertEquals(-2, ti.getDay());		
		assertEquals(-2, ti.getHour());		
		assertEquals(-30, ti.getMinute());
		assertEquals(-45.123D, ti.getSecond());

		ti = de.create("00:00:00.123123").value();
		assertNotNull(ti);
		assertEquals(0, ti.getDay());		
		assertEquals(0, ti.getHour());		
		assertEquals(0, ti.getMinute());
		assertEquals(.123123D, ti.getSecond());

		ti = de.create("-00:00:00.123123").value();
		assertNotNull(ti);
		assertEquals(0, ti.getDay());		
		assertEquals(0, ti.getHour());		
		assertEquals(0, ti.getMinute());
		assertEquals(-.123123D, ti.getSecond());
	}
	
	public void testPatternSQLYearMonth() {
		SQLIntervalExtractor.YearMonth de = new SQLIntervalExtractor.YearMonth(1);
		com.appspot.relaxe.rpc.Interval.YearMonth ym = null;
		
		ym = de.create("1-4").value();
		assertNotNull(ym);
		assertEquals(1, ym.getYear());		
		assertEquals(4, ym.getMonth());
		
		ym = de.create("-2-3").value();
		assertNotNull(ym);
		assertEquals(-2, ym.getYear());		
		assertEquals(-3, ym.getMonth());

		ym = de.create("1-12").value();
		assertNotNull(ym);
		assertEquals(2, ym.getYear());		
		assertEquals(0, ym.getMonth());

		ym = de.create("1-15").value();
		assertNotNull(ym);
		assertEquals(2, ym.getYear());		
		assertEquals(3, ym.getMonth());

		ym = de.create("-1-15").value();
		assertNotNull(ym);
		assertEquals(-2, ym.getYear());		
		assertEquals(-3, ym.getMonth());

		ym = de.create("0-0").value();
		assertNotNull(ym);
		assertEquals(0, ym.getYear());		
		assertEquals(0, ym.getMonth());

		ym = de.create("-0-0").value();
		assertNotNull(ym);
		assertEquals(0, ym.getYear());		
		assertEquals(0, ym.getMonth());

		ym = de.create("-0-7").value();
		assertNotNull(ym);
		assertEquals(0, ym.getYear());		
		assertEquals(-7, ym.getMonth());

	}

	public void testPatternSQLDayTimeFractionSign() {
		SQLIntervalExtractor.DayTime de = new SQLIntervalExtractor.DayTime(1);
		com.appspot.relaxe.rpc.Interval.DayTime ti = null; 

		ti = de.create("-00:00:00.123123").value();
		assertNotNull(ti);
		assertEquals(0, ti.getDay());		
		assertEquals(0, ti.getHour());		
		assertEquals(0, ti.getMinute());
		assertEquals(-.123123D, ti.getSecond());
	}	

}
