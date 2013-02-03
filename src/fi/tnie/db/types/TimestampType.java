/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class TimestampType
	extends PrimitiveType<TimestampType> {
	
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
}
