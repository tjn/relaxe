/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.IntervalHolder;


public interface HasInterval {


	public interface YearMonth<
		A extends Attribute, 
		E extends HasInterval.YearMonth<A, E>
	> {
		IntervalHolder.YearMonth getInterval(IntervalKey.YearMonth<A, E> key);
		void setInterval(IntervalKey.YearMonth<A, E> key, IntervalHolder.YearMonth newValue);
	}
	
	
	public interface DayTime<
		A extends Attribute, 
		E extends HasInterval.DayTime<A, E>
	> {
		IntervalHolder.DayTime getInterval(IntervalKey.DayTime<A, E> key);
		void setInterval(IntervalKey.DayTime<A, E> key, IntervalHolder.DayTime newValue);
	}	
}
