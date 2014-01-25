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
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.types.IntervalType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.Interval;
import com.appspot.relaxe.value.IntervalHolder;
import com.appspot.relaxe.value.ValueHolder;

public abstract class IntervalAttribute<
	A extends AttributeName,	
	E,
	V extends Interval<V>, 
	P extends ValueType<P>, 
	H extends IntervalHolder<V, P, H>, 
	K extends IntervalAttribute<A, E, V, P, H, K>
	>
	extends AbstractAttribute<A, E, V, P, H, K>
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
		E extends HasInterval.YearMonth<A, E>
	>
		extends IntervalAttribute<A, E, Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth, IntervalAttribute.YearMonth<A, E>> {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = -8221115262736792946L;

		/**
		 * No-argument constructor for GWT Serialization
		 */	
		@SuppressWarnings("unused")
		private YearMonth() {
		}

		protected YearMonth(HasIntervalAttribute.YearMonth<A, E> meta, A name) {
			super(name);
			meta.register(this);
		}
		
		public static <
			X extends AttributeName,
			T extends HasInterval.YearMonth<X, T>
		>
		YearMonth<X, T> get(HasIntervalAttribute.YearMonth<X, T> meta, X a) {
			YearMonth<X, T> k = meta.getYearMonthIntervalAttribute(a);
		
			if (k == null) {
				if (IntervalType.YearMonth.TYPE.equals(a.type())) {
					k = new YearMonth<X, T>(meta, a);
				}
			}
					
			return k;
		}		

		@Override
		public com.appspot.relaxe.value.IntervalHolder.YearMonth get(E e) {	
			return e.getInterval(this);
		}

		@Override
		public com.appspot.relaxe.value.IntervalHolder.YearMonth newHolder(
				com.appspot.relaxe.value.Interval.YearMonth newValue) {
			return new IntervalHolder.YearMonth(newValue);
		}

		@Override
		public void set(E e, com.appspot.relaxe.value.IntervalHolder.YearMonth newValue) 
			throws EntityRuntimeException {
			e.setInterval(this, newValue);	
		}

		@Override
		public com.appspot.relaxe.types.IntervalType.YearMonth type() {
			return IntervalType.YearMonth.TYPE;
		}

		@Override
		public void copy(E src, E dest) 
			throws EntityRuntimeException {
			dest.setInterval(this, src.getInterval(this));
		}
		
		@Override
		public YearMonth<A, E> self() {
			return this;
		}
		
		
		
		@Override
		public com.appspot.relaxe.value.IntervalHolder.YearMonth as(ValueHolder<?, ?, ?> holder) {
			return holder.asYearMonthIntervalHolder();
		}
	}
	
	public static final class DayTime<
		A extends AttributeName,				
		E extends HasInterval.DayTime<A, E>
	>
		extends IntervalAttribute<A, E, Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime, 
			IntervalAttribute.DayTime<A, E>> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1349100151581333245L;
		
		public DayTime(HasIntervalAttribute.DayTime<A, E> meta, A a) {
			super(a);
			meta.register(this);
		}

		public static <
			X extends AttributeName,
			T extends HasInterval.DayTime<X, T>
		>
		IntervalAttribute.DayTime<X, T> get(HasIntervalAttribute.DayTime<X, T> meta, X a) {			
			IntervalAttribute.DayTime<X, T> k = meta.getDayTimeIntervalAttribute(a);
			
			if (k == null) {								
				if (IntervalType.DayTime.TYPE.equals(a.type())) {
					k = new IntervalAttribute.DayTime<X, T>(meta, a);
				}
			}
					
			return k;
		}

		@Override
		public com.appspot.relaxe.value.IntervalHolder.DayTime get(E e) {
			return e.getInterval(this);
		}

		@Override
		public com.appspot.relaxe.value.IntervalHolder.DayTime newHolder(com.appspot.relaxe.value.Interval.DayTime newValue) {
			return new IntervalHolder.DayTime(newValue);
		}

		@Override
		public void set(E e, com.appspot.relaxe.value.IntervalHolder.DayTime newValue) 
			throws EntityRuntimeException {
			e.setInterval(this, newValue);			
		}

		@Override
		public com.appspot.relaxe.types.IntervalType.DayTime type() {
			return IntervalType.DayTime.TYPE;
		}
				
		@Override
		public DayTime<A, E> self() {
			return this;
		}
		
		@Override
		public com.appspot.relaxe.value.IntervalHolder.DayTime as(ValueHolder<?, ?, ?> holder) {
			return IntervalHolder.DayTime.of(holder);
		}
	}

	
	
//	public static <
//		X extends Attribute,
//		T extends Entity<X, ?, ?, T>
//	>
//	IntervalKey<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
//		IntervalKey<X, T> k = meta.getTimestampKey(a);
//		
//		if (k == null) {
//			AbstractPrimitiveType<?> t = meta.getAttributeType(a);
//			
//			if (t != null && t.getSqlType() == AbstractPrimitiveType.TIMESTAMP) {
//				k = new IntervalKey<X, T>(meta, a);
//			}			
//		}
//				
//		return k;
//	}

//	@Override
//	public TimestampType type() {
//		return TimestampType.TYPE;
//	}
//
//	public void set(E e, TimestampHolder newValue) {
//		e.setTimestamp(this, newValue);
//	}
//	
//	public TimestampHolder get(E e) {
//		return e.getTimestamp(this);
//	}
//	
//	@Override
//	public TimestampHolder newHolder(Date newValue) {
//		return TimestampHolder.valueOf(newValue);
//	}
//	
//	@Override
//	public IntervalKey<A, E> normalize(EntityMetaData<A, ?, ?, E> meta) {
//		return meta.getTimestampKey(name());
//	}


}
