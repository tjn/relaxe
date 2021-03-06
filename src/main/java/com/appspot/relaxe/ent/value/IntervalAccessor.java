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
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.types.IntervalType;
import com.appspot.relaxe.value.Interval;
import com.appspot.relaxe.value.IntervalHolder;

public abstract class IntervalAccessor<
	A extends AttributeName,
	E,
	B,
	V extends Interval<V>, 
	P extends IntervalType<P>, 
	H extends IntervalHolder<V, P, H>,
	K extends IntervalAttribute<A, E, B, V, P, H, K>
>
	extends AbstractAttributeAccessor<A, E, B, V, P, H, K> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6884358447490019294L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected IntervalAccessor() {
	}

	public IntervalAccessor(B target, K k) {
		super(target, k);
	}
	
	public static class YearMonth<
		A extends AttributeName,		
		R extends HasYearMonthInterval.Read<A, R, RW>,
		RW extends HasYearMonthInterval.Write<A, R, RW> & HasYearMonthInterval.Read<A, R, RW> 
	>
		extends IntervalAccessor<A, R, RW, Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth, IntervalAttribute.YearMonth<A, R, RW>> {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 6124569175841369365L;
				
		/**
			 * No-argument constructor for GWT Serialization
			 */
		@SuppressWarnings("unused")
		private YearMonth() {	
		}
	
		public YearMonth(RW target, IntervalAttribute.YearMonth<A, R, RW> k) {
			super(target, k);		
		}
		
		@Override
		public com.appspot.relaxe.value.IntervalHolder.YearMonth getHolder() {
			return getTarget().getInterval(key());
		}
	}	
	
	public static class DayTime<
		A extends AttributeName,		
		R extends HasDayTimeInterval.Read<A, R, RW>,
		RW extends HasDayTimeInterval.Write<A, R, RW> & HasDayTimeInterval.Read<A, R, RW> 
	>
		extends IntervalAccessor<A, R, RW, Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime, IntervalAttribute.DayTime<A, R, RW>> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6124569175841369365L;
				
		/**
			 * No-argument constructor for GWT Serialization
			 */
		@SuppressWarnings("unused")
		private DayTime() {	
		}

		public DayTime(RW target, IntervalAttribute.DayTime<A, R, RW> k) {
			super(target, k);		
		}
		
		@Override
		public com.appspot.relaxe.value.IntervalHolder.DayTime getHolder() {
			return getTarget().getInterval(key());
		}
	}
}