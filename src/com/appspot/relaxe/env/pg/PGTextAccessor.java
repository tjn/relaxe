/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.value.AbstractPrimitiveAccessor;


public class PGTextAccessor<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?> & HasPGText<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, String, PGTextType, PGTextHolder, PGTextKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2731889274638406123L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private PGTextAccessor() {
	}

	public PGTextAccessor(E target, PGTextKey<A, E> k) {
		super(target, k);
	}
}