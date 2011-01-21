/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.types.IntegerType;

public class IntegerKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, Integer, IntegerType, IntegerHolder, E, IntegerKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private IntegerKey() {
	}

	public IntegerKey(A name) {
		super(name);
	}

	@Override
	public IntegerType type() {
		return IntegerType.TYPE;
	}

	@Override
	public IntegerValue<A, E> newValue() {
		return new IntegerValue<A, E>(this);
	}

	public Value<A, Integer, IntegerType, IntegerHolder, E, IntegerKey<A,E>> value(E e) {
		return e.value(this);
	}
}
