/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.rpc.Interval;
import com.appspot.relaxe.rpc.IntervalHolder;
import com.appspot.relaxe.types.IntervalType;


public abstract class IntervalAssignment<V extends Interval<V>, T extends IntervalType<T>, H extends IntervalHolder<V, T, H>>
	extends AbstractParameterAssignment<V, T, H> {
	
	private static Logger logger = LoggerFactory.getLogger(IntervalAssignment.class);

	public IntervalAssignment(H value) {
		super(value);
	}
	
	
	public static class YearMonth
		extends IntervalAssignment<Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth> {

		public YearMonth(IntervalHolder.YearMonth value) {
			super(value);
		}

		@Override
		public void assign(PreparedStatement ps, int ordinal, com.appspot.relaxe.rpc.Interval.YearMonth ym) throws SQLException {
			StringBuffer v = new StringBuffer();
			
			if (ym.signum() < 0) {
				v.append(ym.signum());
			}
			v.append(ym.getYear());
			v.append("-");
			v.append(ym.getMonth());								
			ps.setObject(ordinal, v.toString(), IntervalType.YearMonth.TYPE.getSqlType());
		}
	}

	public static class DayTime
		extends IntervalAssignment<Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime> {
	
		public DayTime(IntervalHolder.DayTime value) {
			super(value);
		}
	
		@Override
		public void assign(PreparedStatement ps, int ordinal, com.appspot.relaxe.rpc.Interval.DayTime dt) throws SQLException {
			StringBuffer v = new StringBuffer();
			
			if (dt.signum() < 0) {
				v.append("-");
			}
			
			v.append(dt.getDay());
			v.append(" ");
			v.append(dt.getHour());
			v.append(":");
			v.append(dt.getMinute());
			v.append(":");
			v.append(dt.getSecond());
			
			logger().debug("assign: v=" + v);
	
			ps.setObject(ordinal, v.toString(), IntervalType.DayTime.TYPE.getSqlType());
		}
	}
	
	private static Logger logger() {
		return IntervalAssignment.logger;
	}
}
