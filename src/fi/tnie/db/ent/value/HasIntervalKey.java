/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;

public interface HasIntervalKey	{
		
	interface YearMonth<
		A extends Attribute,
		E extends HasInterval.YearMonth<A, E>	
	> {
		IntervalKey.YearMonth<A, E> getYearMonthIntervalKey(A a);	
		void register(IntervalKey.YearMonth<A, E> key);		
	}

	interface DayTime<
		A extends Attribute,
		E extends HasInterval.DayTime<A, E>	
	> {
		IntervalKey.DayTime<A, E> getDayTimeIntervalKey(A a);	
		void register(IntervalKey.DayTime<A, E> key);		
	}

}
