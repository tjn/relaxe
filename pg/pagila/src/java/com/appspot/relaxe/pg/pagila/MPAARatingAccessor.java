/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.value.AbstractPrimitiveAccessor;

public class MPAARatingAccessor<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?> & HasMPAARating<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, MPAARating, MPAARatingType, MPAARatingHolder, MPAARatingKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2731889274638406123L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private MPAARatingAccessor() {
	}

	public MPAARatingAccessor(E target, MPAARatingKey<A, E> k) {
		super(target, k);
	}
}