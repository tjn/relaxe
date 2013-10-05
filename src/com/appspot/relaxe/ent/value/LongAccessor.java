/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.rpc.LongHolder;
import com.appspot.relaxe.types.LongType;

public class LongAccessor<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?> & HasLong<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, Long, LongType, LongHolder, LongKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2731889274638406123L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private LongAccessor() {
	}

	public LongAccessor(E target, LongKey<A, E> k) {
		super(target, k);
	}
}