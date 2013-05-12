/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;


import java.util.Calendar;
import java.util.Date;

import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.AbstractPrimitiveType;

public abstract class AbstractDateAssignment<T extends AbstractPrimitiveType<T>, H extends AbstractPrimitiveHolder<Date, T, H>>	
	extends AbstractParameterAssignment<Date, T, H> {

	public AbstractDateAssignment(H value) {
		super(value);		
	}

	protected Calendar getCalendar() {
		return null;
	}
}
