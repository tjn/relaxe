/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.types.TimeType;

public class TimeAccessor<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveAccessor<A, Date, TimeType, TimeHolder, E, TimeKey<A, E>> {

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

	public TimeAccessor(E target, TimeKey<A, E> k) {
		super(target, k);
	}
}