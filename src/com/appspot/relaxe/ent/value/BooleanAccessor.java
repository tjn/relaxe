/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.rpc.BooleanHolder;
import com.appspot.relaxe.types.BooleanType;

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