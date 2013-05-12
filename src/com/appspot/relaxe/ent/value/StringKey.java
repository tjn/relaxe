/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.AbstractPrimitiveType;

public abstract class StringKey<
	A extends Attribute,
	E extends HasString<A, E>,	
	P extends AbstractPrimitiveType<P>,
	H extends AbstractPrimitiveHolder<String, P, H>,
	K extends PrimitiveKey<A, E, String, P, H, K>
	>
	extends AbstractPrimitiveKey<A, E, String, P, H, K>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected StringKey() {
	}	
	
	protected StringKey(A name) {
		super(name);		
	}
	
	@Override
	public abstract K self();
}
