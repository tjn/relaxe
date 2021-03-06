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
package com.appspot.relaxe.rpc;

// import java.sql.Date;

import java.sql.Time;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.value.Interval;
import com.appspot.relaxe.value.Interval.DayTime;
import com.appspot.relaxe.value.Interval.YearMonth;


public class IntervalTest extends TestCase {
	
	private static Logger logger = LoggerFactory.getLogger(IntervalTest.class);
	
	public void testYearMonthInterval() {		
		Interval.YearMonth ym = new Interval.YearMonth(1, -1);
		assertEquals(0, ym.getYear());
		assertEquals(11, ym.getMonth());
		assertEquals(1, ym.signum());			
		
		ym = new Interval.YearMonth(0, -1);
		assertEquals(0, ym.getYear());
		assertEquals(-1, ym.getMonth());
		assertEquals(-1, ym.signum());
		
		ym = new Interval.YearMonth(-1, 1);
		assertEquals(0, ym.getYear());
		assertEquals(-11, ym.getMonth());
		assertEquals(-1, ym.signum());

		ym = new Interval.YearMonth(0, 0);
		assertEquals(0, ym.getYear());
		assertEquals(0, ym.getMonth());
		assertEquals(0, ym.signum());

		ym = new Interval.YearMonth(1, -24);
		assertEquals(-1, ym.getYear());
		assertEquals(0, ym.getMonth());
		assertEquals(-1, ym.signum());
		
		testEqualsWithNull(ym);				
		assertTrue(ym.equals(ym));
	}

	private void testEqualsWithNull(Object o) {
		try {
			o.equals(null);
			fail(NullPointerException.class);
		}
		catch (NullPointerException e) {
			// OK
		}
	}	
	
	private void fail(Class<? extends Exception> e) {
		fail("exception of type " + e + " was not thrown");
	}

	public void testYearMonthCompare() {		
		YearMonth a = new Interval.YearMonth(3, 3);
		YearMonth b = new Interval.YearMonth(2, 4);
		
		assertEquals(0, a.compareTo(a));
		assertEquals(1, a.compareTo(b));
		assertEquals(-1, b.compareTo(a));		

		a = new Interval.YearMonth(-2, 11);
		b = new Interval.YearMonth(-4, -12);
		
		assertEquals(0, a.compareTo(a));
		assertEquals(1, a.compareTo(b));
		assertEquals(-1, b.compareTo(a));		

		a = new Interval.YearMonth(0, 5);
		b = new Interval.YearMonth(0, -6);
		
		assertEquals(0, a.compareTo(a));
		assertEquals(1, a.compareTo(b));
		assertEquals(-1, b.compareTo(a));		
	}
	
	

