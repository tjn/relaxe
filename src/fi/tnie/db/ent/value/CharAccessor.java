/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.types.CharType;
import fi.tnie.db.types.ReferenceType;

public class CharAccessor<
	A extends Attribute,	
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,	
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>
>
	extends AbstractPrimitiveAccessor<A, T, E, String, CharType, CharHolder, CharKey<A, T, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3149610573030307419L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private CharAccessor() {
	}

	public CharAccessor(E target, CharKey<A, T, E> k) {
		super(target, k);
	}
}