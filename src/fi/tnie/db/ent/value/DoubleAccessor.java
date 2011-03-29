/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.types.DoubleType;
import fi.tnie.db.types.ReferenceType;

public class DoubleAccessor<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,	
	E extends Entity<A, R, T, E>
>
	extends AbstractPrimitiveAccessor<A, R, T, E, Double, DoubleType, DoubleHolder, DoubleKey<A, R, T, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5737849327578345205L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DoubleAccessor() {
	}

	public DoubleAccessor(E target, DoubleKey<A, R, T, E> k) {
		super(target, k);
	}
}