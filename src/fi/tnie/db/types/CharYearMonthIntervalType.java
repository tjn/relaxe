/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.types.IntervalType.YearMonth;

public class CharYearMonthIntervalType
	extends VirtualType<CharYearMonthIntervalType, CharType, IntervalType.YearMonth> {
	
	public static final CharYearMonthIntervalType TYPE = new CharYearMonthIntervalType(); 

	@Override
	public YearMonth virtualized() {
		return IntervalType.YearMonth.TYPE;
	}

	@Override
	public CharType implementedAs() {
		return CharType.TYPE;
	}
}
