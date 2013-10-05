/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.io.Serializable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


public class MutableValueParameter<
	V extends Serializable,
	T extends PrimitiveType<T>, 
	H extends PrimitiveHolder<V, T, H>
>
	extends AbstractParameter<V, T, H> 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8609263476546435725L;
	
	private H value;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private MutableValueParameter() {
	}
	
	@Override
	public H getValue() {
		return this.value;
	}
	
	public MutableValueParameter(Column column, H value) {
		super(column);
		setValue(value);		
	}

	public void setValue(H value) {
		if (value == null) {
			throw new NullPointerException("value");
		}
		
		this.value = value;
	}
	
	@Override
	public boolean isMutable() {
		return true;
	}
}
