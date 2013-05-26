/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.CharHolder;
import com.appspot.relaxe.types.CharType;

public class CharAccessor<
	A extends Attribute,	
	E extends HasChar<A, E> & HasString<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, String, CharType, CharHolder, CharKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3149610573030307419L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private CharAccessor() {
	}

	public CharAccessor(E target, CharKey<A, E> k) {
		super(target, k);
	}

	@Override
	public void setHolder(CharHolder newHolder) {
		getTarget().setChar(key(), newHolder);
	}
}