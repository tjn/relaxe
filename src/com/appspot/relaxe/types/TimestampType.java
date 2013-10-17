/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public class TimestampType
	extends AbstractPrimitiveType<TimestampType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4905880670176790882L;
	public static final TimestampType TYPE = new TimestampType();
	
	private TimestampType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.TIMESTAMP;
	}
	
	@Override
	public TimestampType self() {
		return this;
	}
	
	@Override
	public String getName() {
		return "TIMESTAMP";
	}
}
