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
package com.appspot.relaxe.value;

import com.appspot.relaxe.types.IntervalType;
import com.appspot.relaxe.types.AbstractValueType;
import com.appspot.relaxe.types.ValueType;

public abstract class IntervalHolder<V extends Interval<?>, T extends AbstractValueType<T>, H extends IntervalHolder<V, T, H>>
	extends AbstractValueHolder<V, T, H> {
	
	private V value;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479889538730689743L;

	
	public static class YearMonth
		extends IntervalHolder<Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1716128274518368604L;
		public static final IntervalHolder.YearMonth NULL = new IntervalHolder.YearMonth();
		
		
		public YearMonth() {
			super();
		}

		public YearMonth(Interval.YearMonth value) {
			super(value);		
		}

		@Override
		public com.appspot.relaxe.types.IntervalType.YearMonth getType() {
			return IntervalType.YearMonth.TYPE;
		}
		
		public static IntervalHolder.YearMonth valueOf(Interval.YearMonth value) {
			return (value == null) ? NULL : new YearMonth(value);
		}
		
		@Override
		public YearMonth asYearMonthIntervalHolder() {
			return this;
		}

		@Override
		public YearMonth self() {		
			return this;
		}
		
		public static IntervalHolder.YearMonth of(AbstractValueHolder<?, ?, ?> holder) {
			return holder.asYearMonthIntervalHolder();
		}
	}
	
	
	public static class DayTime
		extends IntervalHolder<Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime> {
			
		/**
		 * 
		 */
		private static final long serialVersionUID = -8694953710513403392L;
		
		public static final IntervalHolder.DayTime NULL = new IntervalHolder.DayTime();

		public DayTime() {
			super();
		}
	
		public DayTime(Interval.DayTime value) {
			super(value);		
		}
	
		@Override
		public com.appspot.relaxe.types.IntervalType.DayTime getType() {
			return IntervalType.DayTime.TYPE;
		}
		
		public static IntervalHolder.DayTime valueOf(Interval.DayTime value) {
			return (value == null) ? NULL : new DayTime(value);
		}
		
		@Override
		public IntervalHolder.DayTime asDayTimeIntervalHolder() {
			return this;
		}
		
		public static IntervalHolder.DayTime of(ValueHolder<?, ?, ?> holder) {
			return holder.asDayTimeIntervalHolder();
		}

		@Override
		public DayTime self() {
			return this;
		}
	}	
	
	/**
	 * TODO: should we have size?
	 */
		
	public IntervalHolder() {
		super();
	}

	
	@Override
	public V value() {		
		return value;
	}

	public IntervalHolder(V value) {
		super();
		this.value = value;
	}
	
	@Override
	public int getSqlType() {
		return ValueType.OTHER;
	}
}
