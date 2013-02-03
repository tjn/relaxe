/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class TimeType
	extends PrimitiveType<TimeType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5725206545838820641L;
	
	public static final TimeType TYPE = new TimeType();
	
	private TimeType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.TIME;
	}
	
	@Override
	public TimeType self() {
		return this;
	}
}
