/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.types.TimestampType;

public class TimestampKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, Date, TimestampType, TimestampHolder, E, TimestampKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7215861304511882107L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private TimestampKey() {
	}

	public TimestampKey(A name) {
		super(name);
	}

	@Override
	public TimestampType type() {
		return TimestampType.TYPE;
	}

	public void set(E e, TimestampHolder newValue) {
		e.setTimestamp(this, newValue);
	}
	
	public TimestampHolder get(E e) {
		return e.get(this);
	}
	
	@Override
	public TimestampHolder newHolder(Date newValue) {
		return TimestampHolder.valueOf(newValue);
	}
	
}
