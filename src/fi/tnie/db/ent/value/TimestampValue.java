/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.types.TimestampType;

public class TimestampValue<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveValue<A, Date, TimestampType, TimestampHolder, E, TimestampKey<A, E>> {

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

	public TimestampValue(E target, TimestampKey<A, E> k) {
		super(target, k);
	}
}