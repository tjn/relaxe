/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public abstract class IntervalType<I extends IntervalType<I>> 
	extends PrimitiveType<I> {
	
	public static class YearMonth
		extends IntervalType<IntervalType.YearMonth> {
		
		public static final IntervalType.YearMonth TYPE = new IntervalType.YearMonth();
				
		private YearMonth() {
			super();
		}
	}
	
	public static class DayTime
		extends IntervalType<IntervalType.DayTime> {
	
		public static final IntervalType.DayTime TYPE = new IntervalType.DayTime();
				
		private DayTime() {
			super();
		}
	
		@Override
		public int getSqlType() {
			return PrimitiveType.OTHER;
		}		
	}	

	protected IntervalType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.OTHER;
	}	
}