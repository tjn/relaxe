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
	R,
	T extends ReferenceType<T>,	
	E extends Entity<A, R, T, E>
>
	extends AbstractPrimitiveAccessor<A, R, T, E, Date, TimeType, TimeHolder, TimeKey<A, R, T, E>> {

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

	public TimeAccessor(E target, TimeKey<A, R, T, E> k) {
		super(target, k);
	}
}