/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.ReferenceType;

public class IntegerAccessor<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>>
	extends AbstractPrimitiveAccessor<A, R, T, E, Integer, IntegerType, IntegerHolder, IntegerKey<A, R, T, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2731889274638406123L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private IntegerAccessor() {
	}

	public IntegerAccessor(E target, IntegerKey<A, R, T, E> k) {
		super(target, k);
	}
}