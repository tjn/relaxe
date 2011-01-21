/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.types.DoubleType;

public class DoubleKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, Double, DoubleType, DoubleHolder, E, DoubleKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1065150474303051699L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DoubleKey() {
	}

	public DoubleKey(A name) {
		super(name);
	}

	@Override
	public DoubleType type() {
		return DoubleType.TYPE;
	}

	@Override
	public DoubleValue<A, E> newValue() {
		return new DoubleValue<A, E>(this);
	}

	public Value<A, Double, DoubleType, DoubleHolder, E, DoubleKey<A,E>> value(E e) {
		return e.value(this);
	}
}
