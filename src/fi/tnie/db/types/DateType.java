/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class DateType
	extends PrimitiveType<DateType> {
	
	public static final DateType TYPE = new DateType();
	
	private DateType() {
		super(Type.DATE);
	}
}