	public void testDayTimeInterval() {
		Interval.DayTime ti = new Interval.DayTime(0, 0, 0, -300D);
		assertEquals(0, ti.getDay());
		assertEquals(0, ti.getHour());
		assertEquals(-5, ti.getMinute());		
		assertEquals(0.0D, ti.getSecond());

		ti = new Interval.DayTime(1, -12);
		assertEquals(0, ti.getDay());
		assertEquals(12, ti.getHour());
		assertEquals(0, ti.getMinute());		
		assertEquals(0.0D, ti.getSecond());
		
		ti = new Interval.DayTime(1, -1, 0);
		assertEquals(0, ti.getDay());
		assertEquals(0, ti.getHour());
		assertEquals(59, ti.getMinute());		
		assertEquals(0.0D, ti.getSecond());
		
		ti = new Interval.DayTime(-1, -1, 0);
		assertEquals(0, ti.getDay());
		assertEquals(-1, ti.getHour());
		assertEquals(-1, ti.getMinute());		
		assertEquals(0.0D, ti.getSecond());		

		ti = new Interval.DayTime(-1, 1, 0);
		assertEquals(0, ti.getDay());
		assertEquals(0, ti.getHour());
		assertEquals(-59, ti.getMinute());		
		assertEquals(0.0D, ti.getSecond());		

		ti = new Interval.DayTime(-1, 1);
		assertEquals(0, ti.getDay());
		assertEquals(-23, ti.getHour());

		ti = new Interval.DayTime(-1, -1);
		assertEquals(-1, ti.getDay());
		assertEquals(-1, ti.getHour());

		ti = new Interval.DayTime(0, -1, 2, 0);
		assertEquals(0, ti.getDay());
		assertEquals(0, ti.getHour());
		assertEquals(-58, ti.getMinute());
		assertEquals(0D, ti.getSecond());

		ti = new Interval.DayTime(0, 2, 30, -15);
		assertEquals(0, ti.getDay());
		assertEquals(2, ti.getHour());
		assertEquals(29, ti.getMinute());
		assertEquals(45D, ti.getSecond());

		ti = new Interval.DayTime(0, 2, 30, -65D);
		assertEquals(0, ti.getDay());
		assertEquals(2, ti.getHour());
		assertEquals(28, ti.getMinute());
		assertEquals(55D, ti.getSecond());

		ti = new Interval.DayTime(0, 2, 30, -(45 * 60));
		assertEquals(0, ti.getDay());
		assertEquals(1, ti.getHour());
		assertEquals(45, ti.getMinute());
		assertEquals(0D, ti.getSecond());

		ti = new Interval.DayTime(1, -48, 0, 0);
		assertEquals(-1, ti.getDay());
		assertEquals(0, ti.getHour());
		assertEquals(-1, ti.signum());
		
		ti = new Interval.DayTime(-1, -48, 0, 0);
		assertEquals(-3, ti.getDay());
		assertEquals(0, ti.getHour());
		assertEquals(-1, ti.signum());

		ti = new Interval.DayTime(-1, -12, 125, 0);
		assertEquals(-1, ti.getDay());
		assertEquals(-9, ti.getHour());
		assertEquals(-55, ti.getMinute());
		assertEquals(-1, ti.signum());
		
		
		ti = new Interval.DayTime(1, -2 * 24);
		logger().debug("testDayTimeInterval: ti=" + ti);
		assertEquals(-1, ti.getDay());
		assertEquals(0, ti.getHour());
		assertEquals(0, ti.getMinute());
		assertEquals(-1, ti.signum());
		
		
		ti = new Interval.DayTime(1, -2 * 24, 3 * 24 * 60, 0D);
		logger().debug("testDayTimeInterval: ti=" + ti);
		assertEquals(2, ti.getDay());
		assertEquals(0, ti.getHour());
		assertEquals(0, ti.getMinute());
		assertEquals(1, ti.signum());

		
		// 1 - 2 + 3 - 4 = -2
		ti = new Interval.DayTime(1, -2 * 24, 3 * 24 * 60, -4 * 24 * 60 * 60);
		logger().debug("testDayTimeInterval: ti=" + ti);
		
		assertEquals(-2, ti.getDay());
		assertEquals(0, ti.getHour());
		assertEquals(0, ti.getMinute());
		assertEquals(-1, ti.signum());
		
		testEqualsWithNull(ti);
		assertEqualsTrue(ti);

		assertFalse(ti.equals(new DayTime(ti.getDay() + 1, ti.getHour(), ti.getMinute(), ti.getSecond())));
		assertFalse(ti.equals(new DayTime(ti.getDay(), ti.getHour() + 1, ti.getMinute(), ti.getSecond())));
		assertFalse(ti.equals(new DayTime(ti.getDay(), ti.getHour(), ti.getMinute() + 1, ti.getSecond())));
		assertFalse(ti.equals(new DayTime(ti.getDay(), ti.getHour(), ti.getMinute(), ti.getSecond() + 1)));		
	}

	private void assertEqualsTrue(Interval.DayTime ti) {
		assertTrue(ti.equals(ti));
		DayTime dt = new DayTime(ti.getDay(), ti.getHour(), ti.getMinute(), ti.getSecond());
		assertTrue(ti.equals(dt));
		assertEquals(ti.hashCode(), dt.hashCode());
	}
	
	public void testDayTimeCompare() {		
		DayTime a = new Interval.DayTime(3, 3);
		DayTime b = new Interval.DayTime(2, 4);
		
		assertEquals(0, a.compareTo(a));
		assertEquals(1, a.compareTo(b));
		assertEquals(-1, b.compareTo(a));		

		a = new Interval.DayTime(-2, 11);
		b = new Interval.DayTime(-4, -12);
		
		assertEquals(0, a.compareTo(a));
		assertEquals(1, a.compareTo(b));
		assertEquals(-1, b.compareTo(a));

		a = new Interval.DayTime(0, 0, 0, -0.1);
		b = new Interval.DayTime(0, 0, 0, 0.2);
		assertEquals(0, a.compareTo(a));
		assertEquals(-1, a.compareTo(b));
		assertEquals(+1, b.compareTo(a));
	}
	
	
	
	public void testDayTimeConstructor() {
		Date d = new Date();
		logger().debug("testDayTimeConstructor: d.getTime()=" + d.getTime());
				
		Time t = new Time(d.getTime());
		logger().debug("testDayTimeConstructor: t.getTime()=" + t.getTime());
		
		
		TimeZone tz = TimeZone.getDefault();
		logger().debug("tz=" + tz);
		
//		Calendar cal = new GregorianCalendar();
//		cal.setTime(d);
//		cal.set(year, month, date, hourOfDay, minute, second);
		// cal.get
						
		Interval.DayTime dt = new Interval.DayTime(t);		
		logger().debug("testDayTimeConstructor: dt=" + dt);
		logger().debug("testDayTimeConstructor: hh=" + dt.getHour());
		logger().debug("testDayTimeConstructor: min=" + dt.getMinute());
	}
	
	private static Logger logger() {
		return IntervalTest.logger;
	}
}
