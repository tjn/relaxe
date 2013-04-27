/*
 * Copyright (c) 2009-2013 Topi Nieminen
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