/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.types.IntervalType;
import fi.tnie.db.types.PrimitiveType;

public abstract class IntervalHolder<V extends Interval<?>, T extends PrimitiveType<T>>
	extends PrimitiveHolder<V, T> {
	
	private V value;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479889538730689743L;

	
	public static class YearMonth
		extends IntervalHolder<Interval.YearMonth, IntervalType.YearMonth> {

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
		public fi.tnie.db.types.IntervalType.YearMonth getType() {
			return IntervalType.YearMonth.TYPE;
		}
		
		public static IntervalHolder.YearMonth valueOf(Interval.YearMonth value) {
			return (value == null) ? NULL : new YearMonth(value);
		}
		
		@Override
		public YearMonth asYearMonthIntervalHolder() {
			return this;
		}
	}
	
	
	public static class DayTime
		extends IntervalHolder<Interval.DayTime, IntervalType.DayTime> {
			
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
		public fi.tnie.db.types.IntervalType.DayTime getType() {
			return IntervalType.DayTime.TYPE;
		}
		
		public static IntervalHolder.DayTime valueOf(Interval.DayTime value) {
			return (value == null) ? NULL : new DayTime(value);
		}
		
		@Override
		public IntervalHolder.DayTime asDayTimeIntervalHolder() {
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
		return PrimitiveType.OTHER;
	}
}
