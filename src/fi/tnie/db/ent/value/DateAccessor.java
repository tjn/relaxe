/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.types.DateType;
import fi.tnie.db.types.ReferenceType;

public class DateAccessor<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T>,	
	E extends Entity<A, R, T, E>
	>
	extends AbstractPrimitiveAccessor<A, R, T, E, Date, DateType, DateHolder, DateKey<A, R, T, E>> {

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

	public DateAccessor(E target, DateKey<A, R, T, E> k) {
		super(target, k);
	}
}