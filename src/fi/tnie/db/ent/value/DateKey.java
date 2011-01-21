/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;
import java.util.Date;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.types.DateType;

public class DateKey<A extends Serializable, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, Date, DateType, DateHolder, E, DateKey<A, E>>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8003688473297829554L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DateKey() {
	}

	public DateKey(A name) {
		super(name);
	}

	@Override
	public DateType type() {
		return DateType.TYPE;
	}

	@Override
	public DateValue<A, E> newValue() {
		return new DateValue<A, E>(this);
	}	
	
	public Value<A, Date, DateType, DateHolder, E, DateKey<A,E>> value(E e) {
		return e.value(this);
	}		
}
