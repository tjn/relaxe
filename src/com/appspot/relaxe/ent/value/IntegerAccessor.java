/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.rpc.IntegerHolder;
import com.appspot.relaxe.types.IntegerType;

public class IntegerAccessor<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?> & HasInteger<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, Integer, IntegerType, IntegerHolder, IntegerKey<A, E>> {

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

	public IntegerAccessor(E target, IntegerKey<A, E> k) {
		super(target, k);
	}
}