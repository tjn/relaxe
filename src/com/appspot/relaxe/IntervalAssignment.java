/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.appspot.relaxe.rpc.Interval;
import com.appspot.relaxe.rpc.IntervalHolder;
import com.appspot.relaxe.types.IntervalType;


public abstract class IntervalAssignment<V extends Interval<V>, T extends IntervalType<T>, H extends IntervalHolder<V, T, H>>
	extends AbstractParameterAssignment<V, T, H> {
	
	private static Logger logger = Logger.getLogger(IntervalAssignment.class);

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