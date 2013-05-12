/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.DoubleHolder;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.DoubleType;
import com.appspot.relaxe.types.AbstractPrimitiveType;

public final class DoubleKey<	
	A extends Attribute,	
	E extends HasDouble<A, E>
>
	extends AbstractPrimitiveKey<A, E, Double, DoubleType, DoubleHolder, DoubleKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1065150474303051699L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DoubleKey() {
	}

	private DoubleKey(HasDoubleKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasDouble<X, T>
	>
	DoubleKey<X, T> get(HasDoubleKey<X, T> meta, X a) {
		DoubleKey<X, T> k = meta.getDoubleKey(a);
		
		if (k == null) {
			AbstractPrimitiveType<?> t = a.type();
			
			if (DoubleType.TYPE.equals(t)) {
				k = new DoubleKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public DoubleType type() {
		return DoubleType.TYPE;
	}

	@Override
	public void set(E e, DoubleHolder newValue) 
		throws EntityRuntimeException {
		e.setDouble(this, newValue);
	}
	
	@Override
	public DoubleHolder get(E e) 
		throws EntityRuntimeException {
		return e.getDouble(this);
	}

	@Override
	public DoubleHolder newHolder(Double newValue) {
		return DoubleHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setDouble(this, src.getDouble(this));
	}
	
	@Override
	public DoubleKey<A, E> self() {
		return this;
	}
	
	@Override
	public DoubleHolder as(AbstractPrimitiveHolder<?, ?, ?> holder) {
		return DoubleHolder.of(holder);
	}
}
