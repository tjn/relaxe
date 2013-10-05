/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.io.Serializable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


public class ImmutableValueParameter<
	V extends Serializable,
	T extends PrimitiveType<T>, 
	H extends PrimitiveHolder<V, T, H>
>
	extends AbstractParameter<V, T, H> 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2274588828067066425L;
	
	
	private H value;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ImmutableValueParameter() {
	}
	
	public ImmutableValueParameter(Column column, H value) {
		super(column);
		
		if (value == null) {
			throw new NullPointerException("value");
		}
		
		this.value = value;
	}


	@Override
	public H getValue() {
		return value;
	}
	
	@Override
	public boolean isMutable() {
		return false;
	}
}
