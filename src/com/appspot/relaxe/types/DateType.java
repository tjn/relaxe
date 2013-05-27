/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public class DateType
	extends AbstractPrimitiveType<DateType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9016427671797631046L;
	
	public static final DateType TYPE = new DateType();
	
	private DateType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.DATE;
	}
	
	@Override
	public DateType self() {
		return this;
	}
}
