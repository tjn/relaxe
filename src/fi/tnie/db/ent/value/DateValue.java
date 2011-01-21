/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.types.DateType;

public class DateValue<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends AbstractValue<A, Date, DateType, DateHolder, E, DateKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6884358447490019294L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DateValue() {
	}

	public DateValue(DateKey<A, E> k) {
		super(k);
	}

	@Override
	public void set(Date newValue) {
		setHolder(DateHolder.valueOf(newValue));
	}

}