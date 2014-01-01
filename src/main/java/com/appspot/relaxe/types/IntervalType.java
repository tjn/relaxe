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
package com.appspot.relaxe.types;

public abstract class IntervalType<I extends IntervalType<I>> 
	extends OtherType<I> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -714149221314980568L;

	public static class YearMonth
		extends IntervalType<IntervalType.YearMonth> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 797031334739315201L;
		
		public static final IntervalType.YearMonth TYPE = new IntervalType.YearMonth();
				
		private YearMonth() {
			super();
		}
		
		@Override
		public YearMonth self() {
			return this;
		}

		@Override
		public String getName() {
			return "interval_ym";
		}
	}
	
	public static class DayTime
		extends IntervalType<IntervalType.DayTime> {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 5642594562023667380L;
		public static final IntervalType.DayTime TYPE = new IntervalType.DayTime();
				
		private DayTime() {
			super();
		}
	
		@Override
		public DayTime self() {
			return this;
		}

		@Override
		public String getName() {
			return "interval";
		}
	}	

	protected IntervalType() {
	}	
}