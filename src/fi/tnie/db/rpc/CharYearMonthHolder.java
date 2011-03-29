/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.types.CharType;
import fi.tnie.db.types.CharYearMonthIntervalType;
import fi.tnie.db.types.IntervalType;

public class CharYearMonthHolder
	extends VirtualHolder<Interval.YearMonth, String, CharType, CharHolder, IntervalType.YearMonth, CharYearMonthIntervalType, CharYearMonthHolder> {
	
	private CharHolder implementedAs;
	
	public CharYearMonthHolder(Interval.YearMonth v) {
		super(v);
		implementedAs = (v == null) ? CharHolder.NULL_HOLDER : CharHolder.valueOf(v.toString());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7671379434861527849L;
	
	@Override
	public CharYearMonthIntervalType getType() {		
		return CharYearMonthIntervalType.TYPE;
	}

	@Override
	public CharHolder implementedAs() {
		return this.implementedAs;
	}
}
