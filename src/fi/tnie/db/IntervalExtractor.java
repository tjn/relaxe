/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.types.IntervalType;

public abstract class IntervalExtractor<V extends Interval<V>, T extends IntervalType<T>, H extends IntervalHolder<V, T, H>> 
	extends ValueExtractor<V, T, H> {

	public IntervalExtractor(int column) {
		super(column);
	}

	@Override
	public abstract H extractValue(ResultSet rs) throws SQLException;
	
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


