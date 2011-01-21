/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class TimestampType
	extends PrimitiveType<TimestampType> {
	
	public static final TimestampType TYPE = new TimestampType();
	
	private TimestampType() {
	}
	
	@Override
	public int getSqlType() {
		return Type.TIMESTAMP;
	}
}
