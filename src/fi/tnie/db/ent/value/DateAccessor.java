/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.types.DateType;
import fi.tnie.db.types.ReferenceType;

public class DateAccessor<
	A extends Attribute,	
	T extends ReferenceType<A, ?, T, E, ?, ?, ?>,	
	E extends Entity<A, ?, T, E, ?, ?, ?>
	>
	extends AbstractPrimitiveAccessor<A, T, E, Date, DateType, DateHolder, DateKey<A, T, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6884358447490019294L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DateAccessor() {
	}

	public DateAccessor(E target, DateKey<A, T, E> k) {
		super(target, k);
	}
}