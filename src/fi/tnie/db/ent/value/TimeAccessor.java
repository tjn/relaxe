/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.TimeType;

public class TimeAccessor<
	A extends Attribute,	
	T extends ReferenceType<T, ?>,	
	E extends Entity<A, ?, T, E, ?, ?, ?>
>
	extends AbstractPrimitiveAccessor<A, T, E, Date, TimeType, TimeHolder, TimeKey<A, T, E>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7412092659898638958L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private TimeAccessor() {
	}

	public TimeAccessor(E target, TimeKey<A, T, E> k) {
		super(target, k);
	}
}