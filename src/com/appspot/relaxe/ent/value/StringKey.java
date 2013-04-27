/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;

public abstract class StringKey<
	A extends Attribute,
	E extends HasString<A, E>,	
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<String, P, H>,
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
	
	public abstract K self();
}
