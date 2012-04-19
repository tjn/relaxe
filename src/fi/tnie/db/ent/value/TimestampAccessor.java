/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.TimestampType;

public class TimestampAccessor<
	A extends Attribute,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,	
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>
>
	extends AbstractPrimitiveAccessor<A, T, E, Date, TimestampType, TimestampHolder, TimestampKey<A, T, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7871095796458117502L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private TimestampAccessor() {
	}

	public TimestampAccessor(E target, TimestampKey<A, T, E> k) {
		super(target, k);
	}
}