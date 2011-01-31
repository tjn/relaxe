/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.PrimitiveHolder;

public class DateType
	extends PrimitiveType<DateType> {
	
	public static final DateType TYPE = new DateType();
	
	private DateType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.DATE;
	}

	@Override
	public PrimitiveHolder<?, DateType> nil() {
		return DateHolder.NULL_HOLDER;
	}
}
