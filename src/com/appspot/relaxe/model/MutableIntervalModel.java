/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

import com.appspot.relaxe.rpc.Interval;

public abstract class MutableIntervalModel<T extends Interval<T>>
	extends DefaultMutableValueModel<T> {

	public MutableIntervalModel() {
		super();
	}

	public MutableIntervalModel(T value) {
		super(value);	
	}		
	
	
	public static class YearMonth
		extends MutableIntervalModel<Interval.YearMonth>
	{
		public YearMonth() {	
		}

		public YearMonth(Interval.YearMonth value) {
			super(value);
		}
	}
	
	public static class DayTime
		extends MutableIntervalModel<Interval.DayTime> {
		public DayTime() {
		}
	
		public DayTime(Interval.DayTime value) {
			super(value);
		}
	}
	
}
