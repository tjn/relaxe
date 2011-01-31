/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.PrimitiveHolder;

public class TimeType
	extends PrimitiveType<TimeType> {
	
	public static final TimeType TYPE = new TimeType();
	
	private TimeType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.TIME;
	}

	@Override
	public PrimitiveHolder<?, TimeType> nil() {
		return null; // TODO: implement TimeHolder		
	}
}
