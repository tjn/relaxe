/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.types.CharType;

public class CharValue<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveValue<A, String, CharType, CharHolder, E, CharKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3149610573030307419L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private CharValue() {
	}

	public CharValue(E target, CharKey<A, E> k) {
		super(target, k);
	}
}