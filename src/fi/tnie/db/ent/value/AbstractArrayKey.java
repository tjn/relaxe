/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.ArrayHolder;
import fi.tnie.db.rpc.ArrayValue;
import fi.tnie.db.types.ArrayType;
import fi.tnie.db.types.PrimitiveType;

public abstract class AbstractArrayKey<
	A extends Attribute,
	E,
	O extends Serializable,
	V extends ArrayValue<O>,
	C extends PrimitiveType<C>,
	P extends ArrayType<P, C>,	 
	H extends ArrayHolder<O, V, C, P, H>,
	K extends AbstractArrayKey<A, E, O, V, C, P, H, K>
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
	protected AbstractArrayKey() {
	}
	
	protected AbstractArrayKey(A name) {
		super(name);	
	}

	public abstract K self();
}
