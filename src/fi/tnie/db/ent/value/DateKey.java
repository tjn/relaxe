/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.types.DateType;

public class DateKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
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

	public void set(E e, DateHolder newValue) {
		e.setDate(this, newValue);
	}
	
	public DateHolder get(E e) {
		return e.get(this);
	}
	
	@Override
	public DateHolder newHolder(Date newValue) {
		return DateHolder.valueOf(newValue);
	}

}
