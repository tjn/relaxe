/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.BooleanHolder;
import fi.tnie.db.types.BooleanType;

public class BooleanAccessor<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?> & HasBoolean<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, Boolean, BooleanType, BooleanHolder, BooleanKey<A, E>> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4785085933557681259L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private BooleanAccessor() {
	}

	public BooleanAccessor(E target, BooleanKey<A, E> k) {
		super(target, k);
	}
}