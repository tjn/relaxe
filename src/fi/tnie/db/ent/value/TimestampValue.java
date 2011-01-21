/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;
import java.util.Date;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.types.TimestampType;

public class TimestampValue<A extends Serializable, E extends Entity<A, ?, ?, E>>
	extends AbstractValue<A, Date, TimestampType, TimestampHolder, E, TimestampKey<A, E>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7871095796458117502L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private TimestampValue() {
	}
	
	public TimestampValue(TimestampKey<A, E> k) {
		super(k);
	}
		
	@Override
	public void set(Date newValue) {
		setHolder(TimestampHolder.valueOf(newValue));
	}
	
}