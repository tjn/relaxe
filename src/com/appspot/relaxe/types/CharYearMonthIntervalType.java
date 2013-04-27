/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

import com.appspot.relaxe.types.IntervalType.YearMonth;

public class CharYearMonthIntervalType
	extends VirtualType<CharYearMonthIntervalType, CharType, IntervalType.YearMonth> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2712539086523771719L;
	public static final CharYearMonthIntervalType TYPE = new CharYearMonthIntervalType(); 

	@Override
	public YearMonth virtualized() {
		return IntervalType.YearMonth.TYPE;
	}

	@Override
	public CharType implementedAs() {
		return CharType.TYPE;
	}

	@Override
	public CharYearMonthIntervalType self() {
		return this;
	}
}
