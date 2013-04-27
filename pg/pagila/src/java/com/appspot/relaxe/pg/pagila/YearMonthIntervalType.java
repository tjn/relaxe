/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.types.DistinctType;

public class YearMonthIntervalType
	extends DistinctType<YearMonthIntervalType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4915976457434883692L;
	public static final YearMonthIntervalType TYPE = new YearMonthIntervalType();
	
	private YearMonthIntervalType() {
		super();
	}

	@Override
	public String getName() {
		return "interval_ym";
	}

	@Override
	public YearMonthIntervalType self() {
		return this;
	}
}
