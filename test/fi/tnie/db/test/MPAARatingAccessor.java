/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.AbstractPrimitiveAccessor;

public class MPAARatingAccessor<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?> & HasMPAARating<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, MPAARating, MPAARatingType, MPAARatingHolder, MPAARatingKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2731889274638406123L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private MPAARatingAccessor() {
	}

	public MPAARatingAccessor(E target, MPAARatingKey<A, E> k) {
		super(target, k);
	}
}