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
	
	public void set(E e, IntegerHolder newValue) {
		e.setInteger(this, newValue);
	}
	
	public IntegerHolder get(E e) {
		return e.get(this);
	}
	
	@Override
	public IntegerHolder newHolder(Integer newValue) {
		return IntegerHolder.valueOf(newValue);
	}
}
