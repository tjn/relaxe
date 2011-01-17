/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class TimeType
	extends PrimitiveType<TimeType> {
	
	public static final TimeType TYPE = new TimeType();
	
	private TimeType() {
		super(Type.TIME);
	}
}
