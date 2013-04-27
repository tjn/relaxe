/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.io.Serializable;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


public abstract class AbstractOtherKey<
	A extends Attribute,
	E,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P, H>,
	K extends AbstractOtherKey<A, E, V, P, H, K>
	>
	extends AbstractPrimitiveKey<A, E, V, P, H, K>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected AbstractOtherKey() {
	}
	
	protected AbstractOtherKey(A name) {
		super(name);	
	}



	public abstract K self();
}
