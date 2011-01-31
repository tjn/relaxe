/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.TimestampHolder;

public class TimestampType
	extends PrimitiveType<TimestampType> {
	
	public static final TimestampType TYPE = new TimestampType();
	
	private TimestampType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.TIMESTAMP;
	}
	
	@Override
	public PrimitiveHolder<?, TimestampType> nil() {
		return TimestampHolder.NULL_HOLDER;
	}
	
}
