/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.Decimal;
import com.appspot.relaxe.rpc.DecimalHolder;
import com.appspot.relaxe.types.DecimalType;

public class DecimalAccessor<
	A extends Attribute,		
	E extends HasDecimal<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, Decimal, DecimalType, DecimalHolder, DecimalKey<A, E>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1855077165248914678L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DecimalAccessor() {
	}

	public DecimalAccessor(E target, DecimalKey<A, E> k) {
		super(target, k);
	}
}