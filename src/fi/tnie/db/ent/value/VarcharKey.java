/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.VarcharType;


public class VarcharKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, String, VarcharType, VarcharHolder, E, VarcharKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private VarcharKey() {
	}

	public VarcharKey(A name) {
		super(name);
	}

	@Override
	public VarcharType type() {
		return VarcharType.TYPE;
	}

	@Override
	public VarcharValue<A, E> newValue() {
		return new VarcharValue<A, E>(this);
	}

	public Value<A, String, VarcharType, VarcharHolder, E, VarcharKey<A,E>> value(E e) {
		return e.value(this);
	}
}
