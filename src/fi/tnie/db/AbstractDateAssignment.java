/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


import java.util.Calendar;
import java.util.Date;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public abstract class AbstractDateAssignment<T extends PrimitiveType<T>, H extends PrimitiveHolder<Date, T>>	
	extends AbstractParameterAssignment<Date, T, H> {

	public AbstractDateAssignment(H value) {
		super(value);		
	}

	protected Calendar getCalendar() {
		return null;
	}
}
