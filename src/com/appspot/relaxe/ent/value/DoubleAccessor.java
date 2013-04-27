/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.DoubleHolder;
import com.appspot.relaxe.types.DoubleType;

public class DoubleAccessor<
	A extends Attribute,
	E extends HasDouble<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, Double, DoubleType, DoubleHolder, DoubleKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5737849327578345205L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DoubleAccessor() {
	}

	public DoubleAccessor(E target, DoubleKey<A, E> k) {
		super(target, k);
	}
}