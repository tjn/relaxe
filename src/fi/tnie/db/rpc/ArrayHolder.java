/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;
import fi.tnie.db.types.ArrayType;
import fi.tnie.db.types.PrimitiveType;

public abstract class ArrayHolder<
	V extends Serializable, 
	A extends ArrayValue<V>, 
	E extends PrimitiveType<E>, 
	T extends ArrayType<T, E>, 
	H extends ArrayHolder<V, A, E, T, H>
>
	extends PrimitiveHolder<A, T, H> {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -891833513022624898L;
	
	private A value;
		
	protected ArrayHolder() {
		super();
	}
	
	protected ArrayHolder(A value) {
		this.value = value;
	}
	
	@Override
	public A value() {
		return this.value;
	}
	
	
	
	
	
}
