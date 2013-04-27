/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.rpc.LongVarBinaryHolder;
import com.appspot.relaxe.types.LongVarBinaryType;

public class LongVarBinaryAccessor<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?> & HasLongVarBinary<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, LongVarBinary, LongVarBinaryType, LongVarBinaryHolder, LongVarBinaryKey<A, E>> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1725602690923907736L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private LongVarBinaryAccessor() {
	}

	public LongVarBinaryAccessor(E target, LongVarBinaryKey<A, E> k) {
		super(target, k);
	}
}