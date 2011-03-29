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
	R,
	T extends ReferenceType<T>,	
	E extends Entity<A, R, T, E>
>
	extends AbstractPrimitiveAccessor<A, R, T, E, Date, TimestampType, TimestampHolder, TimestampKey<A, R, T, E>> {

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

	public TimestampAccessor(E target, TimestampKey<A, R, T, E> k) {
		super(target, k);
	}
}