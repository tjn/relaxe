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
import com.appspot.relaxe.rpc.Interval;
import com.appspot.relaxe.rpc.IntervalHolder;
import com.appspot.relaxe.types.IntervalType;

public class IntervalAccessor<
	A extends AttributeName,
	E,
	V extends Interval<V>, 
	P extends IntervalType<P>, 
	H extends IntervalHolder<V, P, H>,
	K extends IntervalAttribute<A, E, V, P, H, K>
>
	extends AbstractPrimitiveAccessor<A, E, V, P, H, K> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6884358447490019294L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected IntervalAccessor() {
	}

	public IntervalAccessor(E target, K k) {
		super(target, k);
	}
	
	public static class DayTime<
		A extends AttributeName,		
		E extends HasInterval.DayTime<A, E>
	>
		extends IntervalAccessor<A, E, Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime, IntervalAttribute.DayTime<A, E>> {

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

		public DayTime(E target, IntervalAttribute.DayTime<A, E> k) {
			super(target, k);		
		}
	}
	

	public static class YearMonth<
		A extends AttributeName,		
		E extends HasInterval.YearMonth<A, E>
	>
		extends IntervalAccessor<A, E, Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth, IntervalAttribute.YearMonth<A, E>> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7638187752897298152L;
		
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private YearMonth() {	
		}
		
		public YearMonth(E target, IntervalAttribute.YearMonth<A, E> k) {
			super(target, k);		
		}		
	}

}