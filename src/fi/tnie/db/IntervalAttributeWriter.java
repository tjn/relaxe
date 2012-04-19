/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.IntervalKey;
import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.IntervalType;
import fi.tnie.db.types.ReferenceType;

public abstract class IntervalAttributeWriter<
	A extends Attribute,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>,
	V extends Interval<V>,
	P extends IntervalType<P>,
	H extends IntervalHolder<V, P>,
	K extends IntervalKey<A, T, E, V, P, H, K>
>	
	extends AbstractAttributeWriter<A, T, E, V, P, H, K>	
{
		
	public IntervalAttributeWriter(K key, int index) {		
		super(key, index);		
	}		

	public static class YearMonth	<
		A extends Attribute,
		T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
		E extends Entity<A, ?, T, E, ?, ?, ?, ?>	
	>
		extends IntervalAttributeWriter<A, T, E, Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth, IntervalKey.YearMonth<A, T, E>>
	{
		private IntervalExtractor.YearMonth extractor;
		
		public YearMonth(IntervalKey.YearMonth<A, T, E> key, int index) {
			super(key, index);
		}
	
		@Override
		protected fi.tnie.db.rpc.IntervalHolder.YearMonth as(PrimitiveHolder<?, ?> ph) {
			return ph.asYearMonthIntervalHolder();
		}
	
		@Override
		protected fi.tnie.db.rpc.IntervalHolder.YearMonth extract(ResultSet src)
				throws SQLException {
			return extractor.extractValue(src);
		}
	}

	public static class DayTime	<
		A extends Attribute,
		T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
		E extends Entity<A, ?, T, E, ?, ?, ?, ?>	
	>
		extends IntervalAttributeWriter<A, T, E, Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime, IntervalKey.DayTime<A, T, E>>
	{
		private IntervalExtractor.DayTime extractor;
		
		public DayTime(IntervalKey.DayTime<A, T, E> key, int index) {
			super(key, index);
		}

		@Override
		protected fi.tnie.db.rpc.IntervalHolder.DayTime as(PrimitiveHolder<?, ?> ph) {
			return ph.asDayTimeIntervalHolder();
		}

		@Override
		protected fi.tnie.db.rpc.IntervalHolder.DayTime extract(ResultSet src)
				throws SQLException {
			return extractor.extractValue(src);
		}
	}
}
