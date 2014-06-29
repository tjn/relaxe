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
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.Interval;
import com.appspot.relaxe.value.IntervalHolder;
import com.appspot.relaxe.value.ValueHolder;

public abstract class IntervalAttribute<
	A extends AttributeName,	
	E,
	B,
	V extends Interval<V>, 
	P extends ValueType<P>, 
	H extends IntervalHolder<V, P, H>, 
	K extends IntervalAttribute<A, E, B, V, P, H, K>
	>
	extends AbstractAttribute<A, E, B, V, P, H, K>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3226711133703096241L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected IntervalAttribute() {
	}

	protected IntervalAttribute(A name) {
		super(name);
	}
	

	public static final class YearMonth<
		A extends AttributeName,		
		R extends HasYearMonthInterval.Read<A, R, W>,
		W extends HasYearMonthInterval.Write<A, R, W>
	>
		extends IntervalAttribute<A, R, W, Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth, IntervalAttribute.YearMonth<A, R, W>> {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = -8221115262736792946L;

		/**
		 * No-argument constructor for GWT Serialization
		 */
		private YearMonth() {
		}

		private YearMonth(A name) {
			super(name);			
		}
		
		public static <
			X extends AttributeName,
			T extends HasYearMonthInterval.Read<X, T, S>,
			S extends HasYearMonthInterval.Write<X, T, S>			
		>
		IntervalAttribute.YearMonth<X, T, S> get(HasIntervalAttribute.YearMonth<X, T, S> meta, X a) {
			YearMonth<X, T, S> k = meta.getYearMonthIntervalAttribute(a);
		
			if (k == null) {
				if (IntervalType.YearMonth.TYPE.equals(a.type())) {
					k = new YearMonth<X, T, S>(a);
					meta.register(k);
				}
			}
					
			return k;
		}		

		@Override
		public com.appspot.relaxe.value.IntervalHolder.YearMonth get(R e) {	
			return e.getInterval(this);
		}

		@Override
		public com.appspot.relaxe.value.IntervalHolder.YearMonth newHolder(
				com.appspot.relaxe.value.Interval.YearMonth newValue) {
			return new IntervalHolder.YearMonth(newValue);
		}

		@Override
		public void set(W e, com.appspot.relaxe.value.IntervalHolder.YearMonth newValue) {
			e.setInterval(this, newValue);	
		}

		@Override
		public com.appspot.relaxe.types.IntervalType.YearMonth type() {
			return IntervalType.YearMonth.TYPE;
		}

		@Override
		public void copy(R src, W dest) {
			dest.setInterval(this, src.getInterval(this));
		}
		
		@Override
		public YearMonth<A, R, W> self() {
			return this;
		}
		
		@Override
		public com.appspot.relaxe.value.IntervalHolder.YearMonth as(ValueHolder<?, ?, ?> holder) {
			return holder.asYearMonthIntervalHolder();
		}
	}
	
	public static final class DayTime<
		A extends AttributeName,		
		R extends HasDayTimeInterval.Read<A, R, W>,
		W extends HasDayTimeInterval.Write<A, R, W>
	>
		extends IntervalAttribute<A, R, W, Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime, IntervalAttribute.DayTime<A, R, W>> {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = -8221115262736792946L;
	
		/**
		 * No-argument constructor for GWT Serialization
		 */
		private DayTime() {
		}
	
		private DayTime(A name) {
			super(name);			
		}
		
		public static <
			X extends AttributeName,
			T extends HasDayTimeInterval.Read<X, T, S>,
			S extends HasDayTimeInterval.Write<X, T, S>			
		>
		IntervalAttribute.DayTime<X, T, S> get(HasIntervalAttribute.DayTime<X, T, S> meta, X a) {
			DayTime<X, T, S> k = meta.getDayTimeIntervalAttribute(a);
		
			if (k == null) {
				if (IntervalType.DayTime.TYPE.equals(a.type())) {
					k = new DayTime<X, T, S>(a);
					meta.register(k);
				}
			}
					
			return k;
		}		
	
		@Override
		public com.appspot.relaxe.value.IntervalHolder.DayTime get(R e) {	
			return e.getInterval(this);
		}
	
		@Override
		public com.appspot.relaxe.value.IntervalHolder.DayTime newHolder(
				com.appspot.relaxe.value.Interval.DayTime newValue) {
			return new IntervalHolder.DayTime(newValue);
		}
	
		@Override
		public void set(W e, com.appspot.relaxe.value.IntervalHolder.DayTime newValue) {
			e.setInterval(this, newValue);	
		}
	
		@Override
		public com.appspot.relaxe.types.IntervalType.DayTime type() {
			return IntervalType.DayTime.TYPE;
		}
	
		@Override
		public void copy(R src, W dest) {
			dest.setInterval(this, src.getInterval(this));
		}
		
		@Override
		public DayTime<A, R, W> self() {
			return this;
		}
		
		@Override
		public com.appspot.relaxe.value.IntervalHolder.DayTime as(ValueHolder<?, ?, ?> holder) {
			return holder.asDayTimeIntervalHolder();
		}
	}
}
