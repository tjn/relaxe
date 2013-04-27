/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.value.AbstractPrimitiveAccessor;
import com.appspot.relaxe.pg.pagila.HasPGTextArray;
import com.appspot.relaxe.rpc.StringArray;


public class PGTextArrayAccessor<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?> & HasPGTextArray<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, StringArray, PGTextArrayType, PGTextArrayHolder, PGTextArrayKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2731889274638406123L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private PGTextArrayAccessor() {
	}

	public PGTextArrayAccessor(E target, PGTextArrayKey<A, E> k) {
		super(target, k);
	}
}