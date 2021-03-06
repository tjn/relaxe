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
package com.appspot.relaxe;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.types.IntervalType;
import com.appspot.relaxe.value.Interval;
import com.appspot.relaxe.value.IntervalHolder;


public abstract class IntervalExtractor<V extends Interval<V>, T extends IntervalType<T>, H extends IntervalHolder<V, T, H>> 
	extends ValueExtractor<V, T, H> {

	public IntervalExtractor(int column) {
		super(column);
	}

	@Override
	public abstract H extract(ResultSet rs) throws SQLException;
	
	public static abstract class YearMonth
		extends IntervalExtractor<Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth> {

		public YearMonth(int column) {
			super(column);
		}		
	}

	public static abstract class DayTime
		extends IntervalExtractor<Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime> {

		public DayTime(int column) {
			super(column);
		}
	}
}


